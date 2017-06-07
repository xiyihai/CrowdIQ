package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class UploadTableListAction extends InspectionAndReadTableBaseAction {
	private String userID;
	//格式：userID.tablename
	private String tablelist;

	

	public String getTablelist() {
		return tablelist;
	}

	public void setTablelist(String tablelist) {
		this.tablelist = tablelist;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

		
	public String execute(){	
		if (readService.uploadTableList(userID, tablelist)) {
			return SUCCESS;	
		}else {
			return ERROR;
		}
		
	}
}
