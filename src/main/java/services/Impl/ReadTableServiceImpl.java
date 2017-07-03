package services.Impl;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.csvreader.CsvReader;

import daos.Interface.RTableDao;
import daos.Interface.RTableListDao;
import daos.Interface.RTaskDao;
import domains.RTable;
import domains.RTableList;
import domains.RTask;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.InspectionService;
import services.Interface.ParserCrowdIQLService;
import services.Interface.ReadTableService;
import vos.JSONTableVos;

public class ReadTableServiceImpl implements ReadTableService {

	//存放转换后的json表数据
	private JSONObject jsonTable;
	//存放二维表原始数据
	private ArrayList<String[]> readList;
	private InspectionService inspectionService;
	private ParserCrowdIQLService parserCrowdIQLService;
	
	private RTableListDao rtableListDao;
	private RTableDao rtableDao;
	private RTaskDao rtaskDao;
	
	public RTableListDao getRtableListDao() {
		return rtableListDao;
	}

	public void setRtableListDao(RTableListDao rtableListDao) {
		this.rtableListDao = rtableListDao;
	}

	public RTaskDao getRtaskDao() {
		return rtaskDao;
	}

	public void setRtaskDao(RTaskDao rtaskDao) {
		this.rtaskDao = rtaskDao;
	}

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
	
	public ParserCrowdIQLService getParserCrowdIQLService() {
		return parserCrowdIQLService;
	}

	public void setParserCrowdIQLService(ParserCrowdIQLService parserCrowdIQLService) {
		this.parserCrowdIQLService = parserCrowdIQLService;
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
	public boolean tranferJSONTable(String tablename) {
		
		//用于数据转换方便映入的bean类
		JSONTableVos jsonTableVos = new JSONTableVos();
		//放入表名
		jsonTableVos.setTablename(tablename);
		
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
	public boolean readUploadTable(String userID, String tablename, String path) {
		// TODO Auto-generated method stub
		//先判断一下是否存在这么一张表
		File file = new File(path);
		
		if (file.exists()) {
			//将用户上传的表写入数据库,还有转换好的jsontable
			readList = new ArrayList<>();
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
			//获取到转换后的jsontable,这里传入的表名需要 .csv 后缀去除
			tranferJSONTable(tablename.substring(0, tablename.length()-4));
			rtableDao.save(new RTable(Integer.valueOf(userID), tablename, 0, jsonTable.toString()));
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String readDBTable(String userID, String tablename) {
		// TODO Auto-generated method stub
		//从数据库中找到对应的表
		List<RTable> rTables = rtableDao.findByIDName(userID, tablename);
		if (rTables.size()==0) {
			return null;
		}else {
			RTable rTable = rtableDao.findByIDName(userID, tablename).get(0);
			//需要这么来回折腾是为了让这个类属性jsontable存在值，这样返回的showtable才能都显示
			jsonTable = JSONObject.fromObject(rTable.getJsontable());
		}
		return getJSONTable_show().toString();
	}

	@Override
	public String showAllTable(String userID) {
		// TODO Auto-generated method stub
	
		List<RTable> rTables = rtableDao.findAllByRid(userID);
		ArrayList<String> tableInfo = new ArrayList<>();
		for(int i=0;i<rTables.size();i++){
			Integer state = 1;
			RTable rTable = rTables.get(i);
			String tablename = rTable.getTable_name();
			List<RTask> rTasks = rtaskDao.showTaskByTablename(tablename);
			//如果该表格没有对应的任务或者对应的表格都是完成，则是0
			if (rTasks.size() == 0) {
				state = 0;
			}else {
				for (int j = 0; j < rTasks.size(); j++) {
					RTask rTask = rTasks.get(j);
					//雇主任务只有为2 完成，4超期，才能让雇主下载对应表
					if (!(rTask.getState()==2||rTask.getState()==4)) {
						state = 0;
						break;
					}
				}	
			}
			rTable.setAvailable(state);
			rtableDao.update(rTable);
			tableInfo.add(tablename+":"+state);
		}
		return JSONArray.fromObject(tableInfo).toString();
	}

	@Override
	public boolean downloadTable(String tableID, String userID, String path) {
		// TODO Auto-generated method stub
		//先要判断数据库中对应表格的状态，理论上没有问题都是1
		RTable rTable = rtableDao.findByIDName(userID, tableID).get(0);
		JSONObject jsontable = JSONObject.fromObject(rTable.getJsontable());
		if (rTable.getAvailable()==1) {
			//先要整合对应任务的答案，写入到表格，这里需要判断对应任务sqlTarget是否为 header，columns，rows.
			//这三者需要写入原表，其余的只需要显示答案即可
			List<RTask> rTasks = rtaskDao.showTaskByTablename(tableID);
			for (int i = 0; i < rTasks.size(); i++) {
				RTask rtask = rTasks.get(i);
				String content = rtask.getContent();
				//判断是否为 header，columns，rows
				//不是则不用管，若是则需要获取finalAnswer，注意顺序对应
				JSONArray sqlTargets = JSONObject.fromObject(content).getJSONArray("sqlTargets");
				JSONArray finalAnswers = JSONObject.fromObject(content).getJSONArray("finalAnswers");
				
				for (int j = 0; j < sqlTargets.size(); j++) {
					String target = sqlTargets.getString(j);
					String answer;
					if (finalAnswers.size()>j) {
						answer = finalAnswers.getString(j);	
					}else{
						answer = "";
					}
					jsontable = parserCrowdIQLService.fillContent(target.split("\\.")[1], answer, jsontable);
					rTable.setJsontable(jsontable.toString());
				}
			}
			rtableDao.update(rTable);
			//全部完成之后需要将jsontable写入到二维表中
			parserCrowdIQLService.returnTable(jsontable, tableID, path);
			return true;
		}
		return false;
	}
	@Override
	public boolean uploadTableList(String userID, String tablelist, String path) {
		// TODO Auto-generated method stub
		//先判断一下是否存在这么文件
		File file = new File(path);
					
			if (file.exists()) {
				rtableListDao.save(new RTableList(Integer.valueOf(userID), tablelist));
				return true;
			}   
		
		return false;
	}

	@Override
	public boolean deleteTable(String userID, String tablename, String path) {
		// TODO Auto-generated method stub
		RTable rTable = rtableDao.findByIDName(userID, tablename).get(0);
		rtableDao.delete(rTable);
		//这里缺少本地删除机制;
		File file = new File(path+"/"+tablename);
		if (file.exists()) {
			file.delete();	
			return true;
		}else{
			return false;
		}
	}

}
