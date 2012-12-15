package com.founder.e5.job;


import java.util.ArrayList;
import java.util.List;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.Context;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;

/**
 * ĳЩ����£�һ��ϵͳ����Ҫ�ں�̨һֱ����ĳ���������<br/>
 * �÷������ÿ��һ��ʱ���ظ�ִ��һ�Σ�ͨ�����ĳ��������Ҫִ�е�����<br/>
 * ÿ����ִ�е�������Ϊ���һ����¼�����ڡ�<br/>
 * ������ִ������ʱ���Ȱѱ��е�������һ����ִ�б�ǣ�ִ�����ɾ���ü�¼��<br/>
 * ������ִ�в��ɹ�����ѱ��е����������ɹ���ǡ�<br/>
 * <br/>
 * job������Ϊ��������������ĳ���<br/>
 * <br/>
 * BaseManager�ǽ���������ĳ�����ࡣ<br/>
 * �����������ӡ�ɾ������ȡһ����������ķ������޸�ִ�б�ǡ���ȡ���з�������ķ�����
 * �Լ������������ķ�����<br/>
 * ���У������������ķ���<code>handleData</code>���Ƿ���ĺ����߼�����Ҫÿ���������ʵ�֣�
 * ����ǳ��󷽷���<br/>
 * ����һ����������ķ���<code>addData</code>Ҳ�ǳ���ģ���Ϊ��ͬ������������Կ��ܲ�ͬ��
 * Ҳ����˵����ͬ�����ʵ�ֲ�ͬ��BaseData���ࡣ<br/>
 * ͬ�������ݿ��ȡһ�����������¼����װ��BaseData���������ķ���
 * <code>assemble</code>Ҳ�ǳ���ġ�<br/>
 * 
 * <p>
 * ÿ�����඼Ӧ�����¶����Լ���tableName,idField,statusField���ԣ�
 * �Լ�log����
 * </p>
 * @created 2006-7-27
 * @author Gong Lijie
 * @version 1.0
 */
public abstract class BaseManager
{
	/** �����漰�ı������̳�������Ҫ���� */
    protected String tableName = "";
    /** ��������������ȱʡ����ΪDOCID */
    protected String idField = "DOCID";
    /** ������״̬�ֶ�����ȱʡ����ΪSTATUS */
    protected String statusField = "STATUS";
    /** �����Ķ�ȡ����ʱ�������ֶ�����������ʱ��ʾû�������ֶ� */
    protected String orderField = null;

    protected DBSession conn = null;
    protected Log log;
    protected BaseManager()
	{
	}
    /**
     * �������������һ������������
     * @param data
     */
    public abstract void addData(BaseData data) throws Exception;
    /**
     * ����ID��ȡһ����¼
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
	 * ȡ��ָ��״̬�µ����м�¼������������<br/>
	 * �÷�����Ҫ����������ʵ�ֵ�assemble(IResultSet)������<br/>
	 * ����ȡ�߼��бȽϴ�ı仯���������ȫ��д�˷���<br/>
	 * 
	 * @param status
	 * @return List ����ֵ��һ��BaseData��List��
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
	 * �Ӷ�ȡ���ݿ��Ľ�����У���װ��һ����¼�����ݶ���<br/>
	 * �÷�������getAllData��getData�����С�
	 * @param rs
	 * @return BaseData
	 */
	protected abstract BaseData assemble(IResultSet rs) throws Exception;
	
    /**
     * �����ʵ�ʴ����߼���<br/>
     * ����һ�������¼������������
     * @param data
     * @throws Exception
     */
    public abstract void handleData(BaseData data) throws Exception;
    /**
     * ���ñ��<br/>
     * �ڴ���һ������֮ǰ���ȰѼ�¼���Ϊ�ڴ���<br/>
     * ������ʧ�ܣ�Ҳ��ʧ�ܱ�ǡ�<br/>
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
     * ������ɺ�ɾ������ļ�¼
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
	 * ����log����ʹ�������ʱ����ʹ��ͬ������־
	 * @return
	 */
	public Log getLog()
	{
		return log;
	}
}

