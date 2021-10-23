package main.model;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class MySessionFactory {

    @Getter
    public static final SessionFactory INSTANCE = createSessionFactory();

    private static SessionFactory createSessionFactory() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        StandardServiceRegistry registry = cfg.getStandardServiceRegistryBuilder().
                applySetting("hibernate.connection.url", "jdbc:mysql://" + MyProperties.getHost() + ":"+
                                                                MyProperties.getPort() + "/" + MyProperties.getBdName() + "?useSSL=false")
                .applySetting("hibernate.connection.username", MyProperties.getUsername())
                .applySetting("hibernate.connection.password", MyProperties.getPassword()).build();;
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        return sessionFactory;
    }

    public static void addEntityToDB(Object entity) {
        try (Session session = INSTANCE.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        }
    }

    public static void updateEntityToDB(Object entity) {
        try (Session session = INSTANCE.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }
    }

    public static void close() {
        INSTANCE.close();
    }

    private MySessionFactory() {
    }
}
