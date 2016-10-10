package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;


/**
 * Created by uu on 2016/9/9.
 *
 * 订单表
 */
    @Table(name = "B_OrderModel")
public class OrderModel {
    private int id;
    //订单编号，非常重要，与详情表的唯一关联
    @Column(column = "C_OrderID")
    private String order_id;
    //订单总金额
    @Column(column = "C_Total")
    private String total;
    //下单时间
    @Column(column = "C_Time")
    private String time;
    //客户名称(为节省运算内存，本应放只在CustomerModel表中)
    @Column(column = "C_Name")
    private String name;
//    //联系方式
//    @Column(column = "C_Tel")
//    private String tel;
//    //地址
//    @Column(column = "C_Adds")
//    private String adds;
//    //备注信息
//    @Column(column = "C_Info")
//    private String info;
    //订单状态 0表示未完成 1表示已完成
    @Column(column = "C_State")
    private  String state;
    //完成时间
    @Column(column = "C_DoneTime")
    private  String dtime;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public String getTel() {
//        return tel;
//    }
//
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    public String getAdds() {
//        return adds;
//    }
//
//    public void setAdds(String adds) {
//        this.adds = adds;
//    }
//
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }
}
