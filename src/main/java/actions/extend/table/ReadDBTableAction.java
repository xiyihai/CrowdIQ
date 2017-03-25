package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadDBTableAction extends InspectionAndReadTableBaseAction {

	private String userID;
	private String tableID;
	
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
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
		readservice.readDBTable(userID,tableID);
		readservice.tranferJSONTable();
		jsonTable = readservice.getJSONTable_show().toString();
		return SUCCESS;
	}
}
