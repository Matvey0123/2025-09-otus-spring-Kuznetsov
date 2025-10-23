package ru.otus.hw.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(ru.otus.hw.logging.LogIt)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var fullMethodName = joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName();
        log.info("Invocation of method: {}", fullMethodName);

        Object result;
        try {
            result = joinPoint.proceed();
            log.info("Successfully invoked method: {}", fullMethodName);
        } catch (Exception e) {
            log.error("Error during invocation of method: {}, error: {}", fullMethodName, e);
            throw e;
        }
        return result;
    }
}
