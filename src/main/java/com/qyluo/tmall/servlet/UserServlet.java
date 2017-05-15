package com.qyluo.tmall.servlet;

import com.qyluo.tmall.meta.User;
import com.qyluo.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/15.
 */
public class UserServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
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
        List<User> users = userDAO.list(page.getStart(), page.getCount());
        int total = userDAO.getTotal();
        page.setTotal(total);

        request.setAttribute("us", users);
        request.setAttribute("page", page);
        return "admin/listUser.jsp";
    }
}
