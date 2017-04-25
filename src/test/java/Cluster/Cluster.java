package Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Cluster {
	
	public static int cluster_size; //聚簇数
	public static Point[] clusterCenter;  //存放聚类中心
    public static Point[] clusterPoint;	 //存放待聚类的点
	public static Map<Point,ArrayList<Point>> clusterResult;  //存放最后的聚类结果
    
	//聚类算法
	public void startCluster()
	{
		Cluster cluster = new Cluster(); 
		clusterResult = new HashMap<Point,ArrayList<Point>>();
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		for(int i=0;i<clusterPoint.length;i++)
		{
			Point point = clusterPoint[i];
			double distance[] = new double[clusterCenter.length];
			for(int j=0;j<distance.length;j++)
			{
				distance[j] = cluster.averageDistance(point, clusterCenter[j]);
			}
			int index = cluster.getMaxIndex(distance); 
			result.put(i, index);
		}
		for(int k=0;k<clusterCenter.length;k++)
		{
			Point center = clusterCenter[k];
			ArrayList<Point> centerList = new ArrayList<Point>();
			for(int a=0;a<result.size();a++)
			{
				int index = result.get(a);
				if(index == k)
				{
					centerList.add(clusterPoint[a]);
				}
			}
			clusterResult.put(center, centerList);
		}
		cluster.printResult();
	}
	
	//计算新加入的点和聚类中心点的平均距离
	public double averageDistance(Point point,Point center)
	{
		Kmeans kmeans = new Kmeans();
		double distance = 0,dis_2=0;
		double dis_1 = kmeans.getDistance(point, center);
		ArrayList<Point> point_list = clusterResult.get(center);
		if(point_list != null && point_list.size() > 0)
		{
			double dis[] = new double[point_list.size()];
			double sum = 0;
			for(int i=0;i<point_list.size();i++)
			{
				Point temp_point = point_list.get(i);
				dis[i] = kmeans.getDistance(temp_point, point);
				sum += dis[i];
			}
			dis_2 = sum/dis.length;
		}
		distance = (dis_1 + dis_2)/2;
		return distance;
	}
	
	//打印最后的聚类结果
	public void printResult()
	{
		Iterator iterator = clusterResult.entrySet().iterator();
		int number = 0;
		while(iterator.hasNext())
		{
			Map.Entry entry = (Entry) iterator.next();
			Point point = (Point) entry.getKey();
//			System.out.println("第"+number+"个聚类结果：");
			ArrayList<String> center = point.getData();
			for(int i=0;i<center.size();i++)
			{
//				System.out.print(center.get(i)+"  ");
			}
//			System.out.println("");
			ArrayList<Point> points = clusterResult.get(point);
			for(int j=0;j<points.size();j++)
			{
				Point temp_point = points.get(j);
				ArrayList<String> point_data = temp_point.getData();
				for(int k=0;k<point_data.size();k++)
				{
//					System.out.print(point_data.get(k)+"  ");
				}
//				System.out.println("");
			}
			number++;
		}
	}
	
	//返回数组中最大值所在的序号
	public int getMaxIndex(double data[])
	{
		int index = 0;
		double max = data[0];
		for(int i=1;i<data.length;i++)
		{
			if(data[i] > max)
			{
				max = data[i];
			}
		}
		for(int j=0;j<data.length;j++)
		{
			if(data[j] == max)
			{
				index=j;
				break;
			}
		}
		return index;
	}
	
	//计算两个元组之间的Jaccard相似度
	public double getDistance(Point dpA, Point dpB) {
		double distance = 0;
		ArrayList<String> dimA = dpA.getData();
		ArrayList<String> dimB = dpB.getData();
		ArrayList<String> join = new ArrayList<String>();
		ArrayList<String> union = new ArrayList<String>();
		for(int i=0;i<dimA.size();i++)
		{
			String tempA = dimA.get(i);
			String tempB = dimB.get(i);
			if(tempA.equals(tempB))
			{
				join.add(tempA);
				union.add(tempA);
			}else{
				union.add(tempA);
				union.add(tempB);
			}
		}
		distance = (double)join.size()/(double)union.size();
		return distance;
	}

}


