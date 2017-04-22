package FunctionsSupport;

import java.util.ArrayList;

//以下都针对提供候选答案的算法，不涉及数据约简算法
//要求所有算法实现该接口
//所有算法打包成jar包，ID号作为包名 xyh0431.impl.headerrecover
//jar包中实现该接口的类名必须是小写，CrowdIQL写法： ALGORITHM(xyh0431.impl.headerrecover) 方便找到对应包
//输入参数为String[]，对应语句 on (table.rows,table.tablename,TableList(xyh9344.country))
//其中String于属性一一对应的，String本质可能是一维数组，二维数组，甚至是三维数组（TableList就是三维数组）。
//这都是由()属性决定的，这个都是由JSONArray.toString()转换成的
//输出参数是ArrayList<String>，String里面可能是["county":0.333, "nation":"0.1"],也可能是["county", "nation"]
//内部算法直接发布到项目 lib下，外部算法满足要求后上传，系统自动放到lib下，这样也能够被索引到.

public interface AlgorithmInterface {

	public ArrayList<String> process(String[] data, int top_k);
	
}
