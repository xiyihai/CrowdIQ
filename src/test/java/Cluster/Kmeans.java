package Cluster;


import java.util.ArrayList;
import java.util.Random;



public class Kmeans extends Cluster{
	
	//得到kmeans聚类算法的初始数据，即聚类中心和待聚点
		public void getKeansData()
		{
			Kmeans kmeans = new Kmeans();
			String data[][] = ClusterUtil.data;
			ArrayList<Integer> center_num = new ArrayList<Integer>();
			clusterCenter = new Point[cluster_size];
			clusterPoint = new Point[data.length-cluster_size];
			//通过随机的方法得到聚类中心
			for(int i=0;i<cluster_size;i++)
			{
				int row=0;
				if(i==0)
				{
					row = kmeans.randomNum(data.length);
				}
				else{
				    while(kmeans.elemExist(center_num, row))
				   {
					   row = kmeans.randomNum(data.length);
				   }
				}
				ArrayList<String> list = new ArrayList<String>();
				for(int j=0;j<data[0].length;j++)
				{
					list.add(data[row][j]);
				}
				Point point = new Point();
				point.setData(list);
				center_num.add(row);
				clusterCenter[i] = point;
			}
			
			//得到待聚类的点
			for(int i=1,j=0;i<data.length;i++)
			{
				if(!kmeans.elemExist(center_num, i))
				{
					ArrayList<String> row_data = new ArrayList<String>();
					for(int k=0;k<data[0].length;k++)
					{
						row_data.add(data[i][k]);
					}
					Point point = new Point();
					point.setData(row_data);
					clusterPoint[j] = point;
					j++;
				}
			}
		}
		
		//生成1到max之间的随机数
		public int randomNum(int max)
		{
			int num=0;
			Random random = new Random();
			num = random.nextInt(max)%(max+1)+1;
			if(num == max)
			{
				num = num - 1;
			}
			return num;
		}
		
		//判断某个元素在某个数组中是否存在
		public boolean elemExist(ArrayList<Integer> array,int elem)
		{
			boolean flag = false;
			for(int i=0;i<array.size();i++)
			{
				if(array.get(i) == elem)
				{
					flag=true;
					break;
				}
			}
			if(flag)
				return true;
			else
				return false;
		}

}


