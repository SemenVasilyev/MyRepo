package org.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Test3 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        UniLibrary library = context.getBean("uniLibrary", UniLibrary.class);
        String bookName = library.returnBook();
        System.out.println("в библиотеку вернули книгу " + bookName);

        context.close();
    }
}
