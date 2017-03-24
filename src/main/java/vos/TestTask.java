package vos;

import java.util.ArrayList;

//这个类用来构建测试问题
public class TestTask {

	private String taskID;
	
	//sql语句中select目标可能不止一个，这个目标存取还是用sql解析中的原始数据。 select columns[2],headers[2]
	private ArrayList<String> sqlTargets;
	
	//该问题的描述
	private String questionDescribe;
	
	//sql语句中showing rows[2],rows[3],columns 所以String内容可能是多维数组，这里都以字符串存储
	private ArrayList<String> showing_contents;
	
	//对应的答案
	private ArrayList<String> ground_true;
	
	
}
