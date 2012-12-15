/*
 * $Logfile: /chinadaily/enp/src/com/founder/enp/jpublish/util/ThreadPoolRunnable.java $
 * $Revision: 5 $
 * $Date: 06-07-21 22:14 $
 * $Author: Liu_dong $
 * $History: ThreadPoolRunnable.java $
 * 
 * *****************  Version 5  *****************
 * User: Liu_dong     Date: 06-07-21   Time: 22:14
 * Updated in $/chinadaily/enp/src/com/founder/enp/jpublish/util
 *
 * Copyright (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.enp.jpublish.util;


/** Implemented if you want to run a piece of code inside a thread pool.
 */
public interface ThreadPoolRunnable {
    // XXX use notes or a hashtable-like
    // Important: ThreadData in JDK1.2 is implemented as a Hashtable( Thread -> object ),
    // expensive.

    /** Called when this object is first loaded in the thread pool.
     *  Important: all workers in a pool must be of the same type,
     *  otherwise the mechanism becomes more complex.
     */
    public Object[] getInitData();

    /** This method will be executed in one of the pool's threads. The
     *  thread will be returned to the pool.
     */
    public void runIt(Object thData[]);

}
