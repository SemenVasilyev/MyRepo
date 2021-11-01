package org.example.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class MyPointcuts {
//    @Pointcut("execution(* add*(..))")
    @Pointcut("execution(* abc*(..))")
    public void allAddMethods(){}


}
