package com.qyluo.tmall.servlet;

import com.qyluo.tmall.dao.*;
import com.qyluo.tmall.utils.Page;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by qy_lu on 2017/5/10.
 */
public abstract class BaseBackServlet extends HttpServlet {
    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // get page information
            int start = 0;
            int count = 5;
            try {
                start = Integer.parseInt(req.getParameter("page.start"));
            } catch (Exception e) {
            }

            try {
                count = Integer.parseInt(req.getParameter("page.count"));
            } catch (Exception e) {
            }

            Page page = new Page(start, count);

            // invoke the corresponding method using reflection
            String method = (String) req.getAttribute("method");
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class, javax.servlet.http.HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, req, resp, page).toString();

            // choose to dispatch, redirect or output string according to the return redirect
            if (redirect.startsWith("@")) {
                resp.sendRedirect(redirect.substring(1));
            } else if (redirect.startsWith("%")) {
                resp.getWriter().print(redirect.substring(1));
            } else {
                req.getRequestDispatcher(redirect).forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
        InputStream is = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            factory.setSizeThreshold(1024 * 10240);

            List items = upload.parseRequest(request);
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                FileItem item = (FileItem) iterator.next();
                if (!item.isFormField()) {
                    is = item.getInputStream();
                } else {
                    String paramName = item.getFieldName();
                    String paramValue = item.getString();
                    paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
                    params.put(paramName, paramValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }
}
