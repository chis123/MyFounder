/*
 * $Logfile $
 * $Revision $
 * $Date $
 * $Author $
 * $History $
 *
 * Copyright  (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.enp.jpublish.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>ThreadPool， 从Tomcat Copy 的线程池类 </p>
 * <p>维护一定的线程，需要工作时从中分配一个线程，用完后自动回收线程 </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 北京北大方正集团公司</p>
 * @author 周祖胜
 * @version 1.0
 */

public class ThreadPool
{

    private static Log log = LogFactory.getLog(ThreadPool.class);

    /**
     * Default values ...
     */
    public static final int MAX_THREADS = 50;
    public static final int MAX_THREADS_MIN = 4;
    public static final int MAX_SPARE_THREADS = 50;
    public static final int MIN_SPARE_THREADS = 4;
    public static final int WORK_WAIT_TIMEOUT = 60 * 1000;

    /**
     * 存放维护的线程列表.
     */
    protected ControlRunnable[] pool = null;

    /**
     * 监视线程，自动用来回收空闲的线程
     */
    protected MonitorRunnable monitor;

    /**
     * 线程池中可以打开的最大数目.
     */
    protected int maxThreads;

    /**
     * 线程池中可以保留的最小空闲数目.
     */
    protected int minSpareThreads;

    /**
     * 线程池中可以保留的最大空闲数目.
     */
    protected int maxSpareThreads;

    /*
     * 线程池中当前线程数目.
     */
    protected int currentThreadCount;

    /*
     * 线程池中当前已经在使用线程数目.
     */
    protected int currentThreadsBusy;

    /*
     * 停止线程池的标记.
     */
    protected boolean stopThePool;

    /* 主线程是否是Daemon */
    protected boolean isDaemon = true;

    /**
     * 池中的线程，便于查找
     * Key is Thread, value is the ControlRunnable
     */
    protected Hashtable threads = new Hashtable();

    protected Vector listeners = new Vector();

    /**
     * 线程池的名称
     */
    protected String name = "TP";

    /**
     * Sequence.
     */
    protected int sequence = 1;

    private ThreadPool()
    {
        maxThreads = MAX_THREADS;
        maxSpareThreads = MAX_SPARE_THREADS;
        minSpareThreads = MIN_SPARE_THREADS;
        currentThreadCount = 0;
        currentThreadsBusy = 0;
        stopThePool = false;
    }

    /**
     * 创建一个 ThreadPool 对象实例.
     * @return ThreadPool instance
     */
    public static ThreadPool createThreadPool()
    {
        return new ThreadPool();
    }

    public synchronized void start()
    {
        stopThePool = false;
        currentThreadCount = 0;
        currentThreadsBusy = 0;

        adjustLimits();

        pool = new ControlRunnable[maxThreads];

        openThreads(maxThreads);
        monitor = new MonitorRunnable(this);
    }

    public MonitorRunnable getMonitor()
    {
        return monitor;
    }

    public void setMaxThreads(int maxThreads)
    {
        this.maxThreads = maxThreads;
    }

    public int getMaxThreads()
    {
        return maxThreads;
    }

    public void setMinSpareThreads(int minSpareThreads)
    {
        this.minSpareThreads = minSpareThreads;
    }

    public int getMinSpareThreads()
    {
        return minSpareThreads;
    }

    public void setMaxSpareThreads(int maxSpareThreads)
    {
        this.maxSpareThreads = maxSpareThreads;
    }

    public int getMaxSpareThreads()
    {
        return maxSpareThreads;
    }

    public int getCurrentThreadCount()
    {
        return currentThreadCount;
    }

    public int getCurrentThreadsBusy()
    {
        return currentThreadsBusy;
    }

    public boolean isDaemon()
    {
        return isDaemon;
    }

    public static int getDebug()
    {
        return 0;
    }

    /** The default is true - the created threads will be
     *  in daemon mode. If set to false, the control thread
     *  will not be daemon - and will keep the process alive.
     */
    public void setDaemon(boolean b)
    {
        isDaemon = b;
    }

