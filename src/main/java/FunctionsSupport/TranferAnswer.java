package FunctionsSupport;

import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.base.Array;

import net.sf.json.JSONArray;

public class TranferAnswer {

	static public String testtranfer(String wanswer, String truth, JSONArray items){
		String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
		String wanswerA="";
		String truthA="";
		for (int i = 0; i < items.size(); i++) {
			if (wanswer.equals(items.getString(i))) {
				wanswerA = alphabet[i];
			}
			if (truth.equals(items.getString(i))) {
				truthA = alphabet[i];
			}
		}
		return wanswerA+":"+truthA+":"+items.size();
	}
	
	static public String answertranfer(String wanswer, JSONArray items){
		String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
		String wanswerA = wanswer;
		for (int i = 0; i < items.size(); i++) {
			String item = items.getString(i);
			if (item.contains(":")) {
				item = item.split(":")[0];
			}
			if (wanswer.equals(item)) {
				wanswerA = alphabet[i];
			}
		}
		return wanswerA;
	}
	
	static public ArrayList<String> top_ktranfer(JSONArray items){
		String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
		ArrayList<String> result = new ArrayList<>();
		
		for (int i = 0; i < items.size(); i++) {
			String item = items.getString(i);
			if (item.contains(":")) {
				String pert = item.split(":")[1];
				item = alphabet[i]+":"+pert;
			}else {
				item = alphabet[i];
			}
			result.add(item);
		}
		return result;
	}
	
	static public String resulttranfer(String result, JSONArray candidateItems){
		String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
		for (int i = 0; i < alphabet.length; i++) {
			if (result.equals(alphabet[i])) {
				if (candidateItems.getString(i).contains(":")) {
					return candidateItems.getString(i).split(":")[0];
				}else {
					return candidateItems.getString(i);
				}
			}
		}
		return result;
	}
}
