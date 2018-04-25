package csy4367.method;

import java.util.ArrayList;

/** 
 * @author polaris  
 * @version ����ʱ�䣺2015-12-14 ����5:56:24 
 * ���ڼ���
 */
public class RegularInspect {
	
	//ִ��RemovalPartContent����
	//iArrayListΪԭ������һ�е�����
	//��ÿһ���������ݽ��У�ȥ���ַ����еĴ�С�������ţ��Լ��»��߻��ɿո񡢷ֺŲ𿪡�
	public static ArrayList<String> RemovalPartContent(ArrayList<String> iArrayList) {
		ArrayList<String> arrayList = new ArrayList<String>();
			
		for (String iString : iArrayList) {
				
			// ��������ʽȥ�����ַ����е�С���ţ������ţ������š�
			iString = iString.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|��.*?��", "");
				
			// ���»��߻��ɿո�
			iString = iString.replaceAll("_", " ");
			String[] spiltStrings;
				
			// �˴����ַ������Էֺ�Ϊ��λ���ָ����Ϊʵ�塣
			if (!iString.contains(";|,")) {
				if (!iString.isEmpty()) {
					while (iString.charAt(0) == ' ') {
						iString = iString.replace(" ", "");
					}
					if (!arrayList.contains(iArrayList)) {
						arrayList.add(iString);
					}
				}
			} else {
				spiltStrings = iString.split(";|,");

				for (String isString : spiltStrings) {
					// ���������ظ��������
					if (!isString.isEmpty()) {
						if (!arrayList.contains(iArrayList)) {
							while (isString.charAt(0) == ' ') {
								isString = isString.replaceFirst(" ", "");
							}
							arrayList.add(isString);
						}
					}
				}
			}

		}
		return arrayList;
	}

		
		
	//ִ��RegularInspect���ListISData����
	//�ж��Ƿ�������
	public static double ListISData(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		
		for (String iString : iArrayList) {
			if (isNum(iString)) {
				count_num++;
			}
		}
		possible_num = count_num * 1.0 / total;
		return possible_num;
	}

	
	//ִ��RegularInspect���ListISDate����
	//�ж��Ƿ�������
	public static double ListISDate(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (isDate(iString)) {
				count_num++;
			}
		}
		possible_num = count_num * 1.0 / total;
		return possible_num;

	}

	
	//ִ��RegularInspect���ListIsSex����
	//�ж��Ƿ����Ա�
	public static double ListIsSex(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString.equals("F") || iString.equals("M")) {
				count_num++;
			}
		}
		possible_num = count_num * 1.0 / total;
		return possible_num;
	}

	
	//ִ��RegularInspect���ListISWeb����
	//�ж��Ƿ�����ַ
	//�ж��Ƿ�������
	public static double ListISWeb(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString.contains("www.")) {
				count_num++;
			}

		}
		possible_num = count_num * 1.0 / total;
		return possible_num;
	}

	
	// �ж��Ƿ�Ϊ����
	public static double ListISMail(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString
					.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
				count_num++;
			}

		}
		possible_num = count_num * 1.0 / total;
		return possible_num;
	}

	
	//ִ��RegularInspect���ListIsLength����
	//�ж����ݵ��ַ�������
	public static double ListIsLength(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString.length() > 50) {
				count_num++;
			}
		}
		possible_num = count_num * 1.0 / total;
		return possible_num;
	}

	
	
	
	// �ж��Ƿ������ڣ����������1990-12-12,1990-1991��1990/12/13,1990/12/13-1990/12/12������
	public static boolean isDate(String str) {
		if (str.matches("\\d{4}-*\\d*-*\\d*")) {
			return true;
		} else if (str.matches("\\d{4}/*\\d*/*\\d*-*\\d{4}/*\\d*/*\\d*")) {
			return true;
		} else if ((str.matches("((January)|(February)|(March)|(April)|(May)|(June)|(July)|"
				+ "(August)|(September)|(October)|(November)|(December))\\s+"
				+ "[0-9]{1,2}.\\s*[0-9]{4}"))
){
			return true;
		}
	//		else if(str.matches("^(?!.*(ja)).*$")){
	//			return true;
	else
		{
			return false;
		}

	}

	public  static boolean isNum(String str) {
		if(str.matches("[-+]?([0-9]+)(([.]([0-9]+))?|([.]([0-9]+))?)\\D*"))
			return true;
		else if(str.matches(".*\\d+.*"))
			return true;
		else
			return false;
	}
	public static boolean IsSex(String str) {
			if (str.equals("F") || str.equals("M")) {
				return true;
			}
			else if(str.equals("Female") || str.equals("Male")){
				return true;
			}
			else{
				return false;
			}
		}
	public static boolean IsWeb(String str) {
			if (str.contains("www.")) {
				return true;
			}
			else{
				return false;
			}
	}

	public static boolean IsLength(String str) {
		if (str.length() > 50) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean ISMail(String str) {
			if (str
					.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
			return true;
			}
			else{
				return false;
				}
	}

}
