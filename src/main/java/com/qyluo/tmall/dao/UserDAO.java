package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.User;
import com.qyluo.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/5.
 */
public class UserDAO {
    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from user";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(User user) {
        String sql = "insert into user values (null, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        String sql = "update user set name = ?, password = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from user where id = " + id;
            stat.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(int id) {
        User user = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from user where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                user = new User();
                user.setId(id);
                user.setName(name);
                user.setPassword(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<User> list(int start, int count) {
        List<User> users = new ArrayList<User>();
        String sql = "select * from user order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User get(String name) {
        User user = null;
        String sql = "select * from user where name = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(name);
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User get(String name, String password) {
        User user = null;
        String sql = "select * from user where name = ? and password = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(name);
                user.setPassword(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isExist(String name) {
        User user = get(name);
        return user != null;
    }
}
