package com.walter.toby.ServiceAbstract.service;

import com.walter.toby.ServiceAbstract.Level;
import com.walter.toby.ServiceAbstract.User;
import com.walter.toby.ServiceAbstract.UserDao;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static com.walter.toby.ServiceAbstract.Level.BASIC;

public class UserService {

    //트랜잭션 동기화를 위한 DI
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 트랜잭션 매니저를 빈으로 분리시킴
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    // 사용자 레벨 업그레이드
    /*public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users) {
            Boolean changed = null; // 레벨 변화가 있는지 확인하는 플래그
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if (user.getLevel() == Level.GOLD) { // 골드레벨은 변경이 일어나지 않는다.
                changed = false;
            } else {
                changed = false;
            }

            // 레벨의 변경이 있는 경우에만 update 호출
            if(changed) {
                userDao.update(user);
            }
        }
    }*/

    //리팩토링
    /*public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }
    }*/

    /*// 트랜잭션 동기화 방식 적용
    public void upgradeLevels() throws Exception{
        TransactionSynchronizationManager.initSynchronization(); // 트랜잭션 동기화 관리자를 이용해 동기화 작업 초기화
        // DB 커넥션을 생성하고, 트랜잭션 시작
        Connection c = DataSourceUtils.getConnection(dataSource); // DB 커넥션 생성과 동기화를 함께 해주는 유틸리티 메소드
        c.setAutoCommit(false);

        try{
            List<User> users = userDao.getAll();
            for (User user : users){
                if(canUpgradeLevel(user)){
                    upgradeLevel(user);
                }
            }
            c.commit(); // 정상적으로 작업을 마치면 트랜잭션 커밋
        } catch (Exception e){
            c.rollback();
            throw e;
        } finally{
            // 동기화 작업 종료 및 정리
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }*/


    // 스프링의 트랜잭션 추상화 API를 적용한 upgradeLevels()
    public void upgradeLevels(){
        // JDBC 트랜잭션 추상 오브젝트 생성
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            List<User> users = userDao.getAll();
            for(User user: users){
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status); // 트랜잭션 커밋
        } catch (RuntimeException e){
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    private boolean canUpgradeLevel(User user){
        Level currentLevel = user.getLevel();
        switch(currentLevel){
            case BASIC :
                return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD : return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }
    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    // JavaMail을 이용한 메일 발송 메소드
    private void sendUpgradeEmail(User user) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.ksug.org");
        Session s = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(s);
        try {

            message.setFrom(new InternetAddress("useradmin@ksung.org"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레디으 되었습니다.");

            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }

    }

    // 사용자 신규등록 로직을 담은 add
    public void add(User user) throws SQLException {
        if(user.getLevel() == null){
            user.setLevel(BASIC);
        }
        userDao.add(user);
    }


    // UserService의 테스트 대용 대역 클래스
    static class TestUserService extends UserService{
        private String id;

        // 예외를 발생시킬 User 오브젝트의 id를 지정할 수 있게 만든다.
        TestUserService(String id){
            this.id=id;
        }

        @Override
        protected void upgradeLevel(User user){
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        static class TestUserServiceException extends RuntimeException{}
    }
}


