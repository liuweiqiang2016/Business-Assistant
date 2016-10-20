package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;

/**
 * Created by liuweiqiang on 2016/9/23.
 * 版本更新信息表
 */
@Table(name = "B_VersionModel")
public class VersionModel {
    private int id;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //版本号
    @Column(column="C_Code")
    private String code;
    //更新时间
    @Column(column="C_Time")
    private String time;

}
