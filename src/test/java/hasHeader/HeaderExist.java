package hasHeader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.csvreader.CsvReader;

import domains.RTask;
import net.sf.json.JSONObject;

public class HeaderExist {

	public String[] readFirstLine(String path){
		
		try {
			CsvReader reader = new CsvReader(path,',',Charset.forName("utf-8"));
			try {
				if(reader.readRecord()){
					return reader.getValues();				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> findInProBase(String concept, String ProbasePath){
		concept = concept.toLowerCase();
		ArrayList<String> results = new ArrayList<>();
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(ProbasePath);
			Scanner scanner = new Scanner(inputStream);
			
			while (scanner.hasNextLine()) {
				String readline = scanner.nextLine();
				String secondValue = readline.split("\\t")[1];
				if (!secondValue.equals("absence")) {
					if (secondValue.equals(concept)) {
						//这一行需要保留下来
						results.add(readline);
					}
				}
						
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	public Map<String, ArrayList<String>> mergeConcept(ArrayList<String> Allresults){
		//把只有一个属性对应的概念的行忽略，且合并同一个概念的其他属性
		//利用map实现去重，即概念相同的，result放在一快，然后array长度为<2的则去除
		Map<String, ArrayList<String>> map = new HashMap<>();
		for (int i = 0; i < Allresults.size(); i++) {
			String concept = Allresults.get(i).split("\\t")[0];
			if (map.containsKey(concept)) {
				ArrayList<String> oldResult = map.get(concept);
				oldResult.add(Allresults.get(i));
				ArrayList<String> newResult = oldResult;
				map.replace(concept, newResult);
			}else {
				ArrayList<String> result = new ArrayList<>();
				result.add(Allresults.get(i));
				map.put(concept, result);
			}
		}

		ArrayList<String> removeKey = new ArrayList<>();
		//便利map，长度小于2的则去除
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (map.get(key).size()<2) {
				removeKey.add(key);
			}
		}
		for (int i = 0; i < removeKey.size(); i++) {
			map.remove(removeKey.get(i));
		}
		return map;
	}
	
	public ArrayList<String> calculate(Map<String, ArrayList<String>> map){
		// String格式C1，{ actor , date of birth , lifespan}，0.0248
		ArrayList<String> finalResults = new ArrayList<>();
		
		for(String key : map.keySet()){
			ArrayList<String> result = map.get(key);
			
			double down = Double.valueOf(result.get(0).split("\\t")[3]);;
			double[] up = new double[result.size()];
			
			String attribute = null;
			for (int i = 0; i < result.size(); i++) {
				//获取关键元素
				up[i] = Double.valueOf(result.get(i).split("\\t")[2]);
				
				if (i==0) {
					attribute = "{"+result.get(i).split("\\t")[1];
				}else {
					if (i==result.size()-1) {
						attribute = attribute+";"+result.get(i).split("\\t")[1]+"}";
					}else {
						attribute = attribute+";"+result.get(i).split("\\t")[1];
					}	
				}
			}
			//组装格式
			double value = down;
			for (int i = 0; i < up.length; i++) {
				value = value*(up[i]/down);
			}
			finalResults.add(key+","+attribute+","+value);
		}
		
		return finalResults;
	}
	
	public double getValue(ArrayList<String> tuples){
		double result = 0;
		for (int i = 0; i < tuples.size(); i++) {
			System.out.println(tuples.get(i));
			result = result + Double.valueOf(tuples.get(i).split(",")[2]);
		}
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		HeaderExist hasHeader = new HeaderExist();
//		String[] concepts = hasHeader.readFirstLine("g:\\9.csv");
//		
//		ArrayList<String> Allresults = new ArrayList<>();
//		for (int i = 0; i < concepts.length; i++) {
//			String concept = concepts[i];			
//			ArrayList<String> result = hasHeader.findInProBase(concept, "E:\\Probase\\CrowdIQ\\ConceptAndAttribute.txt");
//			Allresults.addAll(result);
//		}
//		System.out.println(hasHeader.getValue(hasHeader.calculate(hasHeader.mergeConcept(Allresults))));
//			
//		JSONObject jObject = new JSONObject();
//		jObject.put("d", 0);
//		jObject.put("e", "hhhh");
//		RTask rTask = new RTask(jObject.toString(), null, null, null, null, null, null, null, null, null, null, null);
//		System.out.println(JSONObject.fromObject(rTask).toString());
		System.out.println(Timestamp.valueOf("2013-12-3 14:00"));
	}
}
