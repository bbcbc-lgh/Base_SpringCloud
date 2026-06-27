package com.gzu.common.log.aspect;

import com.gzu.common.log.annotation.OperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class OperationLogAspect {
    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Object result = joinPoint.proceed();
            watch.stop();
            log.info("operation={}, method={}, cost={}ms, args={}, result={}",
                    operationLog.value(),
                    joinPoint.getSignature().toShortString(),
                    watch.getTotalTimeMillis(),
                    Arrays.toString(joinPoint.getArgs()),
                    result);
            return result;
        } catch (Throwable ex) {
            if (watch.isRunning()) {
                watch.stop();
            }
            log.warn("operation={}, method={}, cost={}ms, args={}, error={}",
                    operationLog.value(),
                    joinPoint.getSignature().toShortString(),
                    watch.getTotalTimeMillis(),
                    Arrays.toString(joinPoint.getArgs()),
                    ex.getMessage());
            throw ex;
        }
    }
}

