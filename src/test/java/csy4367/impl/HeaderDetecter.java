package csy4367.impl;

import java.io.IOException;
import java.util.ArrayList;


import csy4367.method.HeaderDetect;
import csy4367.method.HeaderExist;

public class HeaderDetecter {


	public boolean hasHeader(ArrayList<String[]> tableData, String probasePath){	
		
		HeaderExist headerExist = new HeaderExist();
	
		String[] headers = tableData.get(0);
	
		ArrayList<String> Allresults = new ArrayList<>();	
		for (int i = 0; i < headers.length; i++) {
			ArrayList<String> result = headerExist.findInProBase(headers[i], probasePath);
			Allresults.addAll(result);
		}	
		double result1 = headerExist.getValue(headerExist.calculate(headerExist.mergeConcept(Allresults)));

		HeaderDetect SR = new HeaderDetect();
			
		//组成按列读取
		String[][] tranferData = new String[tableData.get(0).length][tableData.size()];
		for (int i = 0; i < tableData.size(); i++) {
			String[] dStrings = tableData.get(i);
			for (int j = 0; j < dStrings.length; j++) {
				tranferData[j][i] = dStrings[j];
			}
		}
		
		ArrayList<ArrayList<String>> data = new ArrayList<>();
		for (int i = 0; i < tranferData.length; i++) {
			ArrayList<String> temp = new ArrayList<>();
			for (int j = 0; j < tranferData[i].length; j++) {
				temp.add(tranferData[i][j]);
//				System.out.print(tranferData[i][j]+",");
			}
//			System.out.println();
			data.add(temp);
		}
		double result2 = 0;
		try {
			result2 = SR.handle(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if((result1 + result2) > 0.5){
			return true;
		}
		else{
			return false;
		}
		
	}
	
//	public static void main(String[] args){
//		ArrayList<String[]> readList = new ArrayList<>();
//		try {
//			CsvReader reader = new CsvReader("g:\\3.csv",',',Charset.forName("utf-8"));
//		    while(reader.readRecord()){ //逐行读入数据      
//		        readList.add(reader.getValues());  
//		    }              
//		    reader.close();
//		    
//		}catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(new HeaderDetecter().hasHeader(readList, "E:\\Probase\\CrowdIQ\\ConceptAndAttribute.txt"));
//	}
}
