package com.qyluo.tmall.servlet;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.PropertyValue;
import com.qyluo.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/13.
 */
public class ProductServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);

        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Product product = new Product();
        product.setName(name);
        product.setSubTitle(subTitle);
        product.setOrignalPrice(orignalPrice);
        product.setPromotePrice(promotePrice);
        product.setStock(stock);
        product.setCategory(category);
        productDAO.add(product);
        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(id);
        int cid = product.getCategory().getId();
        productDAO.delete(id);
        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(id);
        request.setAttribute("p", product);
        return "admin/editProduct.jsp";
    }

    public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(id);
        request.setAttribute("p", product);

        propertyValueDAO.init(product);

        List<PropertyValue> propertyValues = propertyValueDAO.list(product.getId());
        request.setAttribute("pvs", propertyValues);
        return "admin/editPropertyValue.jsp";
    }

    public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pvid = Integer.parseInt(request.getParameter("pvid"));
        String value = request.getParameter("value");

        PropertyValue propertyValue = propertyValueDAO.get(pvid);
        propertyValue.setValue(value);
        propertyValueDAO.update(propertyValue);
        return "%success";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);

        int id = Integer.parseInt(request.getParameter("id"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        String subTitle = request.getParameter("subTitle");
        String name = request.getParameter("name");

        Product product = new Product();
        product.setStock(stock);
        product.setPromotePrice(promotePrice);
        product.setOrignalPrice(orignalPrice);
        product.setSubTitle(subTitle);
        product.setName(name);
        product.setCategory(category);
        product.setId(id);

        productDAO.update(product);
        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        List<Product> products = productDAO.list(cid, page.getStart(), page.getCount());
        int total = productDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid=" + cid);

        request.setAttribute("ps", products);
        request.setAttribute("c", category);
        request.setAttribute("page", page);
        return "admin/listProduct.jsp";
    }
}
