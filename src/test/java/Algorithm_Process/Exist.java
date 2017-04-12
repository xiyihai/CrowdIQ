package Algorithm_Process;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Exist {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		File file = new File("E:/Probase/CrowdIQ/zlf0320_entity.jar");
//		
//		if (file.exists()) {
//			System.out.println(232);
//		}else {
//			System.out.println(55);
//		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		Date now=new Date();
		String deadline=dateFormat.format(now); 
		System.out.println(deadline);
	}

}
