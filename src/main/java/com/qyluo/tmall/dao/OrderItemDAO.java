package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Order;
import com.qyluo.tmall.meta.OrderItem;
import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.User;
import com.qyluo.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/9.
 */
public class OrderItemDAO {
    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from orderitem";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(OrderItem orderItem) {
        String sql = "insert into orderitem values (null, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItem.getProduct().getId());
            if (null == orderItem.getOrder()) {
                ps.setInt(2, -1);
            } else {
                ps.setInt(2, orderItem.getOrder().getId());
            }
            ps.setInt(3, orderItem.getUser().getId());
            ps.setInt(4, orderItem.getNumber());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                orderItem.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrderItem orderItem) {
        String sql = "update orderitem set pid = ?, oid = ?, uid = ?, number = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItem.getProduct().getId());
            if (null == orderItem.getOrder()) {
                ps.setInt(2, -1);
            } else {
                ps.setInt(2, orderItem.getOrder().getId());
            }
            ps.setInt(3, orderItem.getUser().getId());
            ps.setInt(4, orderItem.getNumber());
            ps.setInt(5, orderItem.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from orderitem where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem get(int id) {
        OrderItem orderItem = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from orderitem where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                orderItem = new OrderItem();
                orderItem.setId(id);
                orderItem.setNumber(number);
                orderItem.setProduct(product);
                orderItem.setUser(user);

                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    orderItem.setOrder(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItem;
    }

    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByUser(int uid, int start, int count) {
        String sql = "select * from orderitem where uid = ? and oid = -1 order by id desc limit ?, ?";
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().get(pid);
                orderItem.setProduct(product);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    orderItem.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                orderItem.setUser(user);
                orderItem.setNumber(number);
                orderItem.setId(id);
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByOrder(int oid, int start, int count) {
        String sql = "select * from orderitem where oid = ? order by desc limit ?, ?";
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    orderItem.setOrder(order);
                }
                Product product = new ProductDAO().get(pid);
                orderItem.setProduct(product);
                User user = new UserDAO().get(uid);
                orderItem.setUser(user);
                orderItem.setNumber(number);
                orderItem.setId(id);
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    public void fill(Order order) {
        List<OrderItem> orderItems = listByOrder(order.getId());
        float total = 0;
        int totalNumber = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
        }
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
        order.setTotal(total);
    }

    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where pid = ? order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                int id = rs.getInt("id");
                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    orderItem.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                orderItem.setUser(user);
                Product product = new ProductDAO().get(pid);
                orderItem.setProduct(product);
                orderItem.setNumber(number);
                orderItem.setId(id);
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public int getSaleCount(int pid) {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select sum(number) from orderitem where oid != -1 and pid = " + pid;
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
