package com.qyluo.tmall.servlet;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/16.
 */
public class ForeServlet extends BaseForeServlet {
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = categoryDAO.list();
        productDAO.fill(cs);
        productDAO.fillByRow(cs);
        request.setAttribute("cs", cs);
        return "home.jsp";
    }

}
