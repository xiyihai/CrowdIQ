package actions.extend.register;

import actions.base.RegisterBaseAction;

public class LoginAction extends RegisterBaseAction {

	//用于接受前端的邮箱，密码，和flag表示类别
	private String informationJSON;

	//这个ID用于在前端显示
	private String showinformation;

	public String getInformationJSON() {
		return informationJSON;
	}

	public void setInformationJSON(String informationJSON) {
		this.informationJSON = informationJSON;
	}
	
	public String getShowinformation() {
		return showinformation;
	}

	public void setShowinformation(String showinformation) {
		this.showinformation = showinformation;
	}

	public String execute(){
		showinformation = registerService.login(informationJSON);
		if (showinformation!=null) {
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
