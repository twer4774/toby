package com.walter.toby.user.dao;

import com.walter.toby.user.domain.User;

import java.sql.*;


public class UserDao {

    private ConnectionMaker connectionMaker; // 인터페이스를 통해 오브젝트에 접근하므로 구체적인 클래스 정보를 알 필요가 없다.

    // 생성자 주입방식
//    public UserDao(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }

    // setter 주입방식 -> 생성자 주입방식을 이용하므로 DaoFactory에서도 코드 수정 필요
    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    /*public void add(User user) throws ClassNotFoundException, SQLException{
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();

    }*/

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from user where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }


    /*public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }*/

    /*// 예외 발생 처리
    public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = connectionMaker.makeConnection();
            ps = c.prepareStatement("delete from users");
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if( ps != null ){
                // ps.close() 메소드에서도 SQLException이 발생할 수 있으므로 try-catch로 잡아준다.
                try{
                    ps.close();
                } catch (SQLException e){

                }
            }

            if(c != null){
                try{
                    c.close();
                } catch (SQLException e){

                }
            }
        }

    }*/

    // 변하는 부분을 메소드로 추출한 후의 deleteAll()
    /*public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = connectionMaker.makeConnection();
            ps = makeStatement(c);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if( ps != null ){
                // ps.close() 메소드에서도 SQLException이 발생할 수 있으므로 try-catch로 잡아준다.
                try{
                    ps.close();
                } catch (SQLException e){

                }
            }
            if(c != null){
                try{
                    c.close();
                } catch (SQLException e){

                }
            }
        }
    }
    private PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return ps;
    }*/


    /*------------------------------- 전략패턴 ---------------------------------------------*/
    //전략패턴 이용
    /*public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = connectionMaker.makeConnection();
            StatementStrategy strategy = new DeleteAllStatement(); // 전략패턴은 이렇게 고정되는 것이 있으면 안된다.
            ps = strategy.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if( ps != null ){
                // ps.close() 메소드에서도 SQLException이 발생할 수 있으므로 try-catch로 잡아준다.
                try{
                    ps.close();
                } catch (SQLException e){

                }
            }
            if(c != null){
                try{
                    c.close();
                } catch (SQLException e){

                }
            }
        }
    }*/

    /*// 클라이언트 책임을 담당할 deleteAll()
    public void deleteAll() throws SQLException, ClassNotFoundException {
        StatementStrategy st = new DeleteAllStatement(); // 선정한 전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(st); // 컨텍스트 호출. 전략 오브젝트 전달
    }*/



    /*// 전략 패턴 적용된 add
   public void add(User user) throws ClassNotFoundException, SQLException{
       StatementStrategy st = new AddStatement(user);

       jdbcContextWithStatementStrategy(st);

    }*/

    /*// 로컬 클래스를 이용한 add - 전략마다 파일이 생성되는 것을 방지하기 위함
    public void add(final User user) throws ClassNotFoundException, SQLException {
        class AddStatement implements StatementStrategy{

            *//*User user;

            public AddStatement(User user) {
                this.user = user;
            }*//*

            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }

//        StatementStrategy st = new AddStatement(user);
        StatementStrategy st = new AddStatement(); // 파라미터로 user를 전달할 필요 없다.

        jdbcContextWithStatementStrategy(st);
    }*/

    // 익명 클래스를 이용한 add - 전략마다 파일이 생성되는 것을 방지하기 위함
    public void add(final User user) throws ClassNotFoundException, SQLException {

        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }
        );
    }

    // 익명 내부 클래스를 이용한 deleteAll
    public void deleteAll() throws ClassNotFoundException, SQLException{
        jdbcContextWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        return c.prepareStatement("delete from users");
                    }
                }
        );
    }


    // DI 적용을 위한 클라이언트/컨텍스트 분리
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = connectionMaker.makeConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                // ps.close() 메소드에서도 SQLException이 발생할 수 있으므로 try-catch로 잡아준다.
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }
    }
    /*public int getCount() throws SQLException, ClassNotFoundException {

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }*/

    // JDBC 예외처리를 적용한 getCount
    public int getCount() throws SQLException, ClassNotFoundException {

        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = connectionMaker.makeConnection();

            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();

            int count = rs.getInt(1);
            return count;
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {

            // close()는 만들어지는 순서와 반대로 한다.
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }

            if (ps != null) {
                // ps.close() 메소드에서도 SQLException이 발생할 수 있으므로 try-catch로 잡아준다.
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }

        }


    }
}


