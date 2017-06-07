package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;
import net.sf.json.JSONObject;

public class ShowDBTableAction extends InspectionAndReadTableBaseAction {

	//接受前端雇主ID
	private String userID;
	//返回前端对应所有的table信息，包括ID和状态, JSONArray[ tableID:state, tableID:state ]
	private String tableInfo;

	public String getTableInfo() {
		return tableInfo;
	}

	public void setTableinfo(String tableInfo) {
		this.tableInfo = tableInfo;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String execute(){
		tableInfo = readService.showAllTable(userID);
		return SUCCESS;
	}
}
