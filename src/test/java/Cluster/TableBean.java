package Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBean {
	
	private List<String> schema;
	private List<String> flags;
	private List<List<String>> content;
	private String title;
	private String id;
	private String primary;
	private String path;
	private int rowNum;
	private Map<String, Double> priDegrees;
	private Map<String, Double> priDegrees1;
	private Map<String, Double> priDegrees0;
	private int MultiDependent;
	public String getPath() {
		return path;
	}

	public String getPrimary(){
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getLine() {
		return line;
	}
	public int getcolNum() {
		return content.size();
	}
	public int getrowNum() {
		int colNum=0;
		for(int i=0;i<content.size();i++)
		{
			if(content.get(i).size()>colNum)
				colNum=content.get(i).size();
		}
		return colNum;
	}
	public void setLine(int line) {
		this.line = line;
	}
	private int line;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getSchema() {
		return schema;
	}
	public void setSchema(List<String> schema) {
		this.schema = schema;
	}
	public List<String> getFlags() {
		return flags;
	}
	public void setFlags(List<String> flags) {
		this.flags = flags;
	}
	public List<List<String>> getContent() {
		return content;
	}
	public void setContent(List<List<String>> content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	private String pageTitle;
	
	public void setPriDegrees(Map<String, Double> priDegrees) {
		// TODO Auto-generated method stub
		this.priDegrees=priDegrees;
	}
	public void setPriDegrees0(Map<String, Double> priDegrees0) {
		// TODO Auto-generated method stub
		this.priDegrees0=priDegrees0;
	}
	public void setRowNum(int rowNum) {
		// TODO Auto-generated method stub
		this.rowNum=rowNum;
	}

	public Map<String, Double> getPriDegrees0() {
		// TODO Auto-generated method stub
		return priDegrees0;
	}
	public Map<String, Double> getPriDegrees() {
		// TODO Auto-generated method stub
		return priDegrees;
	}

	public void setPriDegrees1(Map<String, Double> priDegrees1) {
		// TODO Auto-generated method stub
		this.priDegrees1=priDegrees1;
	}

	public Map<String, Double> getPriDegrees1() {
		// TODO Auto-generated method stub
		return priDegrees1;
	}

	

}
