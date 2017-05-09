package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.meta.Property;
import com.qyluo.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/6.
 */
public class PropertyDAO {
    public int getTotal(int cid) {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from property where cid = " + cid;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Property property) {
        String sql = "insert into property values (null, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, property.getCategory().getId());
            ps.setString(2, property.getName());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                property.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Property property) {
        String sql = "update property set cid = ?, name = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, property.getCategory().getId());
            ps.setString(2, property.getName());
            ps.setInt(3, property.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "delete from property where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Property get(String name, int cid) {
        Property property = null;
        String sql = "select * from property where name = ? and cid = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, cid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                property = new Property();
                property.setId(id);
                property.setName(name);
                Category category = new CategoryDAO().get(cid);
                property.setCategory(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return property;
    }

    public Property get(int id) {
        Property property = null;
        try (Connection connection = DBUtil.getConnection(); Statement stat = connection.createStatement()) {
            String sql = "select * from property where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                int cid = rs.getInt("cid");
                Category category = new CategoryDAO().get(cid);
                property = new Property();
                property.setId(id);
                property.setName(name);
                property.setCategory(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return property;
    }

    public List<Property> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }

    public List<Property> list(int cid, int start, int count) {
        List<Property> properties = new ArrayList<Property>();
        String sql = "select * from property where cid = ? order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Property property = new Property();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Category category = new CategoryDAO().get(cid);
                property.setId(id);
                property.setName(name);
                property.setCategory(category);
                properties.add(property);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
