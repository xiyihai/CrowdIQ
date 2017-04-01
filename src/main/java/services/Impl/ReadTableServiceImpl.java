package services.Impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.csvreader.CsvReader;

import daos.Interface.RTableDao;
import domains.RTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.InspectionService;
import services.Interface.ReadTableService;
import vos.JSONTableVos;

public class ReadTableServiceImpl implements ReadTableService {

	//存放转换后的json表数据
	private JSONObject jsonTable;
	//存放二维表原始数据
	private ArrayList<String[]> readList;
	private InspectionService inspectionService;

	private RTableDao rtableDao;
	
	

	public RTableDao getRtableDao() {
		return rtableDao;
	}

	public void setRtableDao(RTableDao rtableDao) {
		this.rtableDao = rtableDao;
	}

	public InspectionService getInspectionService() {
		return inspectionService;
	}

	public void setInspectionService(InspectionService inspectionService) {
		this.inspectionService = inspectionService;
	}

	@Override
	public JSONObject getJsonTable() {
		return jsonTable;
	}

	@Override
	public ArrayList<String[]> getReadList() {
		return readList;
	}

	@Override
	public boolean tranferJSONTable() {
		
		//用于数据转换方便映入的bean类
		JSONTableVos jsonTableVos = new JSONTableVos();
		
		if (readList.size()>0) {
			
			//判断是否存在表头,全部内容都在readList中
			if(inspectionService.hasHeader(readList)){
				
				jsonTableVos.setHeaders(new ArrayList<String>(Arrays.asList(readList.get(0))));
				ArrayList<ArrayList<String>> data = new ArrayList<>();
				
				for(int i=1;i<readList.size();i++){
					data.add(new ArrayList<String>(Arrays.asList(readList.get(i))));	
				}
				jsonTableVos.setData(data);
				
			}else{
				System.out.println("不存在表头");
				
				//虽然不存在表头，但仍要以""加入，否则headers为空
				ArrayList<String> headers = new ArrayList<>();
				for(int i=0;i<readList.get(0).length;i++){
					headers.add("");	
				}
				jsonTableVos.setHeaders(headers);
				
				ArrayList<ArrayList<String>> data = new ArrayList<>();
				
				for(int i=0;i<readList.size()-1;i++){
					data.add(new ArrayList<String>(Arrays.asList(readList.get(i))));	
				}
				jsonTableVos.setData(data);
			}
		}else {
			System.out.println("空表");
		}
		
		//得到初始化的jsonTable
		jsonTable=JSONObject.fromObject(jsonTableVos);
		return true;
	}
	

	@Override
	public JSONObject getJSONTable_show() {
		// TODO Auto-generated method stub
		JSONObject jsonTable_show;
		
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
		return jsonTable_show;
	}

	
	@Override
	public boolean readUploadTable(String userID) {
		// TODO Auto-generated method stub
		
		readList = new ArrayList<>();
		
		try {
			//？？？？？？？？？？？？？这边路径还需要细化
			String table_name = null;
			CsvReader reader = new CsvReader("WEB-INF/classes/Winners.csv",',',Charset.forName("utf-8"));
		    while(reader.readRecord()){ //逐行读入数据      
		        readList.add(reader.getValues());  
		    }              
		    reader.close();

			//将用户上传的表写入数据库
			rtableDao.save(new RTable(Integer.valueOf(userID), table_name));
		    
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


	@Override
	public boolean readDBTable(String userID, String tablename) {
		// TODO Auto-generated method stub
		//这里的操作应该和readUploadTable差不多
		readList = new ArrayList<>();
		try {
			//？？？？？？？？？？？？？这边路径还需要细化
			CsvReader reader = new CsvReader("WEB-INF/classes/Winners.csv",',',Charset.forName("utf-8"));
		    while(reader.readRecord()){ //逐行读入数据      
		        readList.add(reader.getValues());  
		    }              
		    reader.close();
		    
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


	@Override
	public String showAllTable(String userID) {
		// TODO Auto-generated method stub
		List<RTable> rTables = rtableDao.findAllByRid(userID);
		ArrayList<String> names = new ArrayList<>();
		for(int i=0;i<rTables.size();i++){
			names.add(rTables.get(i).getTable_name());
		}
		return JSONObject.fromObject(rTables).toString();
	}

	@Override
	public boolean downloadTable(String tableID, String userID) {
		// TODO Auto-generated method stub
		return false;
	}

}
