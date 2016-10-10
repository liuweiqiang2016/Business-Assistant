package myapp.alex.com.businessassistant.model;

import com.zeone.framework.db.annotation.Column;
import com.zeone.framework.db.annotation.Table;

/**
 * Created by liuweiqiang on 2016/9/23.
 * 笔记表
 */
@Table(name = "B_NoteModel")
public class NoteModel {
    private int id;
    //笔记主题
    @Column(column="C_Subject")
    private String subject;
    //编辑时间
    @Column(column="C_Time")
    private String time;
    //笔记内容
    @Column(column="C_Body")
    private String body;
    //笔记编号
    @Column(column="C_ID")
    private String cid;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
