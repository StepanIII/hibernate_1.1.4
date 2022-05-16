package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao daoJdbc = new UserDaoJDBCImpl();

    public void createUsersTable() {
        daoJdbc.createUsersTable();
    }

    public void dropUsersTable() {
        daoJdbc.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        daoJdbc.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        daoJdbc.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return daoJdbc.getAllUsers();
    }

    public void cleanUsersTable() {
        daoJdbc.cleanUsersTable();
    }
}
