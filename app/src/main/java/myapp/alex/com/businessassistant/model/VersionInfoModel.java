package myapp.alex.com.businessassistant.model;

public class VersionInfoModel {

	//老版本号
	private String code_old;
	//版本号
	private String code;
	//名称
	private String name;
	//大小
	private String size;
	//更新说明
	private String des;
	//下载地址
	private String link;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getCode_old() {
		return code_old;
	}
	public void setCode_old(String code_old) {
		this.code_old = code_old;
	}
	

}
