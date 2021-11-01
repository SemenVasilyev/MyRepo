package org.example;

import org.springframework.stereotype.Component;

@Component
public class UniLibrary extends AbstractLibrary{

   // @Override
    public void getBook(){
        System.out.println("Мы берем книгу из UniLibrary ");
        System.out.println("--------------------------------------");
    }

    public String returnBook(){
        int i = 10/0;
        System.out.println("Мы возврощаем книгу в UniLibrary");
        //System.out.println("--------------------------------------");
        return "Война и Мир";
    }

    public void getMagazine(){
        System.out.println("Мы берем журнал из UniLibrary");
        System.out.println("--------------------------------------");
    }

    public void returnMagazine(){
        System.out.println("Мы возврощаем журнал в UniLibrary");
        System.out.println("--------------------------------------");
    }

    public void addBook(String personName, Book book){
        System.out.println("Мы добавляем книгу в UniLibrary");
        System.out.println("--------------------------------------");
    }

    public void addMagazine(){
        System.out.println("Мы добавляем журнал в UniLibrary");
        System.out.println("--------------------------------------");
    }
}
