package com.qyluo.tmall.comparator;

import com.qyluo.tmall.meta.Product;

import java.util.Comparator;

/**
 * Created by qy_lu on 2017/5/17.
 */
public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice() - p2.getPromotePrice());
    }
}
