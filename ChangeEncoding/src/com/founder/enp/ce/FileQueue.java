package com.founder.enp.ce;

import java.util.ArrayList;
import java.util.List;

public class FileQueue {
    private List list = new ArrayList();
    private static Object lock = new Object();

    public synchronized List pulls()
    {
        List retList = new ArrayList();
        synchronized(lock) {
            int count = list.size();
            for(int i = 0; i < count; i++)
            {
                retList.add(list.get(i));
            }
            list.clear();
        }
        return retList;
    }

    public synchronized String pull()
    {
            String retID = null;
            synchronized(lock) {
                if(list.size() > 0)
                {
                        retID = (String)list.get(0);
                        list.remove(0);
                }
            }
            return retID;
    }

    public synchronized void pushs(List ids)
    {
        int count = ids.size();
            synchronized(lock) {
            for(int i = 0; i < count; i++)
            {
                String id = (String)ids.get(i);
                if(!list.contains(id))
                    list.add(id);
            }
        }
    }

    public synchronized void push(String id)
    {
        synchronized(lock) {
            //if(!list.contains(id))
                list.add(id);
        }
    }
    
    public int size() {
        return list.size();
    }
}
