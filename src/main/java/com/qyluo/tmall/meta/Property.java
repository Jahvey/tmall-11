package main.java.com.qyluo.tmall.meta;

/**
 * Created by qy_lu on 2017/5/1.
 */
public class Property {
    private String name;
    private int id;
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
