package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ShowDBTableAction extends InspectionAndReadTableBaseAction {

	//接受前端雇主ID
	private String userID;
	//返回前端对应所有的tableID
	private String tableID;
	
	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String execute(){
		tableID = readservice.showAllTable(userID).toString();
		return SUCCESS;
	}
}
