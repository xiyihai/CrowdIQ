package actions.extend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.struts2.ServletActionContext;

import actions.base.ParserCrowdIQLBaseAction;

public class UploadFileAction extends ParserCrowdIQLBaseAction {

	private File uploadfile;
	private String uploadfileFileName;
	private String uploadfileFileContentType;
	private String userID;
	private String filetype;
	

	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public File getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}

	public String getUploadfileFileName() {
		return uploadfileFileName;
	}

	public void setUploadfileFileName(String uploadfileFileName) {
		this.uploadfileFileName = uploadfileFileName;
	}

	public String getUploadfileFileContentType() {
		return uploadfileFileContentType;
	}

	public void setUploadfileFileContentType(String uploadfileFileContentType) {
		this.uploadfileFileContentType = uploadfileFileContentType;
	}

	public String execute(){	
	
		String path = null;
		if (filetype.equals("csv")) {
			if (!(this.getUploadfileFileName().startsWith(this.userID+"."))) {
				return ERROR;
			}
			path = ServletActionContext.getRequest().getRealPath("/WEB-INF/uploadTables");
			try {
	            File f = this.getUploadfile();
	            if((!this.getUploadfileFileName().endsWith(".csv"))){
	            	return ERROR;
	            }
	            FileInputStream inputStream = new FileInputStream(f);
	            FileOutputStream outputStream = new FileOutputStream(path + "/"+ this.getUploadfileFileName());
	            byte[] buf = new byte[1024];
	            int length = 0;
	            while ((length = inputStream.read(buf)) != -1) {
	                outputStream.write(buf, 0, length);
	            }
	            inputStream.close();
	            outputStream.flush();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ERROR;
	        }
	        if(readService.readUploadTable(userID, this.getUploadfileFileName(), path + "/"+ this.getUploadfileFileName())){
	        	return SUCCESS;
	        }else {
	        	return ERROR;
	        }
		}
		if (filetype.equals("zip")) {
			if (!(this.getUploadfileFileName().startsWith(this.userID+"."))) {
				return ERROR;
			}
			path = ServletActionContext.getRequest().getRealPath("/WEB-INF/tablelists");
			try {
	            File f = this.getUploadfile();
	            if((!this.getUploadfileFileName().endsWith(".zip"))){
	            	return ERROR;
	            }
	            FileInputStream inputStream = new FileInputStream(f);
	            FileOutputStream outputStream = new FileOutputStream(path + "/"+ this.getUploadfileFileName());
	            byte[] buf = new byte[1024];
	            int length = 0;
	            while ((length = inputStream.read(buf)) != -1) {
	                outputStream.write(buf, 0, length);
	            }
	            inputStream.close();
	            outputStream.flush();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ERROR;
	        }
	        if(readService.uploadTableList(userID, this.getUploadfileFileName(), path + "/"+ this.getUploadfileFileName())){
	        	return SUCCESS;
	        }else {
	        	return ERROR;
	        }
		}
		if (filetype.equals("jar")) {
			if (!(this.getUploadfileFileName().startsWith("w"+this.userID+"."))) {
				return ERROR;
			}
			path = ServletActionContext.getRequest().getRealPath("/WEB-INF/lib");
			try {
	            File f = this.getUploadfile();
	            if((!this.getUploadfileFileName().endsWith(".jar"))){
	            	return ERROR;
	            }
	            FileInputStream inputStream = new FileInputStream(f);
	            FileOutputStream outputStream = new FileOutputStream(path + "/"+ this.getUploadfileFileName());
	            byte[] buf = new byte[1024];
	            int length = 0;
	            while ((length = inputStream.read(buf)) != -1) {
	                outputStream.write(buf, 0, length);
	            }
	            inputStream.close();
	            outputStream.flush();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ERROR;
	        }
	        if(parserService.uploadAlgorithm(userID, this.getUploadfileFileName(), path + "/"+ this.getUploadfileFileName())){
	        	return SUCCESS;
	        }else {
	        	return ERROR;
	        }
		}
		return ERROR;
		
    }	
}
