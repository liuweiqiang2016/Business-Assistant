package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;

/**
 * Created by liuweiqiang on 2016/9/23.
 * 开销表
 */
@Table(name = "B_CastModel")
public class CostModel {
    private int id;
    //开销名称
    @Column(column="C_Name")
    private String name;
    //开销类型
    @Column(column="C_Type")
    private String type;
    //开销时间
    @Column(column="C_Time")
    private String time;
    //备注信息
    @Column(column="C_Info")
    private String info;
    //开销金额
    @Column(column="C_Money")
    private String money;
    //开销编号
    @Column(column="C_CID")
    private String cid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
