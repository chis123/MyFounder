/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2005-7-20  15:02:09
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
