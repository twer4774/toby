package com.walter.toby;

import com.walter.toby.user.dao.ConnectionMaker;
import com.walter.toby.user.dao.DConnectionMaker;
import com.walter.toby.user.dao.DaoFactory;
import com.walter.toby.user.dao.UserDao;
import com.walter.toby.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        /*
        // UerDao가 사용할 ConnectionoMaker 구현 클래스를 결정하고 오브젝트를 만든다.
        ConnectionMaker connectionMaker = new DConnectionMaker();

        // 1. UserDao 생성
        // 2. 사용할 ConnectionMaker 타입의 오브젝트 제공
        UserDao dao = new UserDao(connectionMaker);
        */

        // 위의 동작을 Factory를 이용하여 오브젝트 생성에 대한 책임을 분리 한다.
        UserDao dao = new DaoFactory().userDao();

//        User user = new User();
//        user.setId("walter");
//        user.setName("wonik");
//        user.setPassword("hihi");
//
//        dao.add(user);
//
//        System.out.println(user.getId() + " 등록 성공");
//        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getId() + " 조회 성공");


        // 어플리케이션 컨텍스트 적용 : @Configuration 인식
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao2 = context.getBean("userDao", UserDao.class); // userDao는 Bean의 이름. 이름을 따로 두지 않았으므로 메소드 이름이 Bean의 이름이 된다.
        User user = new User();

        user.setId("walter");
        user.setName("wonik");
        user.setPassword("hihi");

        dao2.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao2.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
