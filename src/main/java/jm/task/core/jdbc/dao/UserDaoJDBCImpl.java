package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = DBConnection.getDBConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String createTable = """
                    CREATE TABLE IF NOT EXISTS users 
                    (id INT PRIMARY KEY AUTO_INCREMENT, 
                    name VARCHAR(30),
                    lastName VARCHAR(30), 
                    age INT(3))
                    """;

            statement.executeUpdate(createTable);
            System.out.println("Table users is created");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            System.out.println("Table users is drop");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlRequest = "INSERT users(name, lastName, age) VALUE(?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.executeUpdate();
            connection.commit();

            System.out.println("User named - " + name + " added to the database");
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String sqlRequest = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
            System.out.println("User with id = " + id + " has been deleted");
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        String sqlRequest = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet set = statement.executeQuery(sqlRequest)) {

            while (set.next()) {
                User user = new User(set.getString("name"), set.getString("lastName"), set.getByte("age"));
                user.setId(set.getLong("id"));
                users.add(user);
            }

            connection.commit();
            System.out.println("Got all users");
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE users");
            connection.commit();
            System.out.println("Table is clean");
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }
}
