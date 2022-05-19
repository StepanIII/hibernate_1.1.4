package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection CONNECTION = DatabaseConnection.getDatabaseConnection();

    private final String SQL_CREATE_TABLE = """
                    CREATE TABLE IF NOT EXISTS users 
                    (id INT PRIMARY KEY AUTO_INCREMENT, 
                    name VARCHAR(30),
                    lastName VARCHAR(30), 
                    age INT(3))
                    """;
    private final String SQL_DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    private final String SQL_SAVE_USER = "INSERT users(name, lastName, age) VALUE(?, ?, ?)";
    private final String SQL_REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    private final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    private final String SQL_CLEAN_USERS_TABLE = "TRUNCATE users";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            CONNECTION.setAutoCommit(true);

            statement.executeUpdate(SQL_CREATE_TABLE);
            System.out.println("Table users is created");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            CONNECTION.setAutoCommit(true);

            statement.executeUpdate(SQL_DROP_USERS_TABLE);
            System.out.println("Table users is drop");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(SQL_SAVE_USER)) {
            CONNECTION.setAutoCommit(false);

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.executeUpdate();
            CONNECTION.commit();

            System.out.println("User named - " + name + " added to the database");
        } catch (SQLException throwables) {
            try {
                CONNECTION.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(SQL_REMOVE_USER_BY_ID)) {
            CONNECTION.setAutoCommit(false);

            statement.setLong(1, id);
            statement.executeUpdate();

            CONNECTION.commit();
            System.out.println("User with id = " + id + " has been deleted");
        } catch (SQLException throwables) {
            try {
                CONNECTION.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = CONNECTION.createStatement();
             ResultSet set = statement.executeQuery(SQL_GET_ALL_USERS)) {
            CONNECTION.setAutoCommit(false);

            while (set.next()) {
                User user = new User(set.getString("name"), set.getString("lastName"), set.getByte("age"));
                user.setId(set.getLong("id"));
                users.add(user);
            }

            CONNECTION.commit();
            System.out.println("Got all users");
        } catch (SQLException throwables) {
            try {
                CONNECTION.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            CONNECTION.setAutoCommit(false);

            statement.executeUpdate(SQL_CLEAN_USERS_TABLE);

            CONNECTION.commit();
            System.out.println("Table is clean");
        } catch (SQLException throwables) {
            try {
                CONNECTION.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }
}
