package com.walter.toby.user.dao;

import com.walter.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/application.properties")
class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDao dao;

    @BeforeEach
    public void setUp() {
        this.dao = context.getBean("userDao", UserDao.class);
    }

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {


        dao.deleteAll();
        assertEquals(dao.getCount(), 0);


        User user = new User();
        user.setId("gyumme");
        user.setName("박성철");
        user.setPassword("springno1");

        dao.add(user);
        assertEquals(dao.getCount(), 1);

        User user2 = dao.get(user.getId());

        assertEquals(user2.getName(), user.getName());
        assertEquals(user2.getPassword(), user.getPassword());

    }


}