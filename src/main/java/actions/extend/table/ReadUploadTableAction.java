package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadUploadTableAction extends InspectionAndReadTableBaseAction {

	private String userID;
	//格式：userID.tablename
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
//		if (readService.readUploadTable(userID, tablename)) {
			return SUCCESS;	
//		}else {
//			return ERROR;
//		}
		
	}
}
