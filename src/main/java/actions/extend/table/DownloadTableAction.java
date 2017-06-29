package actions.extend.table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.struts2.ServletActionContext;

import actions.base.InspectionAndReadTableBaseAction;

public class DownloadTableAction extends InspectionAndReadTableBaseAction {

	private String userID;
	private String tablename;
	private InputStream inputStream;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public String execute(){
		
		String path = ServletActionContext.getRequest().getRealPath("/WEB-INF/uploadTables");
		File file = new File(path+"/"+tablename);
		
		if (readService.downloadTable(tablename, userID, path)) {
			try {
				this.setInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SUCCESS;			
		}else {
			return ERROR;
		}
	}
}
