package Algorithm_Process;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class AlgorithmIn {

	//这个函数用于找到对应的jar包，并运行
	//这里假设返回的都是一维数组，都用来提供候选答案
	public ArrayList<String> find(String algorithm_name, String result){
		ArrayList<String> items = null;
		try {
			Class<?> algorithm_class = Class.forName(algorithm_name);
				try {
					Method method = algorithm_class.getMethod("process",String.class);
					try {
						//获取返回值，这里都是以字符串返回，解析工作交给后面
						items = (ArrayList<String>) method.invoke(algorithm_class.newInstance(), result);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("未找到该算法");
		}
		return items;
	}
}
