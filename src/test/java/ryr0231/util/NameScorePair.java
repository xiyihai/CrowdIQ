package ryr0231.util;

import java.util.List;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2013年12月4日 下午9:58:47 
 * 类说明 
 */
public class NameScorePair{
	 String name;
	 Double score;
	
	 public NameScorePair(String name,Double score){
		this.name=name;
		this.score=score;
	}
	
	public Double score(){
		return this.score;
	}

	public String name()
	{
		return this.name;
	}
}

class EntityInfo{
public String name;
public int totalFrequency;
public double probability;

public List<EntityFrequencyScore> SynonymInfo;
}
 
 
 class Logarithm {	
	 //static double=math.log(10);
	 static double  log(double value, double base){
		 return Math.log(value) / Math.log(base);
	 }
}

	