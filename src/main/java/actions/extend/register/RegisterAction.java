package actions.extend.register;

import actions.base.RegisterBaseAction;

public class RegisterAction extends RegisterBaseAction {

	//用于接受前端注册的信息
	private String informationJSON;

	public String getInformationJSON() {
		return informationJSON;
	}

	public void setInformationJSON(String informationJSON) {
		this.informationJSON = informationJSON;
	}
	
	public String execute(){
		registerService.register(informationJSON);
		return SUCCESS;
	}
	
}
