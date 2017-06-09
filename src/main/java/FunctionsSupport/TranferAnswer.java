package FunctionsSupport;

import net.sf.json.JSONArray;

public class TranferAnswer {

	static public String tranfer(String wanswer, String truth, JSONArray items){
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
}
