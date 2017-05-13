package com.qyluo.tmall.servlet;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.meta.Property;
import com.qyluo.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by qy_lu on 2017/5/12.
 */
public class PropertyServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);

        String name = request.getParameter("name");
        Property property = new Property();
        property.setName(name);
        property.setCategory(category);
        propertyDAO.add(property);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property property = propertyDAO.get(id);
        propertyDAO.delete(id);
        return "@admin_property_list?cid=" + property.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property property = propertyDAO.get(id);
        request.setAttribute("p", property);
        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        Property property = new Property();
        property.setId(id);
        property.setName(name);
        property.setCategory(category);
        propertyDAO.update(property);
        return "@admin_property_list?cid=" + property.getCategory().getId();
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        List<Property> properties = propertyDAO.list(cid, page.getStart(), page.getCount());
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid=" + cid);

        request.setAttribute("c", category);
        request.setAttribute("ps", properties);
        request.setAttribute("page", page);
        return "admin/listProperty.jsp";
    }
}
