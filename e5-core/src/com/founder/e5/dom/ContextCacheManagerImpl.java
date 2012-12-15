/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-1-10  18:43:53
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
