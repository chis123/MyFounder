package com.founder.e5.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.founder.e5.db.dialect.Dialect;

/**
 * ���ݿ�Ự�ӿڡ�<br>
 * 
 * DBSession��λΪ���ݿ�������棬ּ��ͨ����װ�ײ�jdbc����ϸ�ڣ����ṩ���û�һ����ֱ�۵�ʹ�ýӿڣ���ɳ������ݿ������<br>
 * <br>
 * DBSessionʵ�����������Ժ�ά��һ���򿪵����ݿ����ӣ�ֱ������close/closeQuietly����
 * 
 * @author liyanhui
 * @version 1.3
 * @created 29-����-2005 09:48:25
 */
public interface DBSession {

	// -------------------------------------------------------------------------
	// ��ѯ�ӿ�

	/**
	 * ִ�в�ѯ��䣬���ؽ������<br>
	 * <br>
	 * ���������ر�ʱ�����ȹرմ����ý������������
	 * 
	 * @param sql ��ѯ���
	 * @return �����
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql ) throws SQLException;

	/**
	 * ִ�в�������ѯ��䣬���ؽ������<br>
	 * ���sql����в���?����params��Ϊnull�� <br>
	 * ���������ر�ʱ�����ȹرմ����ý������������
	 * 
	 * @param sql ��������ѯ���
	 * @param params ʵ�ʲ����������в�������nullֵ��
	 * @return �����
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql, Object[] params )
			throws SQLException;

	/**
	 * ִ�в�������ѯ��䣬���ؽ������<br>
	 * ���sql����в���?����params��Ϊnull��<br>
	 * �����params��û��nullֵ����types��Ϊnull��<br>
	 * ��params����nullֵʱ��Ҫ����types�ж��ֶ����ͣ� <br>
	 * ���������ر�ʱ�����ȹرմ����ý������������
	 * 
	 * @param sql ��������ѯ���
	 * @param params ʵ�ʲ���
	 * @param types �ֶ����ͣ���java.sql.Types�ж�����������ʾ
	 * @return �����
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types )
			throws SQLException;

	/**
	 * ִ�в�������ѯ��䣬���ؽ������<br>
	 * ���sql����в���?����params��Ϊnull��<br>
	 * �����params��û��nullֵ����types��Ϊnull��<br>
	 * ��params����nullֵʱ��Ҫ����types�ж��ֶ����ͣ� <br>
	 * ���������ر�ʱ�����ȹرմ����ý������������ <br>
	 * �÷�����ָ������JDBC���ʱ�����������
	 * 
	 * @param sql ��������ѯ���
	 * @param params ʵ�ʲ���
	 * @param types �ֶ����ͣ���java.sql.Types�ж�����������ʾ
	 * @param resultSetType one of the following <code>ResultSet</code>
	 *            constants: <code>ResultSet.TYPE_FORWARD_ONLY</code>,
	 *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
	 *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
	 * @param resultSetConcurrency one of the following <code>ResultSet</code>
	 *            constants: <code>ResultSet.CONCUR_READ_ONLY</code> or
	 *            <code>ResultSet.CONCUR_UPDATABLE</code>
	 * @return �����
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types,
			int resultSetType, int resultSetConcurrency ) throws SQLException;

	/**
	 * ����һ�����ݿ��ѯ������¡�ɾ�������
	 * 
	 * @param sql ���ݿ��ѯ������¡�ɾ�������
	 * @return DBQuery����
	 */
	public DBQuery createQuery( String sql );

	// -------------------------------------------------------------------------
	// ���ݸ��½ӿ�

	/**
	 * ִ�в�����������䣬���ظ��µļ�¼����<br>
	 * ���sql����в���?����params��Ϊnull��
	 * 
	 * @param sql ��������ѯ���
	 * @param params ʵ�ʲ���
	 * @return ���²���Ӱ�������
	 * @exception SQLException
	 * @throws LaterDataTransferException �漰lob/bfile����ʱ�����ܳ����쳣
	 */
	public int executeUpdate( String sql, Object[] params )
			throws SQLException, LaterDataTransferException;

