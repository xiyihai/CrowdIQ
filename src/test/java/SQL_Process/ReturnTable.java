package SQL_Process;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//把搞好的jsontable重新转换成table
public class ReturnTable {

	public boolean retranfer(JSONObject jsontable,String path) throws IOException{
		 String csvWriteFile = path;
	     CsvWriter writer = new CsvWriter(csvWriteFile, ',', Charset.forName("utf-8"));  
	     
	     ArrayList<String[]> writelist = new ArrayList<>();
	     
	     //先放入表头
	     int length = ((JSONArray)jsontable.get("headers")).size();
	     String[] headers = new String[length];
	     ((JSONArray)jsontable.get("headers")).toArray(headers);
	     
	     writelist.add(headers);
	     
	     //再放入row值
	     int length_row = ((JSONArray)jsontable.get("data")).size();
	     for(int i=0;i<length_row;i++){
	    	 int length_column = ((JSONArray)((JSONArray)jsontable.get("data")).get(i)).size();
	    	 String[] rows = new String[length_column];
	    	 ((JSONArray)((JSONArray)jsontable.get("data")).get(i)).toArray(rows);
	    	 writelist.add(rows);
	     }
	     
	     for(int row=0;row<writelist.size();row++){
	       	writer.writeRecord(writelist.get(row));
		 }
		 writer.close();

	     System.out.println("已存储");
	     
	     return true;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
