package Algorithm_Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;


import com.csvreader.CsvReader;

import crowdiq.impl.EntityAugment;
import net.sf.json.JSONArray;


public class Exist {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			ArrayList<String[]> readList = new ArrayList<>();
			CsvReader reader = new CsvReader("g:\\querytable-book.csv",',',Charset.forName("utf-8"));
		    while(reader.readRecord()){ //逐行读入数据      
		        readList.add(reader.getValues());  
		    }              
		    reader.close();
		    JSONArray entity = new JSONArray();
		    for (int i = 1; i < readList.size(); i++) {
				entity.add(readList.get(i)[0]);
			}
		    
		    EntityAugment entityAugment = new EntityAugment();
		    String[] data = new String[4];
		    data[0] = "author";
		    data[1] = "year";
		    data[2] = entity.toString();
		    data[3] = "E:\\Probase\\CrowdIQ\\DataSet\\datasource";
		    
		    System.out.println(new Date());
		    ArrayList<String> result = entityAugment.process(data, 1);
		    for (int r = 0; r < result.size(); r++) {
				System.out.println(result.get(r));
		    }
		    System.out.println(new Date());
		}
}
