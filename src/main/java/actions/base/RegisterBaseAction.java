package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.RegisterService;

public class RegisterBaseAction  extends ActionSupport{

	protected RegisterService registerService;

	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}

	
}
