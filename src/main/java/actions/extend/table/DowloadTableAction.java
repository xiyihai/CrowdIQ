package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class DowloadTableAction extends InspectionAndReadTableBaseAction {

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
	
	public String execute(){
		if (readservice.downloadTable(tableID, userID)) {
			return SUCCESS;			
		}else {
			return ERROR;
		}
	}
}
