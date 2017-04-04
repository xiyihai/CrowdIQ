package actions.extend.register;

import actions.base.RegisterBaseAction;

public class RegisterAction extends RegisterBaseAction {

	//用于接受前端注册的信息(工人和雇主共用)
	private String informationJSON;

	public String getInformationJSON() {
		return informationJSON;
	}

	public void setInformationJSON(String informationJSON) {
		this.informationJSON = informationJSON;
	}
	
	public String execute(){
		
		if (registerService.register(informationJSON)) {
			return SUCCESS;	
		}else {
			return ERROR;
		}
		
	}
	
}
