package hibernate_one_to_many_uni;


import hibernate_one_to_many_uni.entity.Department;
import hibernate_one_to_many_uni.entity.Employee;
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

//
//        try(Session session = sessionFactory.openSession()) {
//
//            Department dep = new Department("HR",  500,1500);
//            Employee emp1 = new Employee("Ivan", "Vasilev", 800);
//            Employee emp2 = new Employee("Olga", "Smirnova", 1000);
//
//            dep.addEmployeeToDepartment(emp1);
//            dep.addEmployeeToDepartment(emp2);
//
//            session.beginTransaction();
//            session.save(dep);
//
//            session.getTransaction().commit();
//        }

//        try(Session session = sessionFactory.openSession()) {
//
//            session.beginTransaction();
//            Department department = session.get(Department.class, 2);
//            System.out.println(department);
//            System.out.println(department.getEmps());
//
//            session.getTransaction().commit();
//        }
//
        try(Session session = sessionFactory.openSession()) {


            session.beginTransaction();
            Department department = session.get(Department.class, 2);
            session.delete(department);

            session.getTransaction().commit();
        }


    }
}
