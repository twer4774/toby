package com.walter.toby.ServiceAbstract.service;

import com.walter.toby.ServiceAbstract.Level;
import com.walter.toby.ServiceAbstract.User;
import com.walter.toby.ServiceAbstract.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    UserDao userDao;

    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                new User("erwins", "박범진", "p3", Level.SILVER, 60, 29),
                new User("madnitel1", "박범진", "p4", Level.SILVER, 60, 30),
                new User("green", "박범진", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    void upgradeLevels() throws SQLException, ClassNotFoundException {

        userDao.deleteAll();

        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);


    }

    private void checkLevelUpgrade(User user, boolean upgraded) throws SQLException, ClassNotFoundException {
        User userUpdate = userDao.get(user.getId());
        if(upgraded){
            // 업그레이드가 일어났는지 확인
            assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        } else {
            // 업그레이드가 일어나지 않았는지 확인
            assertEquals(userUpdate.getLevel(), user.getLevel());
        }
    }


    // 처음 회원가입 시 BASIC 레벨
    @Test
    void add() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();

        User userWithLevel = users.get(4); // 골드레벨
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null); //레벨이 비어있는 사용자. 로직에 따라 등록 중에 BASIC ㄹ벨도 설정돼야 한다.

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        // DB에서 결과를 가져와 확인
        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
        assertEquals(userWithoutLevelRead.getLevel(), userWithoutLevel.getLevel());
    }


}