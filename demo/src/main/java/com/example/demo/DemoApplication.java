package com.example.demo;

import com.opencsv.exceptions.CsvException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws IOException, CsvException {
       // System.out.println(Integer.parseInt("123456789123"));

//        System.out.println(Task1.replase("Как очистить или очистить", "ть", "па"));
//        System.out.println(Task2.stringToInt("123456789"));
//        Task3.printFizzBazz();
        Task4.toDo();
    }

}
