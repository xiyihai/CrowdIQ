package Algorithm_Process;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.EnableLoadTimeWeaving;

import net.sf.json.JSONArray;


public class Exist {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	JSONArray jsonArray = new JSONArray();
	jsonArray.add("ff");
	jsonArray.add("gg");
	String[] strings = new String[jsonArray.size()];
	
	jsonArray.toArray(strings);
	System.out.println(strings[0]);
	
	}

}
