package org.example.lesson_1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test6 {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(MyConfig.class);
//
//        Person person = context.getBean("personBean", Person.class);
//        person.callYourPet();

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig2.class);

//        Pet cat1 = context.getBean("catBean", Pet.class);
//        Pet cat2 = context.getBean("catBean", Pet.class);

        Person2 person = context.getBean("personBean", Person2.class);
        System.out.println(person.getName() + " " + person.getAge());

        person.callYourPet();


        context.close();
    }
}
