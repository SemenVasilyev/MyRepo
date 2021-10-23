package org.example.lesson_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("personBean")
public class Person {
    @Autowired
    @Qualifier("dog")
    private Pet pet;
    private String name;
    private int age;

//    //@Autowired
//    public Person(@Qualifier("dog")Pet pet) {
//        this.pet = pet;
//    }

    public Person() {
    }

//    @Autowired
//    @Qualifier("dog")
//    public void setPet(Pet pet) {
//        this.pet = pet;
//    }

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

    public void callYourPet(){
        System.out.println("Hello, my lovely pet!");
        pet.say();
    }
}
