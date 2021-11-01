import hibernate_test1.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test4 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();


        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();

//            Employee emp = session.get(Employee.class, 1);
//            session.delete(emp);

            session.createQuery("delete from Employee where name = 'Elena'").executeUpdate();

            session.getTransaction().commit();

        }

    }
}
