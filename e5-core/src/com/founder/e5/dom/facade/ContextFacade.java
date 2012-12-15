/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2005-7-21  15:51:17
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
