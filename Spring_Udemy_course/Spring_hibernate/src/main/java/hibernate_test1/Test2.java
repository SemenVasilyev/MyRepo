import hibernate_test1.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();


        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            List<Employee> emps = session.createQuery("from Employee where salary > '450'").getResultList();

            for (Employee em : emps) {
                System.out.println(em);
            }

            session.getTransaction().commit();

        }

    }
}
