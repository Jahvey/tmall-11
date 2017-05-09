package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.Property;
import com.qyluo.tmall.meta.PropertyValue;
import com.qyluo.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/8.
 */
public class PropertyValueDAO {
    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from propertyvalue";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(PropertyValue propertyValue) {
        String sql = "insert into propertyvalue values (null, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, propertyValue.getProduct().getId());
            ps.setInt(2, propertyValue.getProperty().getId());
            ps.setString(3, propertyValue.getValue());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                propertyValue.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(PropertyValue propertyValue) {
        String sql = "update propertyvalue set pid = ?, ptid = ?, value = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, propertyValue.getProduct().getId());
            ps.setInt(2, propertyValue.getProperty().getId());
            ps.setString(3, propertyValue.getValue());
            ps.setInt(4, propertyValue.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from propertyvalue where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PropertyValue get(int id) {
        PropertyValue propertyValue = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from propertyvalue where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Property property = new PropertyDAO().get(ptid);
                Product product = new ProductDAO().get(pid);
                propertyValue = new PropertyValue();
                propertyValue.setId(id);
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValue.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propertyValue;
    }

    public PropertyValue get(int ptid, int pid) {
        PropertyValue propertyValue = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from propertyvalue where ptid = " + ptid + " and pid = " + pid;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                int id = rs.getInt("id");
                String value = rs.getString("value");

                Property property = new PropertyDAO().get(ptid);
                Product product = new ProductDAO().get(pid);
                propertyValue = new PropertyValue();
                propertyValue.setId(id);
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValue.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propertyValue;
    }

    public List<PropertyValue> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<PropertyValue> list(int start, int count) {
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        String sql = "select * from propertyvalue order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue propertyValue = new PropertyValue();
                int id = rs.getInt("id");
                int ptid = rs.getInt("ptid");
                int pid = rs.getInt("pid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                propertyValue.setId(id);
                propertyValue.setValue(value);
                propertyValue.setProperty(property);
                propertyValue.setProduct(product);

                propertyValues.add(propertyValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propertyValues;
    }

    public void init(Product product) {
        List<Property> properties = new PropertyDAO().list(product.getCategory().getId());

        for (Property property : properties) {
            PropertyValue propertyValue = get(property.getId(), product.getId());
            if (null == propertyValue) {
                propertyValue = new PropertyValue();
                propertyValue.setProperty(property);
                propertyValue.setProduct(product);
                this.add(propertyValue);
            }
        }
    }

    public List<PropertyValue> list(int pid) {
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        String sql = "select * from propertyvalue where pid = ? order by ptid desc";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue propertyValue = new PropertyValue();
                int id = rs.getInt("id");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Property property = new PropertyDAO().get(ptid);
                Product product = new ProductDAO().get(pid);
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValue.setId(id);
                propertyValue.setValue(value);
                propertyValues.add(propertyValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propertyValues;
    }
}
