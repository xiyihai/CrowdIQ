package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.InspectionService;
import services.Interface.ReadTableService;

public class InspectionAndReadTableBaseAction extends ActionSupport{
	
	//依赖注入InspectionService，方便子类调用
	protected InspectionService inService;
	//需要read给出的jsontable_show来质量检测
	protected ReadTableService readService;

	public void setInService(InspectionService inService) {
		this.inService = inService;
	}
	
	public void setReadService(ReadTableService readService) {
		this.readService = readService;
	}


	

}
