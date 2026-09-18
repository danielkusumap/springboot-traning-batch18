package com.bootcamp18.training.aspect;

import com.bootcamp18.training.config.filter.servlet.CachedBodyHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.bootcamp18.training.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        HttpServletResponse response = attributes.getResponse();

        String method = request.getMethod();

        String uri = request.getRequestURI();

        String className = joinPoint.getTarget().getClass().getSimpleName();

        String methodName = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();

        log.info("Incoming Request ->> method: [{}], path: {}, class: {}.{}, Starting {}ms", method, uri, className, methodName, start);

        if (request instanceof CachedBodyHttpServletRequest wrapper) {
            String body = wrapper.getContentAsString();
            log.info("Request Body: {}", body);
        }

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;

            log.info("Outgoing response -> [{}] Success in {}ms", response.getStatus(), duration);
            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("Outgoing error -> {} {} | Failed: {} | Time: {}ms", method, uri, e.getMessage(), duration);

            throw e;
        }
    }
}