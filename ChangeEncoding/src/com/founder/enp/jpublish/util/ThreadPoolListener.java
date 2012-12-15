/*
 * $Logfile: /chinadaily/enp/src/com/founder/enp/jpublish/util/ThreadPoolListener.java $
 * $Revision: 5 $
 * $Date: 06-07-21 22:14 $
 * $Author: Liu_dong $
 * $History: ThreadPoolListener.java $
 * 
 * *****************  Version 5  *****************
 * User: Liu_dong     Date: 06-07-21   Time: 22:14
 * Updated in $/chinadaily/enp/src/com/founder/enp/jpublish/util
 *
 * Copyright (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.enp.jpublish.util;

/**
 * <p>ThreadPoolListener， 从Tomcat Copy 的线程池类 </p>
 * <p>监视线程运行的listener </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 北京北大方正集团公司</p>
 * @author 周祖胜
 * @version 1.0
 */

public interface ThreadPoolListener {
  public void threadStart( ThreadPool tp, Thread t);
  public void threadEnd( ThreadPool tp, Thread t);
}
