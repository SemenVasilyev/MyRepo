package hibernate_one_to_many_by;


import hibernate_one_to_many_by.entity.Department;
import hibernate_one_to_many_by.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildSessionFactory();


//        try(Session session = sessionFactory.openSession()) {
//
//            Department dep = new Department("IT",  300,1200);
//            Employee emp1 = new Employee("Semen", "Vasilev", 800);
//            Employee emp2 = new Employee("Elena", "Smirnova", 1000);
//            Employee emp3 = new Employee("Olga", "Petrova", 1000);
//            Employee emp4 = new Employee("Ivan", "Pupa", 1000);
//
//            dep.addEmployeeToDepartment(emp1);
//            dep.addEmployeeToDepartment(emp2);
//            dep.addEmployeeToDepartment(emp3);
//            dep.addEmployeeToDepartment(emp4);
//
//            session.beginTransaction();
//            session.save(dep);
//
//            session.getTransaction().commit();
//        }

        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            Department department = session.get(Department.class, 4);
            System.out.println(department);
            System.out.println(department.getEmps());

            session.getTransaction().commit();
        }

//        try(Session session = sessionFactory.openSession()) {
//
//
//            session.beginTransaction();
//            Employee employee = session.get(Employee.class, 1);
//            session.delete(employee);
//
//            session.getTransaction().commit();
//        }


    }
}
