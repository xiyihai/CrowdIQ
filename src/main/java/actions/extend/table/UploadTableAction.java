package actions.extend.table;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.struts2.ServletActionContext;

import actions.base.InspectionAndReadTableBaseAction;

public class UploadTableAction extends InspectionAndReadTableBaseAction {

	private File file;
	private String fileFileName;
	private String fileFileContentType;
	private String uploadfile;
	private String uploadfileFileName;
	private String uploadfileFileContentType;
	
	private String message = "success";

	public File getFile() {
		return file;
	}

	public String getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(String uploadfile) {
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

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileFileContentType() {
		return fileFileContentType;
	}

	public void setFileFileContentType(String fileFileContentType) {
		this.fileFileContentType = fileFileContentType;
	}
	
	public String execute(){	
	
		System.out.println(222);
		String path = ServletActionContext.getRequest().getRealPath("/WEB-INF/uploadTables");

        try {
            File f = this.getFile();
            if(!this.getFileFileName().endsWith(".csv")){
                message="对不起,你上传的文件格式不允许!!!";
                return ERROR;
            }
            FileInputStream inputStream = new FileInputStream(f);
            FileOutputStream outputStream = new FileOutputStream(path + "/"+ this.getFileFileName());
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            inputStream.close();
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            message = "对不起,文件上传失败了!!!!";
        }
        return SUCCESS;
    }	
	
}
