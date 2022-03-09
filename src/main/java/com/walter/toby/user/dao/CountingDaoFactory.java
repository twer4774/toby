package com.walter.toby.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// CountingConnectionMaker 의존관계가 추가된 DI 설정용 클래스
@Configuration
public class CountingDaoFactory {


    @Bean
    public UserDao userDao() {
        // 생성자 주입방식
//        return new UserDao(connectionMaker());

        // setter 주입방식
        UserDao dao = new UserDao();
        dao.setConnectionMaker(connectionMaker());
        return dao;
    }

    @Bean
    public ConnectionMaker connectionMaker(){
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }
}
