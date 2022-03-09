package com.walter.toby.user.dao;

// USerDao의 생성 책임을 맡은 팩토리 클래스


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 어플리케이션 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {

    // 팩토리 메소드 : UerDao 타입의 오브젝트를 어떻게 만들고 어떻게 준비시킬지 결정한다.
    @Bean // 오브젝트 생성을 담당하는 IoC 메소드라는 표시
    public UserDao userDao(){
        // 생성자 주입 방식
//        return new UserDao(connectionMaker());

        // setter 주입방식 -> 생성자 주입방식을 이용하므로 DaoFactory에서도 코드 수정 필요
        UserDao dao = new UserDao();
        dao.setConnectionMaker(connectionMaker());
        return dao;
    }

    /*public AccountDao accountDao(){
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao(){
        return new MessageDao(connectionMaker());
    }*/

    @Bean
    public ConnectionMaker connectionMaker(){
        return new DConnectionMaker();
    }
}
