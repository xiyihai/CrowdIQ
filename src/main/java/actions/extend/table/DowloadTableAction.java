package actions.extend.table;

import org.apache.struts2.ServletActionContext;

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
		String path = ServletActionContext.getRequest().getRealPath("/WEB-INF/uploadTables");
		
		if (readService.downloadTable(tableID, userID, path)) {
			return SUCCESS;			
		}else {
			return ERROR;
		}
	}
}
