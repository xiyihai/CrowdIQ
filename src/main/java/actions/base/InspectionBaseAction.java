package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.InspectionService;

public class InspectionBaseAction extends ActionSupport{
	
	//依赖注入InspectionService，方便子类调用
	protected InspectionService inservice;

	public void setInservice(InspectionService inservice) {
		this.inservice = inservice;
	}

}
