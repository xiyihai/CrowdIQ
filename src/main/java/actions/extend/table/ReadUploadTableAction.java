package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadUploadTableAction extends InspectionAndReadTableBaseAction {

	private String userID;
	private String tablename;

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

		
	public String execute(){	
		readservice.readUploadTable(userID, tablename);
		return SUCCESS;
	}
}
