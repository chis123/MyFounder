/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-1-10  18:41:16
 *  
 *********************************************************************/
package com.founder.e5.dom;

public interface ContextCacheManager {

    public Object find(Class class1);

    public void refresh(Class clazz) throws Exception;

}
