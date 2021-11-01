package org.example;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class University {
    private List<Student> students = new ArrayList<>();

    public void addStudents(){
        Student st1 = new Student("Semen", 4, 7.7);
        Student st2 = new Student("Ivan", 3, 8.3);
        Student st3 = new Student("Elena", 2, 9.6);
        students.add(st1);
        students.add(st2);
        students.add(st3);
    }

    public List<Student> getStudents(){
        System.out.println("Начало работы метода getStudents");
       // System.out.println(students.get(3));
        System.out.println("Information from method getStudents:");
        System.out.println(students);
        return students;
    }
}
