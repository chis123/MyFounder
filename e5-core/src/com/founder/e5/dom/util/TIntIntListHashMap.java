/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2005-7-20  15:02:09
 *  
 *********************************************************************/
package com.founder.e5.dom.util;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;

public class TIntIntListHashMap extends TIntObjectHashMap {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TIntArrayList getIntList(int key) {
        return (TIntArrayList) super.get(key);
    }

    public void put(int key, int value) {

        TIntArrayList l = this.getIntList(key);
        if (null == l) {
            l = new TIntArrayList();
            this.put(key, l);
        }
        
        l.add(value);        

    }

}
