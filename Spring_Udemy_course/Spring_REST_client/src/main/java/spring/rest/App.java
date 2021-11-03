package spring.rest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.rest.configuration.MyConfig;
import spring.rest.entity.Employee;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        Communication communication = context.getBean("communication", Communication.class);
//        List<Employee> allEmployees = communication.getAllEmployees();
//        Employee empById = communication.getEmployee(1);
//        Employee employee = new Employee("Sveta", "Socolovs", "IT", 1500);
//        employee.setId(7);
//
//        communication.saveEmployee(employee);
        communication.deleteEmployee(7);

    //    System.out.println(employee);

    }
}