	/**
	 * ִ�в�����������䣬���ظ��µļ�¼��<br>
	 * ���sql����в���?����params��Ϊnull��<br>
	 * ���params��û��nullֵ����types��Ϊnull��<br>
	 * ��params����nullֵʱ��Ҫ����types�ж��ֶ�����
	 * 
	 * @param sql ��������ѯ���
	 * @param params ʵ�ʲ���
	 * @param types �ֶ����ͣ���java.sql.Types�ж�����������ʾ
	 * @return ���²���Ӱ�������
	 * @exception SQLException
	 * @throws LaterDataTransferException �漰lob/bfile����ʱ�����ܳ����쳣
	 */
	public int executeUpdate( String sql, Object[] params, int[] types )
			throws SQLException, LaterDataTransferException;

	/**
	 * ִ�в�����������䣬�����Զ����ɵ�ID������еĻ���<br>
	 * <br>
	 * ע�⣺�÷�������ֵ�ĺ�������ͨ��executeUpdate��ͬ��
	 * ��Ϊ���ǵ����루����£�ʧ�ܶ������׳��쳣�����迿����ֵ����ʶ
	 * 
	 * @param sql �������
	 * @param params ʵ�ʲ���
	 * @return ����������Զ�����ID���򷵻�֮�����򣬷���-1
	 * @throws SQLException
	 * @throws LaterDataTransferException
	 */
	public int executeUpdateWithRetrive( String sql, Object[] params )
			throws SQLException, LaterDataTransferException;

	/**
	 * ִ�д洢���̣������������ֵ����������򷵻ؿ����飩<br>
	 * ���sql����в���?����params��Ϊnull�� <br>
	 * ������󶨲����а���nullֵ��������ṩcolumnTypes��Ϣ��<br>
	 * ����洢���̰����������������ͨ��paramtypesָ����
	 * 
	 * @param sql ������sql���
	 * @param params ���󶨲���ֵ
	 * @param paramTypes ��������in/out�����ͣ�in��Ϊ�棬out��Ϊ��
	 * @param columnTypes �ֶ����ͣ���java.sql.Types�ж�����������ʾ
	 * @exception SQLException
	 */
	public Object[] executeSP( String sql, Object[] params,
			boolean[] paramTypes, int[] columnTypes ) throws SQLException;

	// -------------------------------------------------------------------------
	// ȡ���ݵı�������

	/**
	 * ��ѯint��ֵ
	 * 
	 * @exception SQLException
	 */
	public int getInt( String sql, Object[] params ) throws SQLException;

	/**
	 * ��ѯlong��ֵ
	 * 
	 * @param sql
	 * @param params
	 * @exception SQLException
	 */
	public long getLong( String sql, Object[] params ) throws SQLException;

	/**
	 * ��ѯfloat��ֵ
	 * 
	 * @exception SQLException
	 */
	public float getFloat( String sql, Object[] params ) throws SQLException;

	/**
	 * ��ѯdouble��ֵ
	 * 
	 * @exception SQLException
	 */
	public double getDouble( String sql, Object[] params ) throws SQLException;

	/**
	 * ��ѯString��ֵ
	 * 
	 * @exception SQLException
	 */
	public String getString( String sql, Object[] params ) throws SQLException;

	/**
	 * ��ѯDate��ֵ
	 * 
	 * @exception SQLException
	 */
	public java.sql.Date getDate( String sql, Object[] params )
			throws SQLException;

	/**
	 * ��ѯTime��ֵ
	 * 
	 * @exception SQLException
	 */
	public java.sql.Time getTime( String sql, Object[] params )
			throws SQLException;

	/**
	 * ��ѯTimestamp��ֵ
	 * 
	 * @exception SQLException
	 */
	public java.sql.Timestamp getTimestamp( String sql, Object[] params )
			throws SQLException;

	/**
	 * ֱ��ȡObject����ֵ��<br>
	 * ʵ��������jdbc�淶�����ӳ�����
	 * 
	 * @throws SQLException
	 * @created 2005-7-21 15:02:50
	 */
	public Object getObject( String sql, Object[] params ) throws SQLException;

	/**
	 * ��ѯClob��ֵ��תΪIClob���Ͷ��󷵻�
	 * 
	 * @exception IOException
	 * @exception SQLException
	 */
	public IClob getClob( String sql, Object[] params ) throws SQLException,
			IOException;

