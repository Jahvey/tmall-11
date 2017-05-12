package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/1.
 */
public class CategoryDAO {
    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from category";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Category category) {
        String sql = "insert into category values (null, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Category category) {
        String sql = "update category set name = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from category where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category get(int id) {
        Category category = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from category where id = " + id;
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                String name = rs.getString(2);

                category = new Category();
                category.setId(id);
                category.setName(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public List<Category> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Category> list(int start, int count) {
        List<Category> categories = new ArrayList<Category>();
        String sql = "select * from category order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                int id = rs.getInt(1);
                String name = rs.getString(2);
                category.setId(id);
                category.setName(name);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
