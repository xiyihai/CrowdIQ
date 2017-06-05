package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class DeleteTableAction extends InspectionAndReadTableBaseAction {
	
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
	
	public String execute(){	
		readservice.deleteTable(userID,tablename);
		return SUCCESS;
	}
}
