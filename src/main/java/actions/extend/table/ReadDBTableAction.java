package actions.extend.table;

import actions.base.InspectionAndReadTableBaseAction;

public class ReadDBTableAction extends InspectionAndReadTableBaseAction {

	//用于返回读取的JSONTable，用于前端返回json树
	private String jsonTable;

	public String getJsonTable() {
		return jsonTable;
	}

	public void setJsonTable(String jsonTable) {
		this.jsonTable = jsonTable;
	}
	
	public String execute(){	
		readservice.readDBTable();
		readservice.tranferJSONTable();
		jsonTable = readservice.getJSONTable_show().toString();
		return SUCCESS;
	}
}
