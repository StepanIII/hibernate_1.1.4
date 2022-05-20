package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DatabaseConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = DatabaseConnection.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createSQLQuery("CREATE TABLE IF NOT EXISTS users" +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(30), lastName VARCHAR(30), age INT(3))");
            query.executeUpdate();
            transaction.commit();
            System.out.println("User table is create.");
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createSQLQuery("DROP TABLE IF EXISTS users");
            query.executeUpdate();
            transaction.commit();
            System.out.println("User table is drop.");
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("Added user named - " + name + " to the database");
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("DELETE FROM User WHERE id = " + id);
            query.executeUpdate();
            transaction.commit();
            System.out.println("Delete user with id - " + id);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM User");
        List<User> users = query.getResultList();
        transaction.commit();
        System.out.println("Got all users");
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("DELETE FROM User");
            query.executeUpdate();
            transaction.commit();
            System.out.println("Clean table users");
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
