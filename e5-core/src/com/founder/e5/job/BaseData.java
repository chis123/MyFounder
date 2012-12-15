package com.founder.e5.job;

/**
 * 某些情况下，一个系统会需要在后台一直运行某个服务程序。<br/>
 * 该服务程序每隔一段时间重复执行一次，通过检查某个表获得它要执行的任务。<br/>
 * 每个需执行的任务作为表的一条记录而存在。<br/>
 * 当服务执行任务时，先把表中的任务做一个在执行标记，执行完后删除该记录。<br/>
 * 若任务执行不成功，则把表中的任务做不成功标记。<br/>
 * <br/>
 * job包就是为了这种情况而做的抽象。<br/>
 * <br/>
 * BaseData表示服务任务表的一条记录，要求服务任务表必须有一个可唯一标识的列，
 * 以及一个表示执行状态的列。在BaseData中以docID和status表示。<br/>
 * 子类可扩展这个类，加入其他服务任务属性。
 * 
 * Created on 2005-6-1
 * @author Gong Lijie
 */
public class BaseData
{
	/**主键*/
	protected int docID;
	/**执行状态*/
	protected int status;
	
	/**状态：未执行*/
	public static final int STATUS_WAITING = 0;
	/**状态：在执行*/
    public static final int STATUS_DOING = 1;
	/**状态：执行失败*/
    public static final int STATUS_FAILED = 2;

    public int getDocID()
    {
        return docID;
    }
    public void setDocID(int docid)
    {
        this.docID = docid;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int nStatus)
    {
        this.status = nStatus;
    }
}
