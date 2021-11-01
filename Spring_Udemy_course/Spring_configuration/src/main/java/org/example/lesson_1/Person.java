package org.example.lesson_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("personBean")
public class Person {
    @Autowired
    @Qualifier("catBean")
    private Pet pet;
//    @Value("Semen")
//    @Value("${person.name}")
    private String name;
//    @Value("31")
//    @Value("${person.age}")
    private int age;

//    //@Autowired
//    public Person(@Qualifier("dog")Pet pet) {
//        this.pet = pet;
//    }

    public Person() { System.out.println("Person bean is created");
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
