package actions.extend;

import actions.base.ParserCrowdIQLBaseAction;

public class ParserCrowdIQLAction extends ParserCrowdIQLBaseAction {

	//用于接受前端的sql语句
	private String sql;
	
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
		elements = parserService.parser(sql, readService.getJsonTable());
		return SUCCESS;
	}
	
}