	/**
	 * ��ѯBlob��ֵ��תΪIBlob���Ͷ��󷵻�
	 * 
	 * @exception IOException
	 * @exception SQLException
	 */
	public IBlob getBlob( String sql, Object[] params ) throws SQLException,
			IOException;

	/**
	 * ��ѯIBfile��ֵ��<br>
	 * ע�⣺Ŀǰ����ֵֻ����Ŀ¼-�ļ���Ϣ������ȡ���ݡ������Ҫȡ���ݣ���ʹ��e5doc�е�
	 * BfileReader��һ�����þͿ����ˡ�����Ҫ�ǿ��ǵ�e5dbģ�鲻���������ģ�飩
	 * 
	 * @exception SQLException
	 */
	public IBfile getBfile( String sql, Object[] params ) throws SQLException;

	// -------------------------------------------------------------------------

	/**
	 * д�����ݵ����ݿ��Clob�����ֶΡ� <br>
	 * <br>
	 * ע�⣺�÷����������beginTransaction��commitTransactionʹ�ã� <br>
	 * �÷��������ǹ��ɷ��ʷ�ʽ����ȡ��һ��������д�����ݣ�oracle10��ǰ�����������
	 * �����ַ�ʽ���ã�Ŀǰ������oracle10���������Ͳ���Ҫ���������
	 * 
	 * @param id ��¼��ʶֵ
	 * @param tablename ���ݱ���
	 * @param idColumnName ��ʶ�ֶ���
	 * @param lobColumnName Clob�ֶ���
	 * @param clob ��д������
	 * @exception IOException
	 * @exception SQLException
	 */
	public void writeClob( long id, String tablename, String idColumnName,
			String lobColumnName, IClob clob ) throws SQLException, IOException;

	/**
	 * д�����ݵ����ݿ��Blob�����ֶΡ�<br>
	 * <br>
	 * ע�⣺�÷����������beginTransaction��commitTransactionʹ�ã� <br>
	 * �÷��������ǹ��ɷ��ʷ�ʽ����ȡ��һ��������д�����ݣ�oracle10��ǰ�����������
	 * �����ַ�ʽ���ã�Ŀǰ������oracle10���������Ͳ���Ҫ���������
	 * 
	 * @param id ��¼��ʶֵ
	 * @param tablename ���ݱ���
	 * @param idColumnName ��ʶ�ֶ���
	 * @param lobColumnName Blob�ֶ���
	 * @param blob ��д������
	 * @exception IOException
	 * @exception SQLException
	 */
	public void writeBlob( long id, String tablename, String idColumnName,
			String lobColumnName, IBlob blob ) throws SQLException, IOException;

	// -------------------------------------------------------------------------

	/**
	 * ���ص�ǰʹ�õ����ݿ�����
	 */
	public Connection getConnection();

	/**
	 * ���õ�ǰʹ�õ����ݿ����ӡ� <br>
	 * <br>
	 * ע�⣺���õ����ݿ����ӵ��Զ��ύ��־�ᱻ��Ϊtrue
	 */
	public void setConnection( Connection conn );

	/**
	 * �رյײ����ݿ����ӡ�<br>
	 * 
	 * һ����finally�Ӿ��е��ø÷�������Ҫע����Ҫ��try...catch����÷��������ܳ����쳣��
	 * �������÷����׳��쳣��Ḳ����finallyͬ����try���������׳����쳣��<br>
	 * 
	 * �����������û���������ע���ø÷��������׳����쳣����ʱ�Ƽ�ʹ��closeQuietly()������
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException;

	/**
	 * �رյײ����ݿ����ӣ����Ҳ��׳�SQLException<br>
	 * �÷����൱��<code>ResourceMgr.closeQuietly( dbSession )</code>
	 */
	public void closeQuietly();

	// -------------------------------------------------------------------------

	/**
	 * ��ʼһ������
	 * 
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException;

	/**
	 * �ύ��ǰ����<br>
	 * ע�⣺���֮ǰ��δ����beginTransaction��ʼ������ʲô������
	 * 
	 * @throws SQLException
	 */
	public void commitTransaction() throws SQLException;

