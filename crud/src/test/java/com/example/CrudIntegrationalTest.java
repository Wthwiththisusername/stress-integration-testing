package com.example;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrudIntegrationalTest {
    private Crud crud;
    private Connection connection;
    private Database database;

    @Before
    public void setUp() throws SQLException {
        crud = new Crud();

        database = new Database();
        connection = database.getConnection();
    }
    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void integrtionalTestCreateUser() throws SQLException {
        User user = new User("packman", 22, "haha@gmail.com");
        crud.createUser(user);
        assertTrue(crud.readById(user.getId()));
    }

    @Test
    public void integrationalTestDeleteById() throws SQLException {
        User user = new User("packman", 22, "haha@gmail.com");
        crud.createUser(user);
        crud.deleteById(user.getId());
        assertFalse(crud.readById(user.getId()));
    }

    @Test
    public void integrationalTestReadAll() throws SQLException {
        User user1 = new User("packman", 22, "haha@gmail.com");
        crud.createUser(user1);
        User user2 = new User("mario", 21, "oink@gmail.com");
        crud.createUser(user2);
        User user3 = new User("vanila", 13, "ice@gmail.com");
        crud.createUser(user3);
        crud.readAll();
        int numOfUsers = counter();
        assertEquals(3, numOfUsers);
    }
    private int counter() throws SQLException {
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(USER_ID) FROM USER");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    @Test
    public void integrationalTestReadById() throws SQLException {
        User user = new User("packman", 22, "haha@gmail.com");
        crud.createUser(user);

        boolean isFound = crud.readById(user.getId());

        assertTrue(isFound);
    }
    @Test
    public void integrationalTestReadByIdNoUser() throws SQLException {
        boolean isFound = crud.readById(-1);
        assertFalse(isFound);
    }

    @Test
    public void integrationalTestUpdateById() throws SQLException {
        User user = new User("packman", 22, "haha@gmail.com");
        crud.createUser(user);
        User retrievedUser = getUserById(user.getId());
        assertNotNull(retrievedUser);
        User updatedUser = new User(user.getId(), "packman", 24, "hahaha@gmail.com");
        crud.updateById(updatedUser);
        
        retrievedUser = getUserById(user.getId());
        assertNotNull(retrievedUser); 
        assertEquals(updatedUser.getNickname(), retrievedUser.getNickname());
        assertEquals(updatedUser.getAge(), retrievedUser.getAge());
        assertEquals(updatedUser.getAddress(), retrievedUser.getAddress());
    }
    private User getUserById(int id) throws SQLException {
        User user = null;
        try(Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(Query.readByIdQuery(id))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    user = new User(
                    resultSet.getInt("USER_ID"),
                    resultSet.getString("USER_NICKNAME"),
                    resultSet.getInt("USER_AGE"),
                    resultSet.getString("USER_EMAIL")
                    );
                }
            }
        }
        return user;
        
    }
}
