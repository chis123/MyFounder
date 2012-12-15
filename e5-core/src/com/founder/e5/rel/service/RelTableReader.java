/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-4-28  17:48:46
 *  
 *********************************************************************/
package com.founder.e5.rel.service;

import com.founder.e5.rel.model.RelTable;

public interface RelTableReader {
    
    /**
     * �����ĵ���ID����������ID����ȡ��Ӧ�ķ�����������
     * @param docLibID �ĵ���ID
     * @param catTypeID ��������ID
     * @return ���û�У��򷵻�null
     */
    public RelTable getRelTable(int docLibID,int catTypeID);
    
    /**
     * �����ĵ���ID����������ID����ȡ��Ӧ�ķ������������
     * @param docLibID �ĵ���ID
     * @param catTypeID ��������ID
     * @return ���û�У��򷵻�null
     */
    public String getRelTableName(int docLibID,int catTypeID);
}
