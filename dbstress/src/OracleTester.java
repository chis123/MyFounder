import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class OracleTester implements Runnable {
    
    private static Log log = LogFactory.getLog(OracleTester.class);



    /********************************************************************************/
    //并发线程数，可以模拟编辑的并发操作
    private static final int THREAD_COUNT = 50;
    //每个线程里面循环执行多少次
    private static final int LOOP_COUNT = 10;
    //循环执行过程中间休息的时间(单位：秒)
    private static final long SLEEP_TIME = 1;
    //数据库连接的信息
    private static final String URL = "jdbc:oracle:thin:@128.0.168.15:1521:cms40";
    private static final String USER = "cmsuser";
    private static final String PWD = "xyz";
    //要查询的栏目节点
    private static final int NODE_ID = 90;
    /********************************************************************************/



    //每次执行是否要实际取数据
    private static final boolean IS_GET_DATA = false;
    //发布库列表的SQL
    private static final String SQL_SELECT =
//    	"select articleid,title,author,masterid,attrtype as type,"+
//    	"url,subscriber,expirationtime,publishstate,importance,"+
//    	"isremotesend,to_char(displayorder) sorder,pubtime,attr "+
//    	"from pagelayout "+
//    	"where archiveflag=0 and  (SUBSCRIBER='刘冬' "+
//    	"OR SUBSCRIBER is NULL OR SUBSCRIBER='' OR publishstate>0) "+
//    	"AND pubtime between TO_DATE('2007-07-01','yyyy-mm-dd') and sysdate "+
//    	"AND nodeid=" + NODE_ID + " " +
//    	"ORDER BY DISPLAYORDER,PUBTIME DESC";
    	"select * from releaselib where contains(title,'三星')>0 "+
    	"and pubtime between to_date('2007-07-01','yyyy-mm-dd') and sysdate "+
    	"order by pubtime desc";

    private static long allTime = 0;
    private static int allCount = 0;
    /**
     * @param args
     */
    public static void main(String[] args) {
        log.info("程序开始运行...");
        printInfo();
        OracleTester ot = new OracleTester();
        Thread thread;
        String threadName;
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadName = "Thread-test-oracle" + (i+1);
            thread = new Thread(ot, threadName);
            thread.start();
        }
        while(allCount != (THREAD_COUNT * LOOP_COUNT)) {
            try {
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (InterruptedException e) {
                //ignore
            }
        }
        log.info("总时间:" + allTime);
        log.info("总次数:" + allCount);
        log.info("平均时间:" + (int)((double)allTime/(double)allCount) + "毫秒");
    }
    
    private static void printInfo() {
        log.info("本次程序运行共开启线程数量：" + THREAD_COUNT);
        log.info("每个线程里面循环连接的次数：" + LOOP_COUNT);
        log.info("循环执行过程中间休息的时间(单位：秒)" + SLEEP_TIME);
        log.info("要查询的栏目节点：" + NODE_ID);
    }

    public void run() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            exec(i+1);
            if(SLEEP_TIME > 0) {
                try {
                    Thread.sleep(SLEEP_TIME * 1000);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
    }
    
    private Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PWD);
        }
        catch(Exception ex) {
            log.error("数据库连接失败", ex);
        }
        return conn;
    }
    
    private void exec(int n) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        long begin = System.currentTimeMillis();
        
        try {
            conn = getConn();
            begin = System.currentTimeMillis();
            pst = conn.prepareStatement(SQL_SELECT);
            rs = pst.executeQuery();
            if(IS_GET_DATA) {
                while(rs.next()) {
                    String title = rs.getString(2);
                    log.debug("Title: " + title);
                }
            }
        }
        catch(Exception ex) {
            log.error("查询数据库失败", ex);
        }
        finally {
            try{rs.close();}catch(Exception ex){}
            try{pst.close();}catch(Exception ex){}
            try{conn.close();}catch(Exception ex){}
        }
        long end = System.currentTimeMillis();
        allTime += end-begin;
        allCount++;
        log.info("本线程"+n+"次查询执行完毕，用时：" + (end-begin) + "毫秒");
    }
    
    
}

