package test;

import java.util.ArrayList;

public class ThreadWeitPool {
	private ArrayList list = new ArrayList();
	
	public synchronized void add(TimeThread timethread){
		list.add(timethread);
	}
	public synchronized TimeThread get(){
		if(list.size()>0){
			TimeThread timeThread = (TimeThread) list.get(0);
			list.remove(timeThread);
			return timeThread;
		}
		return null;
	}
	public synchronized boolean getThread(){
		return list.size()>0;
	}
	
	public int getSize(){
		return list.size();
	}
	
}
