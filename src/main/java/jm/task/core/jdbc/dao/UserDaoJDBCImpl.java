package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getDBConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE user (\n" +
                    "id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "name VARCHAR(30),\n" +
                    "lastName VARCHAR(30),\n" +
                    "age INT(3))");

            System.out.println("Table user is created");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getDBConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DROP TABLE user");
            System.out.println("Table user is drop");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlRequest = "INSERT user(name, lastName, age) VALUE(?, ?, ?)";

        try (Connection connection = Util.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(sqlRequest)) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.executeUpdate();

            System.out.println("User named - " + name + " added to the database");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getDBConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM user\n" +
                    "WHERE id = " + id);
            System.out.println("User with id = " + id + " has been deleted");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        String sqlRequest = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet set = statement.executeQuery(sqlRequest)) {

            while (set.next()) {
                Long id = set.getLong("id");
                String name = set.getString("name");
                String lastName = set.getString("lastName");
                byte age = set.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);

                users.add(user);
            }

            System.out.println("Got all users");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getDBConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("TRUNCATE user");
            System.out.println("Table is clean");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
