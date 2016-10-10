package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;

/**
 * Created by uu on 2016/9/7.
 * 订单内容详情表
 * 存储大量订单数据
 * 订单编号是此表与订单表唯一联系的关系，非常重要
 */
@Table(name = "B_OrderServiceModel")
public class OrderServiceModel {
    private int id;
    //服务名称
    @Column(column="C_Name")
    private String name;
    //服务价格
    @Column(column="C_Price")
    private float price;
    //服务数量
    @Column(column ="C_Num")
    private String num;
    //订单编号
    @Column(column="C_OrderID")
    private String order_id;

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
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
