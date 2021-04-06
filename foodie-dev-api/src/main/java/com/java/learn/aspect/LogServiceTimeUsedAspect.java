package com.java.learn.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LogServiceTimeUsedAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceTimeUsedAspect.class);

    /**
     * AOP通知：
     * 1. 前置通知：在方法调用之前执行；
     * 2. 后置通知：在方法正常调用之后执行；
     * 3. 环绕通知：在方法执行前后，分别进行通知；
     * 4. 异常通知：方法执行抛出异常则调用；
     * 5. 最终通知：方法调用后执行，无论方法是否抛出异常；
     */

    /**
     * 切面表达式：execution或自定义注解形式
     * execution采用包扫描的方式切入
     * 自定义注解形式只有被该注解标注的方法会被切入，适用于精准切入
     *
     * execution：代表要执行的表达式
     * 第一处 * 代表方法返回类型
     * 第二处 代表aop扫描的包名
     * 第三处 .. 代表该包以及子包
     * 第四处 * 代表包下的类
     * 第五处 .*(**) 代表类中的方法以及参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(public * com.java.learn.service.impl..*.*(..))")
    public Object logServiceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("===== 开始执行: {}.{} =====",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long execTime = endTime - beginTime;

        Method log = logLevelWithExecTime(execTime);
        log.invoke(logger, "===== {}.{} 执行时间: {} 毫秒 =====",
                new Object[]{
                        joinPoint.getTarget().getClass(),
                        joinPoint.getSignature().getName(),
                        execTime
                }
        );

        return result;
    }

    private Method logLevelWithExecTime(long time) throws NoSuchMethodException {
        return time > 3000 ? Logger.class.getMethod("error", String.class, Object[].class)
                : time > 2000 ? Logger.class.getMethod("warn", String.class, Object[].class)
                    : Logger.class.getMethod("info", String.class, Object[].class);
    }
}
