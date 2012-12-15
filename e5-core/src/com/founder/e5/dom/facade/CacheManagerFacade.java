/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2005-7-21  15:48:01
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
