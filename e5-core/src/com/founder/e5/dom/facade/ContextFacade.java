/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2005-7-21  15:51:17
 *  
 *********************************************************************/
package com.founder.e5.dom.facade;

import com.founder.e5.context.Context;
import com.founder.e5.context.DSManager;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;

public class ContextFacade {

    public static Object getBean(Class class1) {
        return Context.getBean(class1);

    }

    public static DBSession getDBSession(int dsID) throws E5Exception {
        return Context.getDBSession(dsID,true);
    }

    public static DSManager getDSManager() {
        return Context.getDSManager();
    }

}
