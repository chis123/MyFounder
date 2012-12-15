/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-1-10  18:41:16
 *  
 *********************************************************************/
package com.founder.e5.dom;

public interface ContextCacheManager {

    public Object find(Class class1);

    public void refresh(Class clazz) throws Exception;

}
