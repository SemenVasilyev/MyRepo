package org.example.lesson_1;

import org.springframework.stereotype.Component;

public class Cat2 implements Pet{

    public Cat2() {
        System.out.println("Cat bean is created");
    }

    @Override
    public void say() {
        System.out.println("Meow-meow");
    }
}
