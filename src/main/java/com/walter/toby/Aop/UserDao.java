package com.walter.toby.Aop;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    public void add(User user) throws SQLException;

    public User get(String id) throws SQLException, ClassNotFoundException;

    public void deleteAll() throws SQLException;

    public int getCount() throws SQLException, ClassNotFoundException;

    public List<User> getAll();

    public void update(User user);

}
