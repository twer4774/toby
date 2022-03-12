package com.walter.toby.user.domain;

import com.walter.toby.ServiceAbstract.Level;

public class User {

    String id;
    String name;
    String password;

    public User(String id, String name, String password, Level basic, int i, int i1) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
