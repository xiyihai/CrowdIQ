package Cluster;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class WordsCount {
	
	public static ArrayList<String> topWords;
	
	//寻找core属性中各单词出现的次数
	public void wordCount(String data[][], ArrayList<String> core, int top_k)
	{
		topWords = new ArrayList<String>();
		WordsCount count = new WordsCount();
		Map<String, Integer> wordsCount = new HashMap<String, Integer>(); //存放单词及其出现的次数
		ArrayList<String> exist = new ArrayList<String>(); //存放已计数过的单词
		int count_number = 0;
		for(int i=0;i<core.size();i++)
		{
			String attri = core.get(i);
			int index = 0; //保存core属性所在的列数
			for(int j=0;j<data[0].length;j++)
			{
				if(attri.equals(data[0][j]))
				{
					index = j;
					break;
				}
			}
			String content = count.unionContent(data, index);
			for(int k=1;k<data.length;k++)
			{
				ArrayList<String> words = count.getWords(data[k][index]);
				for(int m=0;m<words.size();m++)
				{
					String word = words.get(m);
					count_number++;
					if(count.judgeExist(exist, word))
					{
						continue;
					}
					int number = count.stringFind(content, word);
					exist.add(word);
					wordsCount.put(word, number);
					System.out.println(word+" "+number);
				}
			}
		}
		wordsCount = MapSort.sortMapByValue(wordsCount);
		Iterator iterator = wordsCount.entrySet().iterator();
		int number = 0;
		while((number < top_k) && iterator.hasNext())
		{
			Map.Entry entry = (Entry) iterator.next();
			String key = (String) entry.getKey();
			topWords.add(key);
			number++;
		}
		System.out.println(wordsCount.size());
		System.out.println(count_number);
	}
	
	//从一个String中解析出单词，单词之间以空格为分隔符
	public ArrayList<String> getWords(String line)
	{
		ArrayList<String> words = new ArrayList<String>();
		while(line.indexOf(" ") != -1)
		{
			int index = line.indexOf(" ");
			String temp = line.substring(0, index);
			if(!temp.equals(""))
			{
			  words.add(temp);
			}
			line = line.replace(temp+" ", "");
		}
		words.add(line);
		return words;
	}
	
	//查找某个子串在整个String中出现的次数
	public int stringFind(String source, String target){
	       int number = 0;
	       int i = 0;
	       while((i=source.indexOf(target, i))!=-1) {
	           number++;
	           i++;
	       }
	       return number;
	 }
	
	//将某列中的全部数据放到一个String中
	public String unionContent(String[][] data, int index)
	{
		String content = "";
		for(int i=1;i<data.length;i++)
		{
			content += " "+data[i][index];
		}
		return content;
	}
	
	//判断单词word在list中是否已存在
	public boolean judgeExist(ArrayList<String> list, String word)
	{
		for(int i=0;i<list.size();i++)
		{
			String temp = list.get(i);
			if(temp.equalsIgnoreCase(word))  //不区分大小写的进行比较
			{
				return true;
			}
		}
		return false;
	}

}


