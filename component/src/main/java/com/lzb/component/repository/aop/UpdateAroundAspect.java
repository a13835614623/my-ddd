package com.lzb.component.repository.aop;

import java.util.Objects;

import javax.annotation.Priority;

import com.lzb.component.aggregate.BaseAggregate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

/**
 * 更新聚合根，事务提交之后，刷新快照
 */
@Slf4j
@Priority(1)
@Aspect
@Component
public class UpdateAroundAspect {

    /**
     * 支持方法 or 注解
     */
    /*@Around("execution(* UpdateRepository.update(..))")*/
    public Object handleRequestMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        Object[] paramValues = proceedingJoinPoint.getArgs();
        if (Objects.nonNull(paramValues) && paramValues.length > 0) {
            Object aggregateRoot = paramValues[0];
            if (aggregateRoot instanceof BaseAggregate) {
                var snapshotHolder = ((BaseAggregate<?>) aggregateRoot);
                snapshotHolder.checkForVersion();
                try {
                    result = proceedingJoinPoint.proceed(paramValues);
                } catch (Exception e) {
                    log.error("聚合根更新异常 ", e);
                    throw e;
                } finally {
                    snapshotHolder.removeSnapshot();
                }
            }
        }
        return result;
    }

}