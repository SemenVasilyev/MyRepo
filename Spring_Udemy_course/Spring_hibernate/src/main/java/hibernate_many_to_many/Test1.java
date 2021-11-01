package hibernate_many_to_many;


import hibernate_many_to_many.entity.Child;
import hibernate_many_to_many.entity.Section;
import hibernate_one_to_many_by.entity.Department;
import hibernate_one_to_many_by.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Child.class)
                .addAnnotatedClass(Section.class)
                .buildSessionFactory();


//        try(Session session = sessionFactory.openSession()) {
//
//            Section section1 = new Section("Football");
//            Child child1 = new Child("Semen", 5);
//            Child child2 = new Child("Oleg", 3);
//            Child child3 = new Child("Lena", 4);
//
//            section1.addChildToSection(child1);
//            section1.addChildToSection(child2);
//            section1.addChildToSection(child3);
//
//            session.beginTransaction();
//
////            session.save(section1);
//            session.persist(section1); //если нет каскадного сохранения то используй persist
//
//            session.getTransaction().commit();
//        }


//        try(Session session = sessionFactory.openSession()) {
//
//            Section section1 = new Section("basketball");
//            Section section2 = new Section("chess");
//            Section section3 = new Section("math");
//            Child child1 = new Child("Igor", 5);
//
//
//            child1.addSectionToChild(section1);
//            child1.addSectionToChild(section2);
//            child1.addSectionToChild(section3);
//
//            session.beginTransaction();
//
//            session.save(child1);
//
//            session.getTransaction().commit();
//        }


//        try(Session session = sessionFactory.openSession()) {
//
//            session.beginTransaction();
//
//            Section section = session.get(Section.class,1);
//            System.out.println(section);
//            System.out.println(section.getChildren());
//
//            session.getTransaction().commit();
//        }


//        try(Session session = sessionFactory.openSession()) {
//
//            session.beginTransaction();
//
//            Section section = session.get(Section.class,5);
//            session.delete(section);
//
//            session.getTransaction().commit();
//        }


        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            Child child = session.get(Child.class,4);
            session.delete(child);

            session.getTransaction().commit();
        }

    }
}