    public boolean getDaemon()
    {
        return isDaemon;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public synchronized boolean isfull()
    {
        return currentThreadsBusy == maxThreads;

    }

    protected int getSequence()
    {
        return sequence++;
    }

    protected void addThread(Thread t, ControlRunnable cr)
    {
        threads.put(t, cr);
        for (int i = 0; i < listeners.size(); i++)
        {
            ThreadPoolListener tpl = (ThreadPoolListener) listeners.elementAt(i);
            tpl.threadStart(this, t);
        }
    }

    protected void removeThread(Thread t)
    {
        threads.remove(t);
        for (int i = 0; i < listeners.size(); i++)
        {
            ThreadPoolListener tpl = (ThreadPoolListener) listeners.elementAt(i);
            tpl.threadEnd(this, t);
        }
    }

    public void addThreadPoolListener(ThreadPoolListener tpl)
    {
        listeners.addElement(tpl);
    }

    public Enumeration getThreads()
    {
        return threads.keys();
    }

    public void run(Runnable r)
    {
        ControlRunnable c = findControlRunnable();
        c.runIt(r);

    }

    //
    // You may wonder what you see here ... basically I am trying
    // to maintain a stack of threads. This way locality in time
    // is kept and there is a better chance to find residues of the
    // thread in memory next time it runs.
    //

    /**
     * Executes a given Runnable on a thread in the pool, block if needed.
     */
    public void runIt(ThreadPoolRunnable r)
    {
        if (null == r)
        {
            throw new NullPointerException();
        }

        ControlRunnable c = findControlRunnable();
        c.runIt(r);
    }

    private ControlRunnable findControlRunnable()
    {
        ControlRunnable c = null;

        if (stopThePool)
        {
            throw new IllegalStateException();
        }

        // Obtain a free thread from the pool.
        synchronized (this)
        {

            while (currentThreadsBusy == currentThreadCount)
            {
                // All threads are busy
                if (currentThreadCount < maxThreads)
                {
                    // Not all threads were open,
                    // Open new threads up to the max number of idel threads
                    int toOpen = currentThreadCount + minSpareThreads;
                    if(toOpen>maxThreads)
                      toOpen = maxThreads;
                    openThreads(toOpen);
                }
                else
                {
                    logFull(log, currentThreadCount, maxThreads);
                    // Wait for a thread to become idel.
                    try
                    {
                        this.wait();
                    }
                    // was just catch Throwable -- but no other
                    // exceptions can be thrown by wait, right?
                    // So we catch and ignore this one, since
                    // it'll never actually happen, since nowhere
                    // do we say pool.interrupt().
                    catch (InterruptedException e)
                    {
                        log.error("Unexpected exception", e);
                    }
                    if (log.isDebugEnabled())
                    {
                        log.debug("Finished waiting: CTC=" + currentThreadCount +
                                  ", CTB=" + currentThreadsBusy);
                    }
                    // Pool was stopped. Get away of the pool.
                    if (stopThePool)
                    {
                        break;
                    }
                }
            }
            // Pool was stopped. Get away of the pool.
            if (0 == currentThreadCount || stopThePool)
            {
                throw new IllegalStateException();
            }

            // If we are here it means that there is a free thread. Take it.
            int pos = currentThreadCount - currentThreadsBusy - 1;
            c = pool[pos];
            pool[pos] = null;
            currentThreadsBusy++;

        }
        return c;
    }

    private static void logFull(Log loghelper, int currentThreadCount,
                                int maxThreads)
    {
        if (log.isDebugEnabled())
        {
            log.debug("All threads are busy " + currentThreadCount + " " +
                      maxThreads);
        }
    }

    /**
     * Stop the thread pool
     */
    public synchronized void shutdown()
    {
        if (!stopThePool)
        {
            stopThePool = true;
            monitor.terminate();
            monitor = null;
            for (int i = 0; i < (currentThreadCount - currentThreadsBusy - 1);
                 i++)
            {
                try
                {
                    pool[i].terminate();
                }
                catch (Throwable t)
                {
                    /*
                     * Do nothing... The show must go on, we are shutting
                     * down the pool and nothing should stop that.
                     */
                    log.error(
                        "Ignored exception while shutting down thread pool", t);
                }
            }
            currentThreadsBusy = currentThreadCount = 0;
            pool = null;
            notifyAll();
        }
    }

    /**
     * Called by the monitor thread to harvest idle threads.
     */
    protected synchronized void checkSpareControllers()
    {
        return ;
//        if (stopThePool)
//        {
//            return;
//        }
//
//        if ( (currentThreadCount - currentThreadsBusy) > maxSpareThreads)
//        {
//            int toFree = currentThreadCount -
//                currentThreadsBusy -
//                maxSpareThreads;
//            if(toFree>0)
//              log.info("开始回收空闲线程！currentThreadCount:"+currentThreadCount+"&&currentThreadsBusy"+"&&maxSpareThreads"+maxSpareThreads);
//            for (int i = 0; i < toFree; i++)
//            {
//                ControlRunnable c = pool[currentThreadCount -
//                    currentThreadsBusy - 1];
//                c.terminate();
//                pool[currentThreadCount - currentThreadsBusy - 1] = null;
//                currentThreadCount--;
//            }
//
//        }

    }

    /**
     * Returns the thread to the pool.
     * Called by threads as they are becoming idel.
     */
    protected synchronized void returnController(ControlRunnable c)
    {

        if (0 == currentThreadCount || stopThePool)
        {
            c.terminate();
            return;
        }

        // atomic
        currentThreadsBusy--;

        pool[currentThreadCount - currentThreadsBusy - 1] = c;
        notify();
    }

    /**
     * Inform the pool that the specific thread finish.
     *
     * Called by the ControlRunnable.run() when the runnable
     * throws an exception.
     */
    protected synchronized void notifyThreadEnd(ControlRunnable c)
    {
        currentThreadsBusy--;
        currentThreadCount--;
        notify();
    }

    /*
     * Checks for problematic configuration and fix it.
     * The fix provides reasonable settings for a single CPU
     * with medium load.
     */
    protected void adjustLimits()
    {
        if (maxThreads <= 0)
        {
            maxThreads = MAX_THREADS;
        }
        else if (maxThreads < MAX_THREADS_MIN)
        {
            log.warn("threadpool.max_threads_too_low");
            maxThreads = MAX_THREADS_MIN;
        }

        if (maxSpareThreads >= maxThreads)
        {
            maxSpareThreads = maxThreads;
        }

        if (maxSpareThreads <= 0)
        {
            if (1 == maxThreads)
            {
                maxSpareThreads = 1;
            }
            else
            {
                maxSpareThreads = maxThreads / 2;
            }
        }

        if (minSpareThreads > maxSpareThreads)
        {
            minSpareThreads = maxSpareThreads;
        }

        if (minSpareThreads <= 0)
        {
            if (1 == maxSpareThreads)
            {
                minSpareThreads = 1;
            }
            else
            {
                minSpareThreads = maxSpareThreads / 2;
            }
        }
    }

    /** Create missing threads.
     *
     * @param toOpen Total number of threads we'll have open
     */
    protected void openThreads(int toOpen)
    {

        if (toOpen > maxThreads)
        {
            toOpen = maxThreads;
        }

        for (int i = currentThreadCount; i < toOpen; i++)
        {
            pool[i - currentThreadsBusy] = new ControlRunnable(this);
        }

        currentThreadCount = toOpen;
    }

    public static class MonitorRunnable
        implements Runnable
    {
        ThreadPool p;
        Thread t;
        int interval = WORK_WAIT_TIMEOUT;
        boolean shouldTerminate;

        MonitorRunnable(ThreadPool p)
        {
            this.p = p;
            this.start();
        }

        public void start()
        {
            shouldTerminate = false;
            t = new Thread(this);
            t.setDaemon(p.getDaemon());
            t.setName(p.getName() + "-Monitor");
            t.start();
        }

        public void setInterval(int i)
        {
            this.interval = i;
        }

        public void run()
        {
            while (true)
            {
                try
                {

                    // Sleep for a while.
                    synchronized (this)
                    {
                        this.wait(WORK_WAIT_TIMEOUT);
                    }

                    // Check if should terminate.
                    // termination happens when the pool is shutting down.
                    if (shouldTerminate)
                    {
                        break;
                    }

                    // Harvest idle threads.
                    p.checkSpareControllers();

                }
                catch (Throwable t)
                {
                    ThreadPool.log.error("Unexpected exception", t);
                }
            }
        }

        public void stop()
        {
            this.terminate();
        }

        /** Stop the monitor
         */
        public synchronized void terminate()
        {
            shouldTerminate = true;
            this.notify();
        }

    }

    /**
     * A Thread object that executes various actions ( ThreadPoolRunnable )
     *  under control of ThreadPool
     */
    public static class ControlRunnable
        implements Runnable
    {
        /**
         * ThreadPool where this thread will be returned
         */
        private ThreadPool p;

        /**
         * The thread that executes the actions
         */
        private ThreadWithAttributes t;

        /**
         * The method that is executed in this thread
         */

        private ThreadPoolRunnable toRun;
        private Runnable toRunRunnable;

        /**
         * Stop this thread
         */
        private boolean shouldTerminate;

        /**
         * Activate the execution of the action
         */
        private boolean shouldRun;

        /**
         * Per thread data - can be used only if all actions are
         *  of the same type.
         *  A better mechanism is possible ( that would allow association of
         *  thread data with action type ), but right now it's enough.
         */
        private boolean noThData;

        /**
         * Start a new thread, with no method in it
         */
        ControlRunnable(ThreadPool p)
        {
            toRun = null;
            shouldTerminate = false;
            shouldRun = false;
            this.p = p;
            t = new ThreadWithAttributes(p, this);
            t.setDaemon(true);
            t.setName(p.getName() + "-Processor" + p.getSequence());
            t.start();
            p.addThread(t, this);
            noThData = true;
        }

        public void run()
        {
            try
            {
                while (true)
                {
                    try
                    {
                        /* Wait for work. */
                        synchronized (this)
                        {
                            if (!shouldRun && !shouldTerminate)
                            {
                                log.info("lwm  线程等待");
                                this.wait();
                                log.info("lwm 等待完毕");
                            }
                        }

                        if (shouldTerminate)
                        {
                            log.info("lwm 应该中断");
                            if (p.log.isDebugEnabled())
                            {
                                p.log.debug("Terminate");
                            }
                            break;
                        }

                        /* Check if should execute a runnable.  */
                        try
                        {
                            if (noThData)
                            {
                                log.info("lwm noThDate" + noThData);
                                if (toRun != null)
                                {
                                    Object thData[] = toRun.getInitData();
                                    t.setThreadData(p, thData);
                                    if (p.log.isDebugEnabled())
                                    {
                                        p.log.debug("Getting new thread data");
                                    }
                                }
                                noThData = false;
                            }

                            if (shouldRun)
                            {

                                if (toRun != null)
                                {
                                    log.info("lwm toRun.runit");
                                    toRun.runIt(t.getThreadData(p));
                                }
                                else if (toRunRunnable != null)
                                {
                                    log.info("lwm toRunRunnable.run");
                                    toRunRunnable.run();
                                }
                                else
                                {
                                    if (p.log.isDebugEnabled())
                                    {
                                        p.log.debug("No toRun ???");
                                    }
                                }
                            }
                        }
                        catch (Throwable t)
                        {
                            p.log.error("threadpool.thread_error", t);
                            /*
                             * The runnable throw an exception (can be even a ThreadDeath),
                             * signalling that the thread die.
                             *
                             * The meaning is that we should release the thread from
                             * the pool.
                             */
                            shouldTerminate = true;
                            shouldRun = false;
                            p.notifyThreadEnd(this);
                        }
                        finally
                        {
                            if (shouldRun)
                            {
                                shouldRun = false;
                                /*
                                 * Notify the pool that the thread is now idle.
                                 */
                                p.returnController(this);



                            }

                        }
                        //System.out.println("当前线程数："+p.createThreadPool().currentThreadCount+"\n当前忙的线程数："+p.currentThreadsBusy+"\n池子长度："+p.pool.length);
                        /*
                         * Check if should terminate.
                         * termination happens when the pool is shutting down.
                         */
                        if (shouldTerminate)
                        {
                            break;
                        }
                    }
                    catch (InterruptedException ie)
                    { /* for the wait operation */
                        // can never happen, since we don't call interrupt
                        p.log.error("Unexpected exception", ie);
                    }
                }
            }
            finally
            {
                p.removeThread(Thread.currentThread());
            }
        }

        /** Run a task
         *
         * @param toRun
         */
        public synchronized void runIt(Runnable toRun)
        {
            this.toRunRunnable = toRun;
            // Do not re-init, the whole idea is to run init only once per
            // thread - the pool is supposed to run a single task, that is
            // initialized once.
            // noThData = true;
            shouldRun = true;
            this.notify();
        }

        /** Run a task
         *
         * @param toRun
         */
        public synchronized void runIt(ThreadPoolRunnable toRun)
        {
            this.toRun = toRun;
            // Do not re-init, the whole idea is to run init only once per
            // thread - the pool is supposed to run a single task, that is
            // initialized once.
            // noThData = true;
            shouldRun = true;
            log.info("lwm 放进来了 准备notify");
            this.notify();
            log.info("lwm notify结束");
        }

        public void stop()
        {
            this.terminate();
        }

        public synchronized void terminate()
        {
            shouldTerminate = true;
            this.notify();
        }
    }
}
