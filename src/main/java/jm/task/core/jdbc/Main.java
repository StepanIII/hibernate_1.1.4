package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    private static final UserServiceImpl service = new UserServiceImpl();

    public static void main(String[] args) {
        service.createUsersTable();

        service.saveUser("Stepan", "Cupriyanovich", (byte) 23);
        service.saveUser("Elena", "Ivanova", (byte) 25);
        service.saveUser("Tom", "Cruise", (byte) 59);
        service.saveUser("Kate", "Brown", (byte) 9);

        System.out.println(service.getAllUsers());

        service.removeUserById(2);

        service.cleanUsersTable();

        service.dropUsersTable();
    }
}