	/**
	 * �ع���ǰ����<br>
	 * ע�⣺���֮ǰ��δ����beginTransaction��ʼ������ʲô������
	 * 
	 * @throws SQLException
	 */
	public void rollbackTransaction() throws SQLException;

	// -------------------------------------------------------------------------

	/**
	 * ȡ�����е���һ��ֵ
	 * 
	 * @param sequenceName ������
	 * @throws SQLException
	 */
	public long getSequenceNextValue( String sequenceName ) throws SQLException;

	// ------------------------------------------------------------------------

	/**
	 * ��õ�ǰʹ�õ����ݿⷽ��
	 */
	public Dialect getDialect();

	/**
	 * ����ִ�ж���DDL��䣬�����û�ָ���ָ����Ͼ�
	 * 
	 * @param ddls ��������DDL�����ַ���
	 * @param delim �ָ�DDL�����ַ���
	 * @return java.sql.Statement.executeBatch()�ķ��ؽ��
	 * @throws SQLException
	 */
	public int[] executeDDL( String ddls, String delim ) throws SQLException;

	/**
	 * ����ִ�ж���DDL��䣬����Ĭ�Ϸָ����Ͼ�
	 * 
	 * @param ddls ��������DDL�����ַ���
	 * @return java.sql.Statement.executeBatch()�ķ��ؽ��
	 * @throws SQLException
	 */
	public int[] executeDDL( String ddls ) throws SQLException;

	// ------------------------------------------------------------------------
	// �߼�����
	
	/**
	 * ���ݱ�����id�����ݿ�������һ����¼��װ��Ϊʵ����󷵻ء�<br>
	 * <br>
	 * ����ñ�δ����ӳ���࣬��Ĭ��ӳ�䵽java.util.Map��<br>
	 * <br>
	 * ���δ������������������ӳ�������Ĭ�ϲ���ͬ��ӳ�䣻<br>
	 * <br>
	 * ע�⣺��ı�ʶ�ֶ����û�Ԥ�����ã�Ҳ�ɶ�̬�޸ġ�
	 * 
	 * @param tablename ����
	 * @param id ��ʶ�ֶε�ֵ
	 * @return ʵ�����POJO��Map��
	 * @throws SQLException
	 */
	public Object load( String tablename, Object id ) throws SQLException;
	
	/**
	 * ���ݸ�����ѯ������ѯָ��������ʵ������б�<br>
	 * <br>
	 * ����ñ�δ����ӳ���࣬��Ĭ��ӳ�䵽java.util.Map��<br>
	 * <br>
	 * ���δ������������������ӳ�������Ĭ�ϲ���ͬ��ӳ��
	 * 
	 * @param tablename ����
	 * @param queryParams ��ѯ���� {���� - ��ֵ} ע�⣺���ܺ�nullֵ
	 * @return bean�ļ��ϣ����˱����mappingClass����Ϊ����ʵ��������ΪMapʵ����
	 * @throws SQLException
	 */
	public List query( String tablename, Map queryParams ) throws SQLException;

	/**
	 * �Ѹ���ʵ�����洢Ϊָ�����һ����¼��<br>
	 * <br>
	 * ע�⣺POJO��ʵ�����ӳ������������ת��Ϊ������Map��ʵ��������ֱ�ӽ���Ϊ������
	 * 
	 * @param tablename ����
	 * @param entity ʵ�����POJO��Map��
	 * @throws SQLException
	 */
	public void store( String tablename, Object entity ) throws SQLException;

	/**
	 * ����ָ�����������ʵ������Ӧ�ı��¼��<br>
	 * <br>
	 * ע�⣺�ô�����̺���Ϊnullֵ�����ԣ�<br>
	 * ��������������ֶΣ���������������޶����������򣬸������з�null��ֵ�����޶�������<br>
	 * POJOʱdirtyProperties�д����������������Mapʱ�������
	 * 
	 * @param tablename ����
	 * @param entity ʵ�����POJO��Map��
	 * @param dirtyProperties �����ԣ�����ʱֻ��������Щ���Զ�Ӧ���У�
	 * @throws SQLException
	 */
	public void update( String tablename, Object entity, String[] dirtyProperties )
			throws SQLException;

}
