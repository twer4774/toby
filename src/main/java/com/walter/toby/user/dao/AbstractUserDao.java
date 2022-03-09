package com.walter.toby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 템플릿 메소드 패턴때 이용된 추상 클래스 => 전략 패턴으로 보완
 */
public abstract class AbstractUserDao {

    // ...

    abstract protected PreparedStatement makeStatement(Connection e) throws SQLException;
}
