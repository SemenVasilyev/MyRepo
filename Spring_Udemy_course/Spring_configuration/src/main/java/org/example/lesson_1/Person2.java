package org.example.lesson_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class Person2 {

    private Pet pet;
    @Value("${person.name}")
    private String name;
    @Value("${person.age}")
    private int age;


    public Person2(Pet pet) {
        System.out.println("Person bean is created");
        this.pet = pet;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void callYourPet() {
        System.out.println("Hello, my lovely pet!");
        pet.say();
    }
}
