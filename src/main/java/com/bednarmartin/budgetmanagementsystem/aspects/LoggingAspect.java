package com.bednarmartin.budgetmanagementsystem.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Around("@annotation(com.bednarmartin.budgetmanagementsystem.annotations.LogMethod)")
    private Object log(ProceedingJoinPoint pjp) throws Throwable {
        Object[] params = pjp.getArgs();
        String methodName = pjp.getSignature().getName();

        log.info("Method {} called with parameters: {}", methodName, Arrays.stream(params).toList());

        Object returnValue = pjp.proceed(params);
        if (returnValue != null) {
            log.info("Method {} returned value: {}", methodName, returnValue);
        }

        return returnValue;
    }
}
