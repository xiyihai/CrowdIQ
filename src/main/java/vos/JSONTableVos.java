package vos;

import java.util.ArrayList;

public class JSONTableVos {
	
		private ArrayList<String> headers;
		private ArrayList<ArrayList<String>> data; //data数据按照行值排列 [[第一行],[第二行],[第三行],[第四行]]
		
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
