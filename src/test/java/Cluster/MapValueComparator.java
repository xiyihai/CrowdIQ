package Cluster;


import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

class MapValueComparator implements Comparator<Map.Entry<String,Integer>> {

	@Override
	public int compare(Entry<String,Integer> me1, Entry<String,Integer> me2) {

		return me2.getValue().compareTo(me1.getValue());
	}
}


