package com.qyluo.tmall.dao;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.ProductImage;
import com.qyluo.tmall.utils.DBUtil;
import com.qyluo.tmall.utils.DateUtil;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/9.
 */
public class ProductDAO {
    public int getTotal(int cid) {
        int total = 0;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select count(*) from product where cid = " + cid;
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Product product) {
        String sql = "insert into product values (null, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getSubTitle());
            ps.setFloat(3, product.getOriginalPrice());
            ps.setFloat(4, product.getPromotePrice());
            ps.setInt(5, product.getStock());
            ps.setInt(6, product.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(product.getCreateDate()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                product.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Product product) {
        String sql = "update product set name = ?, subTitle = ?, originalPrice = ?, promotePrice = ?, stock = ?, cid = ?, " +
                "createDate = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getSubTitle());
            ps.setFloat(3, product.getOriginalPrice());
            ps.setFloat(4, product.getPromotePrice());
            ps.setInt(5, product.getStock());
            ps.setInt(6, product.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(product.getCreateDate()));
            ps.setInt(8, product.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "delete from product where id = " + id;
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product get(int id) {
        Product product = null;
        try (Connection conn = DBUtil.getConnection(); Statement stat = conn.createStatement()) {
            String sql = "select * from product where id = " + id;
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                product = new Product();
                product.setName(name);
                product.setSubTitle(subTitle);
                product.setOriginalPrice(originalPrice);
                product.setPromotePrice(promotePrice);
                product.setStock(stock);
                product.setCreateDate(createDate);
                Category category = new CategoryDAO().get(cid);
                product.setCategory(category);
                product.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }

    public List<Product> list(int cid, int start, int count) {
        List<Product> products = new ArrayList<Product>();
        String sql = "select * from product where cid = ? order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                Product product = new Product();
                product.setName(name);
                product.setSubTitle(subTitle);
                product.setOriginalPrice(originalPrice);
                product.setPromotePrice(promotePrice);
                product.setStock(stock);
                product.setCreateDate(createDate);
                Category category = new CategoryDAO().get(cid);
                product.setCategory(category);
                product.setId(id);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Product> list(int start, int count) {
        List<Product> products = new ArrayList<Product>();
        String sql = "select * from product order by id desc limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                Product product = new Product();
                product.setName(name);
                product.setSubTitle(subTitle);
                product.setOriginalPrice(originalPrice);
                product.setPromotePrice(promotePrice);
                product.setStock(stock);
                product.setCreateDate(createDate);
                Category category = new CategoryDAO().get(cid);
                product.setCategory(category);
                product.setId(id);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void fill(List<Category> categories) {
        for (Category category : categories) {
            fill(category);
        }
    }

    public void fill(Category category) {
        List<Product> products = list(category.getId());
        category.setProducts(products);
    }

    public void fillByRow(List<Category> categories) {
        for (Category category : categories) {
            fillByRow(category);
        }
    }

    public void fillByRow(Category category) {
        int productNumberEachRow = 8;
        List<List<Product>> productsByRow = new ArrayList<List<Product>>();
        List<Product> products = category.getProducts();
        for (int i = 0; i < products.size(); i += productNumberEachRow) {
            int size = i + productNumberEachRow;
            size = size > products.size() ? products.size() : size;
            List<Product> productsOfEachRow = products.subList(i, size);
            productsByRow.add(productsOfEachRow);
        }
        category.setProductsByRow(productsByRow);
    }

    public void setFirstProductImage(Product product) {
        List<ProductImage> productImages = new ProductImageDAO().list(product, ProductImageDAO.type_single);
        if (!productImages.isEmpty()) {
            product.setFirstProductImage(productImages.get(0));
        }
    }

    public void setSaleAndReviewNumber(Product product) {
        int saleCount = new OrderItemDAO().getSaleCount(product.getId());
        product.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getTotal(product.getId());
        product.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    public List<Product> search(String keyWord, int start, int count) {
        List<Product> products = null;
        if (null == keyWord || keyWord.trim().length() == 0) {
            return products;
        }

        String sql = "select * from product where name like ? limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyWord.trim() + "%");
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                product.setName(name);
                product.setSubTitle(subTitle);
                product.setOriginalPrice(originalPrice);
                product.setPromotePrice(promotePrice);
                product.setStock(stock);
                product.setCreateDate(createDate);
                Category category = new CategoryDAO().get(cid);
                product.setCategory(category);
                product.setId(id);
                setFirstProductImage(product);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
