/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2005-7-21  15:48:01
 *  
 *********************************************************************/
package com.founder.e5.dom.facade;

import com.founder.e5.context.CacheManager;

public class CacheManagerFacade {

    public static Object find ( Class class1 ) {
        return CacheManager.find ( class1 );
    }

    public synchronized static void refresh ( Class clazz ) throws Exception {
        CacheManager.refresh ( clazz );
    }

}
