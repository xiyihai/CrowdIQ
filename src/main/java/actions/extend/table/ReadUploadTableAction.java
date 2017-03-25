package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadUploadTableAction extends InspectionAndReadTableBaseAction {

	private String userID;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	//用于返回读取的JSONTable，用于前端展示jsontable树
	private String jsonTable;

	public String getJsonTable() {
		return jsonTable;
	}

	public void setJsonTable(String jsonTable) {
		this.jsonTable = jsonTable;
	}
		
	public String execute(){	
		readservice.readUploadTable(userID);
		readservice.tranferJSONTable();
		jsonTable = readservice.getJSONTable_show().toString();
		return SUCCESS;
	}
}
