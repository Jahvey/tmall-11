package com.qyluo.tmall.comparator;

import com.qyluo.tmall.meta.Product;

import java.util.Comparator;

/**
 * Created by qy_lu on 2017/5/17.
 */
public class ProductReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount() - p1.getReviewCount();
    }
}
