package com.walter.toby.Aop;

import java.sql.SQLException;

public interface UserService {
    void add(User user) throws SQLException;
    void upgradeLevels();

}
