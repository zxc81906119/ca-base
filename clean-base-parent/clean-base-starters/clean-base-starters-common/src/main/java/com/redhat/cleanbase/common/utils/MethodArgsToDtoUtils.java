package com.redhat.cleanbase.common.utils;

import lombok.NonNull;
import lombok.val;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class MethodArgsToDtoUtils {
    private MethodArgsToDtoUtils() {
        throw new UnsupportedOperationException();
    }

    public static void printClassAllMethodToDto(@NonNull Class<?> someClass) {
        val methods = someClass.getDeclaredMethods();
        Arrays.stream(methods)
                .forEach((method) -> {
                    val methodName = method.getName();
                    val className = methodName.substring(0, 1).toUpperCase() + methodName.substring(1) + "Dto";
                    val parameters = method.getParameters();
                    System.out.println("@Data");
                    val generics = new HashSet<String>();
                    val fieldsDeclareStringBuilder = new StringBuilder();
                    for (Parameter parameter : parameters) {
                        val name = parameter.getName();
                        val parameterizedType = parameter.getParameterizedType();
                        fieldsDeclareStringBuilder.append("private")
                                .append(" ")
                                .append(getClassName(parameterizedType, generics))
                                .append(" ")
                                .append(name)
                                .append(";")
                                .append(System.lineSeparator());
                    }

                    val genericString = generics.isEmpty() ? "" : "<" + String.join(",", generics) + ">";
                    val classDeclare = "public class " +
                            className +
                            genericString +
                            "{";
                    System.out.println(classDeclare);
                    System.out.print(fieldsDeclareStringBuilder);
                    System.out.println("}");
                });
    }

    public static StringBuilder getClassName(Type type, Set<String> generics) {
        val stringBuilder = new StringBuilder();

        if (type instanceof Class) {
            return stringBuilder.append(((Class<?>) type).getSimpleName());
        }

        if (type instanceof ParameterizedType) {
            val parameterizedType = (ParameterizedType) type;
            stringBuilder
                    .append(((Class<?>) parameterizedType.getRawType()).getSimpleName())
                    .append("<");
            val actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                val actualTypeArgument = actualTypeArguments[i];
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(getClassName(actualTypeArgument, generics));
            }
            return stringBuilder.append(">");
        }

        if (type instanceof TypeVariable) {
            // 類物件要多加上範行,用set
            val typeName = type.getTypeName();
            generics.add(typeName);
            return stringBuilder.append(typeName);
        }

        if (type instanceof WildcardType) {
            val wildcardType = (WildcardType) type;
            val upperBounds = wildcardType.getUpperBounds();
            val lowerBounds = wildcardType.getLowerBounds();
            stringBuilder.append("?");
            val father = upperBounds[0];
            if (!Object.class.equals(father)) {
                stringBuilder
                        .append(" ")
                        .append("extends")
                        .append(" ")
                        .append(getClassName(father, generics));
            }
            if (lowerBounds.length != 0) {
                stringBuilder
                        .append(" ")
                        .append("super")
                        .append(" ")
                        .append(getClassName(lowerBounds[0], generics));
            }
            return stringBuilder;
        }

        if (type instanceof GenericArrayType) {
            val replace = type.getTypeName().replace("[]", "");
            generics.add(replace);
            return stringBuilder.append(type.getTypeName());
        }

        throw new UnsupportedOperationException();
    }
}
