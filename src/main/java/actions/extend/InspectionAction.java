package actions.extend;

import actions.base.InspectionAndReadTableBaseAction;

public class InspectionAction extends InspectionAndReadTableBaseAction {

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

	//用来返回存在的问题
	private String problems;

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
	}
	
	public String execute(){	
		readService.readDBTable(userID,tablename);
		problems = inService.inspect(readService.getJSONTable_show());
		return SUCCESS;
	}
}
