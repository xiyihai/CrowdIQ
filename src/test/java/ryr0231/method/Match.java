package ryr0231.method;

/**
 * 
 * @author Administrator
 * 最长公共子序列算法
 */

interface MatchHander{
	boolean compare(int a,int b);
}

public  class Match {

	public static boolean match(String src, String dest, MatchHander hander){
		double percent = 0.8;
		char[] csrc = src.toCharArray();
		char[] cdest = dest.toCharArray();
		//假如长度差多于3个字符 可以直接退出
		if(Math.abs(csrc.length-cdest.length) < 2){ 
			double score = 0;
			score = cal(csrc, 0, cdest, 0, hander);
			int max = csrc.length > cdest.length ? csrc.length : cdest.length;
			return score / max > percent;
		} else {
 			return false;
		}
	}

	public static boolean match(String src, String dest){
		return match(src, dest, new MatchHander(){

			@Override
			public boolean compare(int a, int b) {
				return a == b;
			}
		});
	}

	private static int cal(char[] csrc, int i, char[] cdest, int j, MatchHander hander) {
		int score = 0;
		if(i >= csrc.length || j >= cdest.length){
			return 0;
		}
		
		boolean ismatch = hander.compare(csrc[i], cdest[j]);
		if(ismatch){
			score ++;
			if(i+1 < csrc.length && j+1 < cdest.length){
				score +=  cal(csrc, i+1, cdest, j+1, hander) ;
			}
		} else {
			int temp1 = 0;
			int temp2 = 0;
			int temp3 = 0;
			temp1 +=  cal(csrc, i, cdest, j+1, hander) ;
			temp2 +=  cal(csrc, i+1, cdest, j, hander) ;
			temp3 +=  cal(csrc, i+1, cdest, j+1, hander) ;
			int temp4 = Math.max(temp1, temp2);
			score += Math.max(temp3, temp4);
		}
		return score;
	}
	public static void main(String[] args) {
		
	}
}
