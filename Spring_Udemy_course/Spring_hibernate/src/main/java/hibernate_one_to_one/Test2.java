package hibernate_one_to_one;

import hibernate_one_to_one.entity.Detail;
import hibernate_one_to_one.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test2 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();


//        try(Session session = sessionFactory.openSession()) {
//            Employee employee = new Employee("Elena", "Ivanova", "HR", 700);
//            Detail detail = new Detail("London", "54321", "lenka7@yandex.ru");
//
//            employee.setEmpDetail(detail);
//            detail.setEmployee(employee);
//
//
//            session.beginTransaction();
//            session.save(detail);
//
//
//            session.getTransaction().commit();
//        }

        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            Detail detail = session.get(Detail.class, 4);

            System.out.println(detail);

            session.delete(detail);

            session.getTransaction().commit();
        }



    }
}
