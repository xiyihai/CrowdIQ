package actions.extend;

import actions.base.InspectionBaseAction;

public class InspectionAction extends InspectionBaseAction {

	//用来返回存在的问题
	private String problems;

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
	}
	
	public String execute(){
		
		problems = inservice.inspect();
		return SUCCESS;
	}
}
