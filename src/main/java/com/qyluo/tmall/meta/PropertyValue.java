package main.java.com.qyluo.tmall.meta;

/**
 * Created by qy_lu on 2017/5/1.
 */
public class PropertyValue {
    private String value;
    private int id;
    private Property property;
    private Product product;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
