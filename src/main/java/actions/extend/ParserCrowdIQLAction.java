package actions.extend;

import org.apache.struts2.ServletActionContext;

import actions.base.ParserCrowdIQLBaseAction;

public class ParserCrowdIQLAction extends ParserCrowdIQLBaseAction {

	//用于接受前端的sql语句,和其作用的表名
	private String sql;
	private String tablename;
	private String userID;
	
	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	//用于返回解析出来的各部分元素组成前端的UI模板，里面是hashmap转换过来的
	private String elements;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getElements() {
		return elements;
	}

	public void setElements(String elements) {
		this.elements = elements;
	}
	
	public String execute(){
		String path = ServletActionContext.getRequest().getRealPath("/WEB-INF/tablelists");
		
		//如果sql是insert语句，那么elements为null	
		elements = parserService.parser(sql, userID, tablename, path);
		return SUCCESS;
	}
	
}
