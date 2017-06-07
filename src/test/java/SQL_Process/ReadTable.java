package SQL_Process;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.csvreader.CsvReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ReadTable {

	public JSONObject jsonTable;
	public JSONObject jsonTable_show;
	private TableBean table = new TableBean();
	private ArrayList<String[]> readList = new ArrayList<>(); //用来保存读取的数据

	
	
	public ArrayList<String[]> getReadList() {
		return readList;
	}

	public void setReadList(ArrayList<String[]> readList) {
		this.readList = readList;
	}

	//判断是否存在表头，会影响jsontable的形成
	private boolean hasHeader(){
		return true;
	}
	
	//读取csv格式的table，输入参数 path为路径
	public void readTable(String path){
		try {
			CsvReader reader = new CsvReader(path,',',Charset.forName("utf-8"));
		    while(reader.readRecord()){ //逐行读入数据      
		        readList.add(reader.getValues());  
		    }              
		    reader.close();
		    
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//将读取到的数据放入JSONTable
	public void tranfer(String path){
		readTable(path);
		
		if (readList.size()>0) {
			//这里tableID是随机字符串
			table.setTablename("Country in world");
			
			//判断是否存在表头,全部内容都在readList中
			if(hasHeader()){
				
				table.setHeaders(new ArrayList<String>(Arrays.asList(readList.get(0))));
				ArrayList<ArrayList<String>> data = new ArrayList<>();
				
				for(int i=1;i<readList.size();i++){
					data.add(new ArrayList<String>(Arrays.asList(readList.get(i))));	
				}
				table.setData(data);
				
			}else{
				System.out.println("不存在表头");
				
				//虽然不存在表头，但仍要以""加入，否则headers为空
				ArrayList<String> headers = new ArrayList<>();
				for(int i=0;i<readList.get(0).length;i++){
					headers.add("");	
				}
				table.setHeaders(headers);
				
				ArrayList<ArrayList<String>> data = new ArrayList<>();
				
				for(int i=0;i<readList.size()-1;i++){
					data.add(new ArrayList<String>(Arrays.asList(readList.get(i))));	
				}
				table.setData(data);
			}
		}else {
			System.out.println("空表");
		}
		
		//得到初始化的jsonTable
		jsonTable=JSONObject.fromObject(table);
	}
	
	//将jsontable做成展示的json数据,主要在于行列数据分别存一遍，这一步需要建立在jsontable基础上
	//jsontable有改动，则该showtable也要随之变化
	public void showJSONTable(){
		jsonTable_show= JSONObject.fromObject(jsonTable.toString());
		
		jsonTable_show.put("rows", jsonTable.get("data"));
		
		//下面需要组装列数据
		//这里只能通过二维数组来实现		
		JSONArray data = (JSONArray) jsonTable.get("data");
		String[][] columns = new String[data.getJSONArray(0).size()][data.size()];
		
		for (int i = 0; i < data.size(); i++) {
			JSONArray rdata = data.getJSONArray(i);
			for(int j=0;j<rdata.size();j++){
				columns[j][i] = (String) rdata.get(j);
			}
		}
		
		//数组可以直接当做JSONArray放在JSONObject中
		jsonTable_show.put("columns", columns);
		jsonTable_show.remove("data");
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadTable table = new ReadTable();
		table.tranfer("g:/Winners.csv");
		table.showJSONTable();
		//System.out.println(table.jsonTable_show.toString());
		System.out.println(table.jsonTable.toString());
	}

}
