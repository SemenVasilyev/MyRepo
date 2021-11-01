package org.example.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
public class ExceptionHandingAspect {
    @Before("org.example.aspects.MyPointcuts.allAddMethods()")
    public void beforeAddExceptionHandingAdvice(){
        System.out.println("beforeGetExceptionHandingAdvice: ловим/обробатываем исключения при попытке получить книгу/журнал");
        System.out.println("--------------------------------------");
    }
}
