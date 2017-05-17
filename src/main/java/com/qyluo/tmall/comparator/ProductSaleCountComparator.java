package com.qyluo.tmall.comparator;

import com.qyluo.tmall.meta.Product;

import java.util.Comparator;

/**
 * Created by qy_lu on 2017/5/17.
 */
public class ProductSaleCountComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount() - p1.getSaleCount();
    }
}
