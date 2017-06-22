package actions.extend.table;

import org.apache.struts2.ServletActionContext;

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
		String path = ServletActionContext.getRequest().getRealPath("/WEB-INF/uploadTables");
		
		readService.deleteTable(userID,tablename,path);
		return SUCCESS;
	}
}
