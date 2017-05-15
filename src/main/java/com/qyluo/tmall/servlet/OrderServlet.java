package com.qyluo.tmall.servlet;

import com.qyluo.tmall.dao.OrderDAO;
import com.qyluo.tmall.meta.Order;
import com.qyluo.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/15.
 */
public class OrderServlet extends BaseBackServlet {
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
        List<Order> orders = orderDAO.list(page.getStart(), page.getCount());
        orderItemDAO.fill(orders);
        int total = orderDAO.getTotal();
        page.setTotal(total);

        request.setAttribute("os", orders);
        request.setAttribute("page", page);
        return "admin/listOrder.jsp";
    }

    public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderDAO.get(id);
        order.setDeliveryDate(new Date());
        order.setStatus(OrderDAO.waitConfirm);
        orderDAO.update(order);
        return "@admin_order_list";
    }
}
