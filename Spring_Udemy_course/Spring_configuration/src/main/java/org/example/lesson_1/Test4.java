package org.example.lesson_1;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test4 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext2.xml");
                //new ClassPathXmlApplicationContext("applicationContext2.xml", "applicationContext.xml");

        Pet pet = context.getBean("myPet", Dog.class);
        pet.say();

        context.close();
    }
}
