package com.qyluo.tmall.servlet;

import com.qyluo.tmall.meta.Category;
import com.qyluo.tmall.utils.ImageUtil;
import com.qyluo.tmall.utils.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qy_lu on 2017/5/10.
 */
public class CategoryServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<String, String>();
        InputStream is = super.parseUpload(request, params);

        String name = params.get("name");
        Category category = new Category();
        category.setName(name);
        categoryDAO.add(category);

        // upload image
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, category.getId() + ".jpg");

        try {
            if (null != is && 0 != is.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] bytes = new byte[1024 * 1024];
                    int length = 0;
                    while (-1 != (length = is.read(bytes))) {
                        fos.write(bytes, 0, length);
                    }
                    fos.flush();

                    // save image as jpg format
                    BufferedImage bufferedImage = ImageUtil.change2jpg(file);
                    ImageIO.write(bufferedImage, "jpg", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin_category_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = categoryDAO.get(id);
        request.setAttribute("c", c);
        return "admin/editCategory.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<String, String>();
        InputStream is = super.parseUpload(request, params);

        System.out.println(params);
        String name = params.get("name");
        int id = Integer.parseInt(params.get("id"));

        Category category = new Category();
        category.setName(name);
        category.setId(id);
        categoryDAO.update(category);

        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, category.getId() + ".jpg");
        file.getParentFile().mkdirs();

        try {
            if (null != is && 0 != is.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] bytes = new byte[1024 * 1024];
                    int length = 0;
                    while (-1 != (length = is.read(bytes))) {
                        fos.write(bytes, 0, length);
                    }
                    fos.flush();

                    // save image as jpg format
                    BufferedImage bufferedImage = ImageUtil.change2jpg(file);
                    ImageIO.write(bufferedImage, "jpg", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> categories = categoryDAO.list(page.getStart(), page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);

        request.setAttribute("thecs", categories);
        request.setAttribute("page", page);
        return "admin/listCategory.jsp";
    }
}
