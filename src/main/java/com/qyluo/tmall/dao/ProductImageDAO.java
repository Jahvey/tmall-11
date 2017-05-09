package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.ProductImage;
import com.qyluo.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/7.
 */
public class ProductImageDAO {
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from productimage";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(ProductImage productImage) {
        String sql = "insert into productimage values (null, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productImage.getProduct().getId());
            ps.setString(2, productImage.getType());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                productImage.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ProductImage productImage) {
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from productimage where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductImage get(int id) {
        ProductImage productImage = new ProductImage();
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from productimage where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                Product product = new ProductDAO().get(pid);
                String type = rs.getString("type");
                productImage.setId(id);
                productImage.setProduct(product);
                productImage.setType(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productImage;
    }

    public List<ProductImage> list(Product product, String type) {
        return list(product, type, 0, Short.MAX_VALUE);
    }

    public List<ProductImage> list(Product product, String type, int start, int count) {
        List<ProductImage> productImages = new ArrayList<ProductImage>();
        String sql = "select * from productimage where pid = ? and type = ? order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getId());
            ps.setString(2, type);
            ps.setInt(3, start);
            ps.setInt(4, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductImage productImage = new ProductImage();
                int id = rs.getInt("id");
                productImage.setId(id);
                productImage.setType(type);
                productImage.setProduct(product);
                productImages.add(productImage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productImages;
    }
}
