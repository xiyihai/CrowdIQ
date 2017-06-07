package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadDBTableAction extends InspectionAndReadTableBaseAction {

	private String userID;
	private String tablename;
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	//用于返回读取的JSONTable，用于前端返回json树
	private String jsonTable;

	public String getJsonTable() {
		return jsonTable;
	}

	public void setJsonTable(String jsonTable) {
		this.jsonTable = jsonTable;
	}
	
	public String execute(){	
		jsonTable = readService.readDBTable(userID,tablename);
		return SUCCESS;
	}
}
