package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DatabaseConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = DatabaseConnection.getSessionFactory();

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users
            (id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(30),
            last_name VARCHAR(30),
            age INT(3))
            """;
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users";

    private static final String HQL_REMOVE_USER_BY_ID = "DELETE FROM User WHERE id = :userId";
    private static final String HQL_GET_ALL_USERS = "FROM User";
    private static final String HQL_CLEAN_TABLE = "DELETE FROM User";

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            Query query = session.createSQLQuery(SQL_CREATE_TABLE);
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("User table is create.");
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            Query query = session.createSQLQuery(SQL_DROP_TABLE);
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("User table is drop.");
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("Added user named - " + name + " to the database");
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            Query query = session.createQuery(HQL_REMOVE_USER_BY_ID);
            query.setParameter("userId", id);
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("Delete user with id - " + id);
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<User> users = new ArrayList<>();
        try {
            Query query = session.createQuery(HQL_GET_ALL_USERS);
            users = query.getResultList();
            session.getTransaction().commit();
            System.out.println("Got all users");
            return users;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            Query query = session.createQuery(HQL_CLEAN_TABLE);
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("Clean table users");
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
