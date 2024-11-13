package com.redhat.cleanbase.web.servlet.aspect.message;

import com.redhat.cleanbase.web.servlet.context.IAppInfoContext;
import com.redhat.cleanbase.common.utils.StringUtil;
import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.base.GenericExceptionNamespace;
import com.redhat.cleanbase.exception.base.GenericRtException;
import com.redhat.cleanbase.web.model.info.IClientAppInfo;
import com.redhat.cleanbase.web.model.info.IGenericAppInfo;
import com.redhat.cleanbase.web.model.info.IServiceAppInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Enumeration;

@Aspect
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageResponseAspect<C extends IClientAppInfo, S extends IServiceAppInfo, G extends IGenericAppInfo> {

    private final IAppInfoContext<C, S, G> appInfoContext;

    @Before("com.redhat.cleanbase.web.pointcut.GenericPointcuts.controllerPointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("=====Before advice starts=====");

        log.info(String.format("訪問 /%s 交易開始",
                joinPoint.getSignature().getName()));

        // 設定前端傳入的追蹤資訊
        setContextClientAppInfo(appInfoContext.findClientAppInfoOrNew());
        // 印前端資訊
        printHttpInfo();

        log.info("=====Before advice ends=====");
    }

    @Around("com.redhat.cleanbase.web.pointcut.GenericPointcuts.controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("=====Around advice starts=====");
        val startDateTime = new Date();
        try {
            return joinPoint.proceed();
        } catch (GenericException | GenericRtException ex) {
            setContextServiceAppInfoFromException(appInfoContext.findServiceAppInfoOrNew(), ex);
            throw ex;
        } finally {
            setContextGenericAppInfo(joinPoint, appInfoContext.findGenericAppInfoOrNew(), startDateTime);
        }
    }

    @After("com.redhat.cleanbase.web.pointcut.GenericPointcuts.controllerPointcut()")
    public void after(JoinPoint joinPoint) {
        log.info("=====After advice starts=====");

        log.info(String.format("訪問 /%s 交易結束...",
                joinPoint.getSignature().getName()));

        log.info("=====After advice ends=====");
    }

    private void setContextGenericAppInfo(ProceedingJoinPoint joinPoint, G genericAppInfo, Date startDateTime) {
        val endTime = new Date();
        val spentTime = endTime.getTime() - startDateTime.getTime();
        log.info("訪問 " + joinPoint.getSignature().getName()
                + " Time spent: " + spentTime);

        log.info("=====Around advice ends=====");

        genericAppInfo.setStartTime(startDateTime);
        genericAppInfo.setEndTime(endTime);
        genericAppInfo.setExecTimeMillis(spentTime);

        setContextCustomGenericAppInfo(genericAppInfo);
    }

    // todo 如有需要再進行複寫方法
    protected void setContextCustomGenericAppInfo(G genericAppInfo) {
    }

    protected abstract void setContextServiceAppInfoFromException(S serviceAppInfo, GenericExceptionNamespace<?> ex);

    protected abstract void setContextClientAppInfo(C clientAppInfo);

    /**
     * 印出HTTP相關資訊
     */
    protected void printHttpInfo() {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        log.info("=========User Info========");
        log.info(String.format("url:%s", request.getRequestURI()));
        log.info(String.format("ip:%s", getIp()));
        log.info(String.format("method:%s", getRequestMethod()));
        if (null != request.getSession(false)) {
            log.info(String.format("Session ID:%s",
                    request.getSession(false).getId()));
        }
        log.info("=========User Info END========");

        log.info("=========Header Info========");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String paramName = headerNames.nextElement();// Key
            String paramValue = request.getHeader(paramName); // Value
            log.info(String.format("key:\"%s\", value:\"%s\"",
                    paramName, paramValue));
        }
        log.info("=========Header Info END=======");

        log.info("=========Parameter Info END=======");
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();// Key
            String paramValue = request.getParameter(paramName); // Value
            log.info(String.format("key:\"%s\", value:\"%s\"",
                    paramName, paramValue));
        }
        log.info("=========Parameter Info END=======");
    }

    /**
     * 取得使用者IP
     */
    protected String getIp() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (StringUtil.isNotBlank(ip) && ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }

    }

    private String getRequestMethod() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getMethod();
    }
}
