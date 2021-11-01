package hibernate_one_to_one;

import hibernate_one_to_one.entity.Detail;
import hibernate_one_to_one.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();


//        try(Session session = sessionFactory.openSession()) {
//            Employee employee = new Employee("Semen", "Vasilev", "IT", 500);
//            Detail detail = new Detail("KnA", "12345", "vsemen7@yandex.ru");
//
//            employee.setEmpDetail(detail);
//
//            session.beginTransaction();
//            session.save(employee);
//
//
//            session.getTransaction().commit();
//        }

//        try(Session session = sessionFactory.openSession()) {
//            Employee employee = new Employee("Oleg", "Smirnov", "Sales", 700);
//            Detail detail = new Detail("Moscow", "54321", "olejka7@yandex.ru");
//
//            employee.setEmpDetail(detail);
//
//            session.beginTransaction();
//            session.save(employee);
//
//
//            session.getTransaction().commit();
//        }

        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            Employee emp = session.get(Employee.class, 2);
            session.delete(emp);

            session.getTransaction().commit();
        }


    }
}
