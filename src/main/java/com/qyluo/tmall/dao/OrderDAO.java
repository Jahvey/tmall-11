package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Order;
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
public class OrderDAO {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from order_";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Order order) {
        String sql = "insert into order_ values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderCode());
            ps.setString(2, order.getAddress());
            ps.setString(3, order.getPost());
            ps.setString(4, order.getReceiver());
            ps.setString(5, order.getMobile());
            ps.setString(6, order.getUserMessage());
            ps.setTimestamp(7, DateUtil.d2t(order.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(order.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(order.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(order.getConfirmDate()));
            ps.setInt(11, order.getUser().getId());
            ps.setString(12, order.getStatus());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                order.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Order order) {
        String sql = "update order_ set orderCode = ?, address = ?, post = ?, receiver = ?, mobile = ?, userMessage = ?, " +
                "createDate = ?, payDate = ?, deliveryDate = ?, confirmDate = ?, uid = ?, status = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderCode());
            ps.setString(2, order.getAddress());
            ps.setString(3, order.getPost());
            ps.setString(4, order.getReceiver());
            ps.setString(5, order.getMobile());
            ps.setString(6, order.getUserMessage());
            ps.setTimestamp(7, DateUtil.d2t(order.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(order.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(order.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(order.getConfirmDate()));
            ps.setInt(11, order.getUser().getId());
            ps.setString(12, order.getStatus());
            ps.setInt(13, order.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from order_ where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order get(int id) {
        Order order = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from order_ where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                int uid =rs.getInt("uid");
                User user = new UserDAO().get(uid);
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));

                order = new Order();
                order.setId(id);
                order.setOrderCode(orderCode);
                order.setAddress(address);
                order.setPost(post);
                order.setReceiver(receiver);
                order.setMobile(mobile);
                order.setUserMessage(userMessage);
                order.setStatus(status);
                order.setUser(user);
                order.setCreateDate(createDate);
                order.setPayDate(payDate);
                order.setDeliveryDate(deliveryDate);
                order.setConfirmDate(confirmDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<Order> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Order> list(int start, int count) {
        List<Order> orders = new ArrayList<Order>();
        String sql = "select * from order_ order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                int uid =rs.getInt("uid");
                User user = new UserDAO().get(uid);
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));

                Order order = new Order();
                order.setId(id);
                order.setOrderCode(orderCode);
                order.setAddress(address);
                order.setPost(post);
                order.setReceiver(receiver);
                order.setMobile(mobile);
                order.setUserMessage(userMessage);
                order.setStatus(status);
                order.setUser(user);
                order.setCreateDate(createDate);
                order.setPayDate(payDate);
                order.setDeliveryDate(deliveryDate);
                order.setConfirmDate(confirmDate);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> list(int uid, String excludedStatus) {
        return list(uid, excludedStatus, 0, Short.MAX_VALUE);
    }

    public List<Order> list(int uid, String excludedStatus, int start, int count) {
        String sql = "select * from order_ where uid = ? and status != ? order by id desc limit ?, ?";
        List<Order> orders = new ArrayList<Order>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setString(2, excludedStatus);
            ps.setInt(3, start);
            ps.setInt(4, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                User user = new UserDAO().get(uid);
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));

                Order order = new Order();
                order.setId(id);
                order.setOrderCode(orderCode);
                order.setAddress(address);
                order.setPost(post);
                order.setReceiver(receiver);
                order.setMobile(mobile);
                order.setUserMessage(userMessage);
                order.setStatus(status);
                order.setUser(user);
                order.setCreateDate(createDate);
                order.setPayDate(payDate);
                order.setDeliveryDate(deliveryDate);
                order.setConfirmDate(confirmDate);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
