package com.qyluo.tmall.servlet;

import com.qyluo.tmall.dao.ProductImageDAO;
import com.qyluo.tmall.meta.Product;
import com.qyluo.tmall.meta.ProductImage;
import com.qyluo.tmall.utils.ImageUtil;
import com.qyluo.tmall.utils.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qy_lu on 2017/5/13.
 */
public class ProductImageServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        InputStream is = null;

        Map<String, String> params = new HashMap<>();
        is = parseUpload(request, params);

        // generate productImage object from input parameters
        String type = params.get("type");
        int pid = Integer.parseInt(params.get("pid"));
        Product product = productDAO.get(pid);

        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        productImage.setProduct(product);
        productImageDAO.add(productImage);

        // generate files
        String fileName = productImage.getId() + ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if (ProductImageDAO.type_single.equals(productImage.getType())) {
            imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
            imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
        } else {
            imageFolder = request.getSession().getServletContext().getRealPath("img/productDetail");
        }

        File file = new File(imageFolder, fileName);
        file.getParentFile().mkdirs();

        // copy files
        try {
            if (null != is && 0 != is.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] bytes = new byte[1024 * 1024];
                    int length;
                    while (-1 != (length = is.read(bytes))) {
                        fos.write(bytes, 0, length);
                    }
                    fos.flush();

                    BufferedImage img = ImageUtil.change2jpg(file);
                    ImageIO.write(img, "jpg", file);

                    if (ProductImageDAO.type_single.equals(productImage.getType())) {
                        File file_small = new File(imageFolder_small, fileName);
                        file_small.getParentFile().mkdirs();
                        File file_middle = new File(imageFolder_middle, fileName);
                        file_middle.getParentFile().mkdirs();

                        ImageUtil.resizeImage(file, 56, 56, file_small);
                        ImageUtil.resizeImage(file, 217, 190, file_middle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "@admin_productImage_list?pid=" + product.getId();
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductImage pi = productImageDAO.get(id);
        productImageDAO.delete(id);

        if (ProductImageDAO.type_single.equals(pi.getType())) {
            String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
            String imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");

            File file_single = new File(imageFolder_single, pi.getId() + ".jpg");
            file_single.delete();
            File file_small = new File(imageFolder_small, pi.getId() + ".jpg");
            file_small.delete();
            File file_middle = new File(imageFolder_middle, pi.getId() + ".jpg");
            file_middle.delete();
        } else {
            String imageFolder_detail = request.getSession().getServletContext().getRealPath("img/productDetail");
            File file_detail = new File(imageFolder_detail, pi.getId() + ".jpg");
            file_detail.delete();
        }
        return "@admin_productImage_list?pid=" + pi.getProduct().getId();
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
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        List<ProductImage> pisSingle = productImageDAO.list(product, ProductImageDAO.type_single);
        List<ProductImage> pisDetail = productImageDAO.list(product, ProductImageDAO.type_detail);

        request.setAttribute("p", product);
        request.setAttribute("pisSingle", pisSingle);
        request.setAttribute("pisDetail", pisDetail);
        return "admin/listProductImage.jsp";
    }
}

