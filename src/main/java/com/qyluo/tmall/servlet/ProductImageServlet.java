package com.qyluo.tmall.servlet;

import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.ProductImage;
import com.qyluo.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qy_lu on 2017/5/13.
 */
public class ProductImageServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        InputStream is = null;

        Map<String, String> params = new HashMap<>();
        parseUpload(request, params);

        String type = params.get("type");
        int pid = Integer.parseInt(params.get("pid"));
        Product product = productDAO.get(pid);

        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        productImage.setProduct(product);
        productImageDAO.add(productImage);



        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }
}
