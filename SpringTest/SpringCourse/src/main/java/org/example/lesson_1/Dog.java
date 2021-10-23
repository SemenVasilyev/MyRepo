package org.example.lesson_1;

import org.springframework.stereotype.Component;

@Component
public class Dog implements Pet {

    @Override
    public void say(){
        System.out.println("Bow-bow");
    }

    public void init(){
        System.out.println("Class Dog: init method");
    }

    public void destroy(){
        System.out.println("Class Dog: destroy method");
    }

}
