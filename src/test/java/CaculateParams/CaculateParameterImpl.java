package CaculateParams;

public class CaculateParameterImpl implements CaculateParameter{

	/**
	 * 计算困难度
	 */
	private double dif;
	public double getDiffDegree(Integer[] selects, Integer[] showing, Double top_k) {
		// TODO Auto-generated method stub
		double dif_topk;
		//int local1=1;
		// 划分空格数量等级
		int level0=0,level1=0;
		//level0：select的数量对于任务难度的影响；level1：select中空格的个数对于任务难度的影响；
		int sum=0;
		int lens=selects.length;
		if(lens==0){
			level0=0;
		}else if(lens==1){
			level0=1;
		}else if(lens==2){
			level0=2;
		}else if(lens<=5){
			level0=5;
		}else 
			level0=15;
		
		for(int i=0;i<selects.length;i++){
			sum=sum+selects[i];
			}
		if(sum<5){
			level1=1;
			}
		else if(sum<15){
			level1=5;	
			}
		else if(sum<50){
			level1=10;
		}else if(sum<100){
			level1=25;
		}else{
			level1=50;
		}
		dif_topk=10*(1-top_k);
		dif=0.4*level0/23+0.4*level1/71+0.2*dif_topk;
		return dif;
	}

	@Override
	public double getEachReward(Integer[] selects, Integer[] showing, Double top_k) {

		/**
		 * 计算任务单位工资
		 */
		double punit=1,dfloat=0.0,wbase=0.0;
		if(dif>=0&&dif<=0.5){
			dfloat=0.6;
		}else if(dif>0.5&&dif<=0.8){
			dfloat=1;
		}else if(dif>0.8&&dif<=1.0){
			dfloat=1.2;
		}else{
			System.out.println("the HIT got something wrong");
		}
		wbase=punit*dfloat;
		return wbase;
	}

	@Override
	public int getWorkNumber(String[] workerc) {

		/**
		 * 计算工人个数
		 */
		int n=0;
		double u=0.65,c=0.8;
		n=(int) (2*((0-Math.log(1-c))/(4*(u-0.5)*(u-0.5))))+1;
		n=getOptimizeWokerNum(n,c,u);
		System.out.println(n);
		return n;
	}
	//优化工人个数
		@SuppressWarnings("unused")
		private int getOptimizeWokerNum(int num,double c,double u){
			int s=1;
			while(s<num){
				int m=(int)(2*((s+num)/4))-1;
				//System.out.println(m);
				int E=computeExpectedProb(m,u);
				//System.out.println(E);
				if(E>=c){
					num=m;
					//System.out.println(E);
				}
				else{
					s=m+2;
				}
				//System.out.println(s);
				//System.out.println(num);
			}
			return num;	
		}
		private int computeExpectedProb(int m,double u){
			double E=0,dit=Math.pow(u,m);
			double u1=0;
			for(int i=0;i<(m/2+1);i++){
				E=E+dit;
				u1=((1-u)*i)/(u*(m-i+1));
				//System.out.println(u1);
				dit=dit*u1;
				//System.out.println(dit);
				//System.out.println(E);
			}
			//System.out.println(E);
			return (int) E;
			
		}

	
}
