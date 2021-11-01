import hibernate_test1.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

//        try(Session session = sessionFactory.openSession()) {
//
//            Employee emp = new Employee("Elena", "Vasilyev", "HR", 400);
//
//            session.beginTransaction();
//            session.save(emp);
//            session.getTransaction().commit();
//
//        }

        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            Employee employee = session.get(Employee.class, 8);
            session.getTransaction().commit();
            System.out.println(employee);

        }

    }
}
