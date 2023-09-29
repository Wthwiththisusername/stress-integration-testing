package com.example;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class CrudSmokeTest {
    private Crud crud;

    @Before
    public void setUp() {
        crud = new Crud();
    }
    @Test
    public void smokeTestCreateUser() {
        User user = new User("Capybara", 19, "capybara@gmail.com");
        try {
            crud.createUser(user);
        } catch (SQLException e) {
            fail("Creating denied through exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteById() {
        int id = 5;
        try {
            crud.deleteById(id);
        } catch (SQLException e) {
            fail("Deletion denied through exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadAll() {
        try {
            crud.readAll();
        } catch (SQLException e) {
            fail("Reading all denied through the exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadById() {
        int id = 5;
        try {
            boolean isFound = crud.readById(id);
            assertTrue("User with ID 5 is not found", isFound);
        } catch (SQLException e) {
            fail("Reading by ID denied through exception: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateById() {
        User user = new User(5, "Winx", 28, "wakingupat8@gmail.com");
        try {
            crud.updateById(user);
        } catch (SQLException e) {
            fail("Updateing denied through exception: " + e.getMessage());
        }
    }
}
