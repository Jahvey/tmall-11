package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.Review;
import com.qyluo.tmall.meta.User;
import com.qyluo.tmall.utils.DBUtil;
import com.qyluo.tmall.utils.DateUtil;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/8.
 */
public class ReviewDAO {
    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from review";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public int getTotal(int pid) {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from review where pid = " + pid;
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Review review) {
        String sql = "insert into review values (null, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, review.getContent());
            ps.setInt(2, review.getUser().getId());
            ps.setInt(3, review.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(review.getCreateDate()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                review.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Review review) {
        String sql = "update review set content = ?, uid = ?, pid = ?, createDate = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, review.getContent());
            ps.setInt(2, review.getUser().getId());
            ps.setInt(3, review.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(review.getCreateDate()));
            ps.setInt(5, review.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from review where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Review get(int id) {
        Review review = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from review where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                int pid = rs.getInt("pid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                User user = new UserDAO().get(uid);
                Product product = new ProductDAO().get(pid);

                review = new Review();
                review.setContent(content);
                review.setUser(user);
                review.setProduct(product);
                review.setCreateDate(createDate);
                review.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }

    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }

    public List<Review> list(int pid, int start, int count) {
        String sql = "select * from review where pid = ? order by id desc limit ?, ?";
        List<Review> reviews = new ArrayList<Review>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                User user = new UserDAO().get(uid);
                Product product = new ProductDAO().get(pid);

                Review review = new Review();
                review.setContent(content);
                review.setUser(user);
                review.setProduct(product);
                review.setCreateDate(createDate);
                review.setId(id);
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

}
