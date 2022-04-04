package com.walter.toby.ServiceAbstract.service;

import com.walter.toby.Aop.UserServiceImpl;
import com.walter.toby.Aop.UserServiceTx;
import com.walter.toby.ServiceAbstract.Level;
import com.walter.toby.ServiceAbstract.User;
import com.walter.toby.ServiceAbstract.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    DataSource dataSource;

    UserDao userDao;

    List<User> users;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;


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
    void upgradeLevels() throws Exception {


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

    // 강제 예외 발생을 통한 테스트 - 예외 발생 시 작업 취소 여부 테스트
    @Test
    public void upgradeAllOrNothing() throws SQLException, ClassNotFoundException {
        UserService testUserService = new UserService.TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao); // 수동 DI
//        testUserService.setDataSource(this.dataSource);
//        testUserService.setTransactionManager(transactionManager); //수동 DI

        // 트랜잭션 기능 분리
        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService((com.walter.toby.Aop.UserService) testUserService);

        userDao.deleteAll();

        for(User user : users){
           userDao.add(user);
        }

        try{
            // 트랜잭션 기능을 분리한 오브젝트를 통해 예외 발생용 TestUSerService가 호출되게 해야 한다.
            txUserService.upgradeLevels();
            
            // 업그레이드 작업 중 예외를 발생시켜야 한다. 정상 종료가 되면 실패
            testUserService.upgradeLevels();
            fail("TestUSerServiceException expected");
        } catch (UserService.TestUserService.TestUserServiceException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkLevelUpgrade(users.get(1), false); // 예외가 발생하기 전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 확인

    }

}