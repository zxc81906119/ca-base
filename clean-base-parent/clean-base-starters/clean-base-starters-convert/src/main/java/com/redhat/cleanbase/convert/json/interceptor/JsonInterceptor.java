package com.redhat.cleanbase.convert.json.interceptor;

import com.redhat.cleanbase.common.wrapper.Wrapper;
import com.redhat.cleanbase.convert.json.annotation.MapPathProperty;
import com.redhat.cleanbase.convert.json.model.base.BaseMapWrapper;
import com.redhat.cleanbase.convert.json.util.JsonUtil;
import lombok.val;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ming
 */
public class JsonInterceptor implements MethodInterceptor {
    public static final Class<?> DELEGATING_CLASS = BaseMapWrapper.class;

    static {
        try {
            DELEGATING_CLASS.getMethod(Wrapper.Methods.Fields.unwrap);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> pathValueMap;

    private static Field getField(Method setterMethod) {
        try {
            val fieldName = getFieldName(setterMethod.getName());
            return setterMethod.getDeclaringClass()
                    .getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static Object getSetValue(Object setValue, String defaultValue, boolean keepNull) {
        return Optional.ofNullable(setValue)
                .orElseGet(() ->
                        !keepNull ? defaultValue : null
                );
    }

    private static String getMapPath(Field field, MapPathProperty mapPathProperty) {
        val annoJsonPath = mapPathProperty.path();
        val jsonPath =
                annoJsonPath.isEmpty()
                        ? mapPathProperty.value()
                        : annoJsonPath;
        return jsonPath.isEmpty()
                ? field.getName()
                : jsonPath;
    }

    private static boolean isNotProcess(boolean processNull, Object setValue) {
        return setValue == null && !processNull;
    }

    private static String getFieldName(String setterMethodName) {
        return setterMethodName.substring(3, 4).toLowerCase() + setterMethodName.substring(4);
    }

    private static boolean isSetter(String methodName, Object[] args) {
        return methodName.startsWith("set") && methodName.length() >= 4 && (args.length == 1 || args.length == 2 && args[1] instanceof int[]);
    }

    private Map<String, Object> getPathValueMap() {
        if (pathValueMap == null) {
            pathValueMap = new HashMap<>();
        }
        return pathValueMap;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!DELEGATING_CLASS.isInstance(o)) {
            throw new UnsupportedOperationException("必須是" + BaseMapWrapper.class.getName() + "的子類");
        }

        val methodName = method.getName();

        if (Wrapper.Methods.Fields.unwrap.equals(methodName)) {
            return new HashMap<>(getPathValueMap());
        }

        if (!isSetter(methodName, args)) {
            return methodProxy.invokeSuper(o, args);
        }

        val field = getField(method);
        if (field == null) {
            return methodProxy.invokeSuper(o, args);
        }

        val mapPathProperty = field.getAnnotation(MapPathProperty.class);
        if (mapPathProperty == null) {
            return methodProxy.invokeSuper(o, args);
        }

        val processNull = mapPathProperty.processNull();
        val setValue = args[0];
        if (isNotProcess(processNull, setValue)) {
            // 為了資料一致
            return null;
        }

        val finalMapPath = getMapPath(field, mapPathProperty);
        val defaultValue = mapPathProperty.defaultValue();
        val keepNull = mapPathProperty.keepNull();
        val finalSetValue = getSetValue(setValue, defaultValue, keepNull);
        val indexes = args.length == 1 ? null : (int[]) args[1];
        JsonUtil.setPathValueToMap(getPathValueMap(), finalMapPath, finalSetValue, indexes);

        return methodProxy.invokeSuper(o, args);
    }

}