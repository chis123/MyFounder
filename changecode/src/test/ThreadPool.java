package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class ThreadPool {
	
	//private HashMap<String, TimeThread> map = new HashMap(); 
	private ArrayList list = new ArrayList();
//	/synchronized
//	public synchronized TimeThread get(String key){
//		return (TimeThread) list.get(key);
//	}
	
	public synchronized void put(TimeThread timethread){
		list.add(timethread);
	}
	

	public synchronized boolean remove(TimeThread timethread){
		
		
		return list.remove(timethread);
		
	}
	public synchronized int getSize()
	{
		return list.size();
	}
}
