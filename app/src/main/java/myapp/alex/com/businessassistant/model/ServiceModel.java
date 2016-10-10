package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;

/**
 * Created by uu on 2016/9/6.
 * 仅仅起选项作用，不存储大量数据
 */
@Table(name="B_ServiceModel")
public class ServiceModel {

    private int id;
    //服务名称
    @Column(column="C_Name")
    private String name;
    //服务价格
    @Column(column="C_Price")
    private float price;
    //服务编号
    @Column(column="C_ID")
    private int c_id;
    //是否隐藏(服务被删除后，隐藏0，显示1)
    @Column(column = "C_Show")
    private String show;

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public int getC_id() {
        return c_id;
    }
    public void setC_id(int c_id) {
        this.c_id = c_id;
    }
}
