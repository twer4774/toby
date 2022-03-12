package com.walter.toby.user.dao;

import com.walter.toby.ServiceAbstract.Level;
import com.walter.toby.ServiceAbstract.User;
import com.walter.toby.ServiceAbstract.UserDaoJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/application.properties")
class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDaoJdbc dao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("leee", "이길원", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
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


        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);

    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }

    @Test
    public void update() throws SQLException {

        dao.deleteAll();

        dao.add(user1); // 수정할 사용자
        dao.add(user2); // 수정하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);

        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);

    }


}