package com.founder.e5.job;


import java.util.ArrayList;
import java.util.List;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.Context;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;

/**
 * 某些情况下，一个系统会需要在后台一直运行某个服务程序。<br/>
 * 该服务程序每隔一段时间重复执行一次，通过检查某个表获得它要执行的任务。<br/>
 * 每个需执行的任务作为表的一条记录而存在。<br/>
 * 当服务执行任务时，先把表中的任务做一个在执行标记，执行完后删除该记录。<br/>
 * 若任务执行不成功，则把表中的任务做不成功标记。<br/>
 * <br/>
 * job包就是为了这种情况而做的抽象。<br/>
 * <br/>
 * BaseManager是进行任务处理的抽象基类。<br/>
 * 它定义了增加、删除、读取一个服务任务的方法，修改执行标记、读取所有服务任务的方法，
 * 以及处理服务任务的方法。<br/>
 * 其中，处理服务任务的方法<code>handleData</code>就是服务的核心逻辑，需要每个子类各自实现，
 * 因此是抽象方法。<br/>
 * 增加一个服务任务的方法<code>addData</code>也是抽象的，因为不同服务的任务属性可能不同，
 * 也就是说，不同服务会实现不同的BaseData子类。<br/>
 * 同理，从数据库读取一条服务任务记录后，组装成BaseData的子类对象的方法
 * <code>assemble</code>也是抽象的。<br/>
 * 
 * <p>
 * 每个子类都应该重新定义自己的tableName,idField,statusField属性，
 * 以及log变量
 * </p>
 * @created 2006-7-27
 * @author Gong Lijie
 * @version 1.0
 */
public abstract class BaseManager
{
	/** 服务涉及的表名，继承类中需要定义 */
    protected String tableName = "";
    /** 服务表的主键名，缺省定义为DOCID */
    protected String idField = "DOCID";
    /** 服务表的状态字段名，缺省定义为STATUS */
    protected String statusField = "STATUS";
    /** 服务表的读取数据时的排序字段名，不设置时表示没有排序字段 */
    protected String orderField = null;

    protected DBSession conn = null;
    protected Log log;
    protected BaseManager()
	{
	}
    /**
     * 向任务表中增加一条待处理数据
     * @param data
     */
    public abstract void addData(BaseData data) throws Exception;
    /**
     * 根据ID读取一条记录
     * @param id int
     * @return BaseData
     */
	public BaseData getData(int id){
    	StringBuffer sbSQL = new StringBuffer(100);
    	sbSQL.append("select * from ").append(tableName)
			.append(" where ").append(idField).append("=?");
    	BaseData data = null;
        try {
        	conn = Context.getDBSession();
        	IResultSet rs = conn.executeQuery(sbSQL.toString(), new Object[]{
        		new Integer(id)});
            if (rs.next()) data = assemble(rs);
            
            rs.close();
            
        	return data;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        } finally {
			close(conn);
        }
		
	}
	/**
	 * 取出指定状态下的所有记录，以做任务处理<br/>
	 * 该方法需要调用由子类实现的assemble(IResultSet)方法。<br/>
	 * 若读取逻辑有比较大的变化，则可以完全重写此方法<br/>
	 * 
	 * @param status
	 * @return List 返回值是一个BaseData的List。
	 */
    public List getAllData(int status){
    	StringBuffer sbSQL = new StringBuffer(100);
    	sbSQL.append("select * from ").append(tableName)
			.append(" where ").append(statusField).append("=?");
    	if (!StringUtils.isBlank(orderField))
    		sbSQL.append(" order by ").append(orderField);
		List list = new ArrayList();
        try {
        	conn = Context.getDBSession();
        	IResultSet rs = conn.executeQuery(sbSQL.toString(), new Object[]{
        		new Integer(status)});
            while (rs.next()) list.add(assemble(rs));
            rs.close();
            
        	return list;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        } finally {
			close(conn);
        }
	}
	/**
	 * 从读取数据库后的结果集中，组装出一条记录的数据对象。<br/>
	 * 该方法用在getAllData和getData方法中。
	 * @param rs
	 * @return BaseData
	 */
	protected abstract BaseData assemble(IResultSet rs) throws Exception;
	
    /**
     * 任务的实际处理逻辑。<br/>
     * 传入一条任务记录，进行任务处理。
     * @param data
     * @throws Exception
     */
    public abstract void handleData(BaseData data) throws Exception;
    /**
     * 设置标记<br/>
     * 在处理一条任务之前，先把记录标记为在处理。<br/>
     * 若处理失败，也做失败标记。<br/>
     * @param data
     * @param status
     * @return
     */
    public void setStatus(BaseData data, int status) throws Exception
    {
    	StringBuffer sbSQL = new StringBuffer(200);
    	sbSQL.append("update ").append(tableName)
			.append(" set ").append(statusField)
			.append("=? where ").append(idField)
			.append("=? and ").append(statusField).append("=?");
        try
        {
        	conn = Context.getDBSession();
        	int result = conn.executeUpdate(sbSQL.toString(), new Object[]{
        		new Integer(status), new Integer(data.getDocID()),
        		new Integer(data.getStatus())});
            if (result == 0)
                throw new Exception("Cannot update the status.Maybe already modified.");
        }
        finally
        {
			close(conn);
        }
    }
    /**
     * 处理完成后删除表里的记录
     * @param id int
     * @return boolean
     */
    public void delete(int id) throws Exception
    {
    	StringBuffer sbSQL = new StringBuffer(200);
    	sbSQL.append("delete from ").append(tableName)
			.append(" where ").append(idField).append("=?");
		try {
        	conn = Context.getDBSession();
        	conn.executeUpdate(sbSQL.toString(), new Object[]{
        		new Integer(id)});
		} finally {
			close(conn);
		}
    }
    protected void close(DBSession conn)
    {
        if (conn != null) conn.closeQuietly();
    }
	/**
	 * 返回log对象，使外面调用时可以使用同样的日志
	 * @return
	 */
	public Log getLog()
	{
		return log;
	}
}

