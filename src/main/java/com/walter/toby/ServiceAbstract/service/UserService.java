package com.walter.toby.ServiceAbstract.service;

import com.walter.toby.ServiceAbstract.Level;
import com.walter.toby.ServiceAbstract.User;
import com.walter.toby.ServiceAbstract.UserDao;

import java.sql.SQLException;
import java.util.List;

import static com.walter.toby.ServiceAbstract.Level.BASIC;

public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
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
    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
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
    private void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
    }

    // 사용자 신규등록 로직을 담은 add
    public void add(User user) throws SQLException {
        if(user.getLevel() == null){
            user.setLevel(BASIC);
        }
        userDao.add(user);
    }
}
