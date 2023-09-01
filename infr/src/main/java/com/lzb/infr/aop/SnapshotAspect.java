package com.lzb.infr.aop;

import java.util.Optional;

import com.lzb.domain.common.BaseAggregate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SnapshotAspect {

    /**
     * 支持方法 or 注解
     * @param pjp
     * @param returnVal
     */
    @AfterReturning(pointcut = "execution(* com.lzb.domain.common.repository.GetRepository.get(..)) " +
            "|| @annotation(com.lzb.domain.common.annotation.Snapshot)",
            returning = "returnVal")
    public void handleRequestMethod(JoinPoint pjp, Object returnVal) {
        if (returnVal instanceof BaseAggregate) {
            ((BaseAggregate<?>) returnVal).attachSnapshot();
            return;
        }
        if (returnVal instanceof Optional<?> returnValOpt) {
            if (returnValOpt.isPresent()) {
                Object o = returnValOpt.get();
                if (o instanceof BaseAggregate<?> aggregate) {
                    aggregate.attachSnapshot();
                    return;
                }
            }
        }
    }

}