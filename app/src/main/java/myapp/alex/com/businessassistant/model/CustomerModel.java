package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;

/**
 * Created by liuweiqiang on 2016/9/26.
 * 客户表
 */
@Table(name = "B_CustomerModel")
public class CustomerModel {
    private int id;
    //订单编号，非常重要，与详情表的唯一关联
    @Column(column = "C_OrderID")
    private String order_id;
    //客户名称
    @Column(column = "C_Name")
    private String name;
    //联系方式
    @Column(column = "C_Tel")
    private String tel;
    //地址
    @Column(column = "C_Adds")
    private String adds;
    //备注信息
    @Column(column = "C_Info")
    private String info;
    //数据来源 0:订单生成 1：用户添加
    @Column(column = "C_Source")
    private String source;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdds() {
        return adds;
    }

    public void setAdds(String adds) {
        this.adds = adds;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
