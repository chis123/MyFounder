/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-1-10  18:43:53
 *  
 *********************************************************************/
package com.founder.e5.dom;

import com.founder.e5.context.CacheManager;
import com.founder.e5.context.CacheReader;

class ContextCacheManagerImpl implements ContextCacheManager {

    public Object find(Class class1) {
        return CacheReader.find(class1);
    }

    public void refresh(Class clazz) throws Exception {
       CacheManager.refresh(clazz);
    }

}
