package com.genspark.cart_service.aspect;

import com.genspark.cart_service.model.Cart;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class CartLoggingAspect {
    // Log CartService methods
    @Before("execution(* com.genspark.cart_service.services.CartService.*(..))")
    public void logBeforeCartService(JoinPoint joinPoint) {
        logMethodExecution(joinPoint);
    }

    // Log CartOrderService methods
    @Before("execution(* com.genspark.cart_service.services.CartOrderService.*(..))")
    public void logBeforeCartOrderService(JoinPoint joinPoint) {
        logMethodExecution(joinPoint);
    }

    // Log SaveForLaterService methods
    @Before("execution(* com.genspark.cart_service.services.SaveForLaterService.*(..))")
    public void logBeforeSaveForLaterService(JoinPoint joinPoint) {
        logMethodExecution(joinPoint);
    }

    // Log WishListService methods
    @Before("execution(* com.genspark.cart_service.services.WishListService.*(..))")
    public void logBeforeWishListService(JoinPoint joinPoint) {
        logMethodExecution(joinPoint);
    }

    // After it finished, it will log the result of the method
    @AfterReturning(pointcut = "execution(* com.genspark.cart_service.services.*Service.*(..))", returning = "result")
    public void logAfterService(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsString = IntStream.range(0, args.length)
                .mapToObj(i -> {
                    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                    Parameter parameter = method.getParameters()[i];
                    return parameter.getName() + "=" + args[i];
                })
                .collect(Collectors.joining(", "));
        log.info("Method executed: " + methodName + ", Arguments: " + argsString + ", Return: " + result);
        if (isEmpty(result)) {
            log.info("No result returned for method: " + methodName + ", Arguments: " + argsString);
        }
    }

    // Helper method to check if the result is empty
    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Arrays.asList((Object[]) obj).isEmpty();
        }
        if (obj instanceof Cart) {
            return ((Cart) obj).getId().isEmpty();
        }
        return false;
    }

    // Helper method to log method execution
    private void logMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);
        log.info("Executing method: " + methodName + ", Arguments: " + argsString);
    }
}
