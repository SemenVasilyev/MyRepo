package com.example.demo;

/**
 * @author Semen V
 * @created 30|11|2021
 */
public class Task3 {
    public static void printFizzBazz(){
        for(int i = 1; i <= 100; i++){
            if(i % 3 == 0  && i % 5 == 0){
                System.out.println("FizzBuzz");
            }else if(i % 3 == 0){
                System.out.println("Fizz");
            }else if(i % 5 == 0){
                System.out.println("Buzz");
            }else{
                System.out.println(i);
            }
        }
    }
}
