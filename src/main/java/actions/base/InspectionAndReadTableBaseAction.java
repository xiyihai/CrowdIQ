package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.InspectionService;
import services.Interface.ReadTableService;

public class InspectionAndReadTableBaseAction extends ActionSupport{
	
	//依赖注入InspectionService，方便子类调用
	protected InspectionService inservice;
	//需要read给出的jsontable_show来质量检测
	protected ReadTableService readservice;

	public void setInservice(InspectionService inservice) {
		this.inservice = inservice;
	}

	public void setReadservice(ReadTableService readservice) {
		this.readservice = readservice;
	}
	

}
