import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class OracleTester implements Runnable {
    
    private static Log log = LogFactory.getLog(OracleTester.class);



    /********************************************************************************/
    //�����߳���������ģ��༭�Ĳ�������
    private static final int THREAD_COUNT = 50;
    //ÿ���߳�����ѭ��ִ�ж��ٴ�
    private static final int LOOP_COUNT = 10;
    //ѭ��ִ�й����м���Ϣ��ʱ��(��λ����)
    private static final long SLEEP_TIME = 1;
    //���ݿ����ӵ���Ϣ
    private static final String URL = "jdbc:oracle:thin:@128.0.168.15:1521:cms40";
    private static final String USER = "cmsuser";
    private static final String PWD = "xyz";
    //Ҫ��ѯ����Ŀ�ڵ�
    private static final int NODE_ID = 90;
    /********************************************************************************/



    //ÿ��ִ���Ƿ�Ҫʵ��ȡ����
    private static final boolean IS_GET_DATA = false;
    //�������б��SQL
    private static final String SQL_SELECT =
//    	"select articleid,title,author,masterid,attrtype as type,"+
//    	"url,subscriber,expirationtime,publishstate,importance,"+
//    	"isremotesend,to_char(displayorder) sorder,pubtime,attr "+
//    	"from pagelayout "+
//    	"where archiveflag=0 and  (SUBSCRIBER='����' "+
//    	"OR SUBSCRIBER is NULL OR SUBSCRIBER='' OR publishstate>0) "+
//    	"AND pubtime between TO_DATE('2007-07-01','yyyy-mm-dd') and sysdate "+
//    	"AND nodeid=" + NODE_ID + " " +
//    	"ORDER BY DISPLAYORDER,PUBTIME DESC";
    	"select * from releaselib where contains(title,'����')>0 "+
    	"and pubtime between to_date('2007-07-01','yyyy-mm-dd') and sysdate "+
    	"order by pubtime desc";

    private static long allTime = 0;
    private static int allCount = 0;
    /**
     * @param args
     */
    public static void main(String[] args) {
        log.info("����ʼ����...");
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
        log.info("��ʱ��:" + allTime);
        log.info("�ܴ���:" + allCount);
        log.info("ƽ��ʱ��:" + (int)((double)allTime/(double)allCount) + "����");
    }
    
    private static void printInfo() {
        log.info("���γ������й������߳�������" + THREAD_COUNT);
        log.info("ÿ���߳�����ѭ�����ӵĴ�����" + LOOP_COUNT);
        log.info("ѭ��ִ�й����м���Ϣ��ʱ��(��λ����)" + SLEEP_TIME);
        log.info("Ҫ��ѯ����Ŀ�ڵ㣺" + NODE_ID);
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
            log.error("���ݿ�����ʧ��", ex);
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
            log.error("��ѯ���ݿ�ʧ��", ex);
        }
        finally {
            try{rs.close();}catch(Exception ex){}
            try{pst.close();}catch(Exception ex){}
            try{conn.close();}catch(Exception ex){}
        }
        long end = System.currentTimeMillis();
        allTime += end-begin;
        allCount++;
        log.info("���߳�"+n+"�β�ѯִ����ϣ���ʱ��" + (end-begin) + "����");
    }
    
    
}

