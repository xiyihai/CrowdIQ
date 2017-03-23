package actions.extend;

import actions.base.InspectionAndReadTableBaseAction;

public class InspectionAction extends InspectionAndReadTableBaseAction {

	//用来返回存在的问题
	private String problems;

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
	}
	
	public String execute(){	
		problems = inservice.inspect(readservice.getJSONTable_show());
		return SUCCESS;
	}
}
