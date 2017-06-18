package FunctionsSupport;

import java.util.ArrayList;

import net.sf.json.JSONArray;

public class TranferAnswer {

	static public String testtranfer(String wanswer, String truth, JSONArray items){
		String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
		String wanswerA="";
		String truthA="";
		for (int i = 0; i < items.size(); i++) {
			if (wanswer.equals(items.get(i))) {
				wanswerA = alphabet[i];
			}
			if (truth.equals(items.get(i))) {
				truthA = alphabet[i];
			}
		}
		return wanswerA+":"+truthA+":"+items.size();
	}
	
	static public String answertranfer(String wanswer, ArrayList<String> items){
		String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
		String wanswerA = wanswer;
		for (int i = 0; i < items.size(); i++) {
			String item = items.get(i);
			if (item.contains(":")) {
				item = item.split(":")[0];
			}
			if (wanswer.equals(item)) {
				wanswerA = alphabet[i];
			}
		}
		return wanswerA;
	}
}
