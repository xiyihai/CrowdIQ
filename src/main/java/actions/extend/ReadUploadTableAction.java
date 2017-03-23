package actions.extend;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadUploadTableAction extends InspectionAndReadTableBaseAction {

	//用于返回读取的JSONTable，用于前端展示jsontable树
	private String jsonTable;

	public String getJsonTable() {
		return jsonTable;
	}

	public void setJsonTable(String jsonTable) {
		this.jsonTable = jsonTable;
	}
		
	public String execute(){	
		readservice.readUploadTable();
		readservice.tranferJSONTable();
		jsonTable = readservice.getJSONTable_show().toString();
		return SUCCESS;
	}
}
