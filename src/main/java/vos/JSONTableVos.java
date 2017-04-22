package vos;

import java.util.ArrayList;

public class JSONTableVos {
	
		//就是表格上传时的表名
		private String tablename;
		private ArrayList<String> headers;
		private ArrayList<ArrayList<String>> data; //data数据按照行值排列 [[第一行],[第二行],[第三行],[第四行]]
		
		public String getTablename() {
			return tablename;
		}
		public void setTablename(String tablename) {
			this.tablename = tablename;
		}
		public ArrayList<String> getHeaders() {
			return headers;
		}
		public void setHeaders(ArrayList<String> headers) {
			this.headers = headers;
		}
		public ArrayList<ArrayList<String>> getData() {
			return data;
		}
		public void setData(ArrayList<ArrayList<String>> data) {
			this.data = data;
		}
		
}
