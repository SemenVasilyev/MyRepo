package org.example.lesson_1;

import org.springframework.stereotype.Component;

@Component("catBean")
//@Component
public class Cat implements Pet{

    @Override
    public void say() {
        System.out.println("Meow-meow");
    }
}
