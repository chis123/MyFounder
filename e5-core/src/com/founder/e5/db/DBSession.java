package com.founder.e5.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.founder.e5.db.dialect.Dialect;

/**
 * 数据库会话接口。<br>
 * 
 * DBSession定位为数据库操作门面，旨在通过封装底层jdbc操作细节，来提供给用户一个简单直观的使用接口，完成常用数据库操作。<br>
 * <br>
 * DBSession实例创建出来以后维护一个打开的数据库连接，直到调用close/closeQuietly方法
 * 
 * @author liyanhui
 * @version 1.3
 * @created 29-六月-2005 09:48:25
 */
public interface DBSession {

	// -------------------------------------------------------------------------
	// 查询接口

	/**
	 * 执行查询语句，返回结果集。<br>
	 * <br>
	 * 结果集对象关闭时，会先关闭创建该结果集的语句对象
	 * 
	 * @param sql 查询语句
	 * @return 结果集
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql ) throws SQLException;

	/**
	 * 执行参数化查询语句，返回结果集。<br>
	 * 如果sql语句中不含?，则params可为null； <br>
	 * 结果集对象关闭时，会先关闭创建该结果集的语句对象
	 * 
	 * @param sql 参数化查询语句
	 * @param params 实际参数（数组中不允许有null值）
	 * @return 结果集
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql, Object[] params )
			throws SQLException;

	/**
	 * 执行参数化查询语句，返回结果集。<br>
	 * 如果sql语句中不含?，则params可为null；<br>
	 * 再如果params中没有null值，则types可为null；<br>
	 * 当params中有null值时需要根据types判断字段类型； <br>
	 * 结果集对象关闭时，会先关闭创建该结果集的语句对象
	 * 
	 * @param sql 参数化查询语句
	 * @param params 实际参数
	 * @param types 字段类型，由java.sql.Types中定义的类型码表示
	 * @return 结果集
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types )
			throws SQLException;

	/**
	 * 执行参数化查询语句，返回结果集。<br>
	 * 如果sql语句中不含?，则params可为null；<br>
	 * 再如果params中没有null值，则types可为null；<br>
	 * 当params中有null值时需要根据types判断字段类型； <br>
	 * 结果集对象关闭时，会先关闭创建该结果集的语句对象 <br>
	 * 该方法可指定创建JDBC语句时结果集的类型
	 * 
	 * @param sql 参数化查询语句
	 * @param params 实际参数
	 * @param types 字段类型，由java.sql.Types中定义的类型码表示
	 * @param resultSetType one of the following <code>ResultSet</code>
	 *            constants: <code>ResultSet.TYPE_FORWARD_ONLY</code>,
	 *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
	 *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
	 * @param resultSetConcurrency one of the following <code>ResultSet</code>
	 *            constants: <code>ResultSet.CONCUR_READ_ONLY</code> or
	 *            <code>ResultSet.CONCUR_UPDATABLE</code>
	 * @return 结果集
	 * @exception SQLException
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types,
			int resultSetType, int resultSetConcurrency ) throws SQLException;

	/**
	 * 创建一个数据库查询（或更新、删除）语句
	 * 
	 * @param sql 数据库查询（或更新、删除）语句
	 * @return DBQuery对象
	 */
	public DBQuery createQuery( String sql );

	// -------------------------------------------------------------------------
	// 数据更新接口

	/**
	 * 执行参数化更新语句，返回更新的记录数。<br>
	 * 如果sql语句中不含?，则params可为null；
	 * 
	 * @param sql 参数化查询语句
	 * @param params 实际参数
	 * @return 更新操作影响的行数
	 * @exception SQLException
	 * @throws LaterDataTransferException 涉及lob/bfile插入时可能跑出此异常
	 */
	public int executeUpdate( String sql, Object[] params )
			throws SQLException, LaterDataTransferException;

	/**
	 * 执行参数化更新语句，返回更新的记录数<br>
	 * 如果sql语句中不含?，则params可为null；<br>
	 * 如果params中没有null值，则types可为null；<br>
	 * 当params中有null值时需要根据types判断字段类型
	 * 
	 * @param sql 参数化查询语句
	 * @param params 实际参数
	 * @param types 字段类型，由java.sql.Types中定义的类型码表示
	 * @return 更新操作影响的行数
	 * @exception SQLException
	 * @throws LaterDataTransferException 涉及lob/bfile插入时可能跑出此异常
	 */
	public int executeUpdate( String sql, Object[] params, int[] types )
			throws SQLException, LaterDataTransferException;

	/**
	 * 执行参数化更新语句，返回自动生成的ID（如果有的话）<br>
	 * <br>
	 * 注意：该方法返回值的含义与普通的executeUpdate不同，
	 * 因为考虑到插入（或更新）失败多数会抛出异常，不需靠返回值来标识
	 * 
	 * @param sql 插入语句
	 * @param params 实际参数
	 * @return 若插入语句自动产生ID，则返回之；否则，返回-1
	 * @throws SQLException
	 * @throws LaterDataTransferException
	 */
	public int executeUpdateWithRetrive( String sql, Object[] params )
			throws SQLException, LaterDataTransferException;

	/**
	 * 执行存储过程，返回输出参数值（如无输出则返回空数组）<br>
	 * 如果sql语句中不含?，则params可为null； <br>
	 * 如果待绑定参数中包含null值，则必须提供columnTypes信息；<br>
	 * 如果存储过程包含输出参数，则需通过paramtypes指定；
	 * 
	 * @param sql 参数化sql语句
	 * @param params 待绑定参数值
	 * @param paramTypes 参数（的in/out）类型：in则为真，out则为假
	 * @param columnTypes 字段类型，由java.sql.Types中定义的类型码表示
	 * @exception SQLException
	 */
	public Object[] executeSP( String sql, Object[] params,
			boolean[] paramTypes, int[] columnTypes ) throws SQLException;

	// -------------------------------------------------------------------------
	// 取数据的便利方法

	/**
	 * 查询int型值
	 * 
	 * @exception SQLException
	 */
	public int getInt( String sql, Object[] params ) throws SQLException;

	/**
	 * 查询long型值
	 * 
	 * @param sql
	 * @param params
	 * @exception SQLException
	 */
	public long getLong( String sql, Object[] params ) throws SQLException;

	/**
	 * 查询float型值
	 * 
	 * @exception SQLException
	 */
	public float getFloat( String sql, Object[] params ) throws SQLException;

	/**
	 * 查询double型值
	 * 
	 * @exception SQLException
	 */
	public double getDouble( String sql, Object[] params ) throws SQLException;

	/**
	 * 查询String型值
	 * 
	 * @exception SQLException
	 */
	public String getString( String sql, Object[] params ) throws SQLException;

	/**
	 * 查询Date型值
	 * 
	 * @exception SQLException
	 */
	public java.sql.Date getDate( String sql, Object[] params )
			throws SQLException;

	/**
	 * 查询Time型值
	 * 
	 * @exception SQLException
	 */
	public java.sql.Time getTime( String sql, Object[] params )
			throws SQLException;

	/**
	 * 查询Timestamp型值
	 * 
	 * @exception SQLException
	 */
	public java.sql.Timestamp getTimestamp( String sql, Object[] params )
			throws SQLException;

	/**
	 * 直接取Object类型值。<br>
	 * 实际类型由jdbc规范定义的映射决定
	 * 
	 * @throws SQLException
	 * @created 2005-7-21 15:02:50
	 */
	public Object getObject( String sql, Object[] params ) throws SQLException;

	/**
	 * 查询Clob型值，转为IClob类型对象返回
	 * 
	 * @exception IOException
	 * @exception SQLException
	 */
	public IClob getClob( String sql, Object[] params ) throws SQLException,
			IOException;

	/**
	 * 查询Blob型值，转为IBlob类型对象返回
	 * 
	 * @exception IOException
	 * @exception SQLException
	 */
	public IBlob getBlob( String sql, Object[] params ) throws SQLException,
			IOException;

	/**
	 * 查询IBfile型值。<br>
	 * 注意：目前返回值只包含目录-文件信息，不能取数据。如果需要取数据，可使用e5doc中的
	 * BfileReader包一下再用就可以了。（主要是考虑到e5db模块不能依赖别的模块）
	 * 
	 * @exception SQLException
	 */
	public IBfile getBfile( String sql, Object[] params ) throws SQLException;

	// -------------------------------------------------------------------------

	/**
	 * 写入内容到数据库的Clob类型字段。 <br>
	 * <br>
	 * 注意：该方法必须配合beginTransaction、commitTransaction使用； <br>
	 * 该方法本意是供旧访问方式（先取回一个流，再写入数据，oracle10以前的驱动必须采
	 * 用这种方式）用，目前采用了oracle10的驱动，就不需要这个方法了
	 * 
	 * @param id 记录标识值
	 * @param tablename 数据表名
	 * @param idColumnName 标识字段名
	 * @param lobColumnName Clob字段名
	 * @param clob 待写入内容
	 * @exception IOException
	 * @exception SQLException
	 */
	public void writeClob( long id, String tablename, String idColumnName,
			String lobColumnName, IClob clob ) throws SQLException, IOException;

	/**
	 * 写入内容到数据库的Blob类型字段。<br>
	 * <br>
	 * 注意：该方法必须配合beginTransaction、commitTransaction使用； <br>
	 * 该方法本意是供旧访问方式（先取回一个流，再写入数据，oracle10以前的驱动必须采
	 * 用这种方式）用，目前采用了oracle10的驱动，就不需要这个方法了
	 * 
	 * @param id 记录标识值
	 * @param tablename 数据表名
	 * @param idColumnName 标识字段名
	 * @param lobColumnName Blob字段名
	 * @param blob 待写入内容
	 * @exception IOException
	 * @exception SQLException
	 */
	public void writeBlob( long id, String tablename, String idColumnName,
			String lobColumnName, IBlob blob ) throws SQLException, IOException;

	// -------------------------------------------------------------------------

	/**
	 * 返回当前使用的数据库连接
	 */
	public Connection getConnection();

	/**
	 * 设置当前使用的数据库连接。 <br>
	 * <br>
	 * 注意：设置的数据库连接的自动提交标志会被置为true
	 */
	public void setConnection( Connection conn );

	/**
	 * 关闭底层数据库连接。<br>
	 * 
	 * 一般在finally子句中调用该方法，但要注意需要用try...catch捕获该方法可能跑出的异常，
	 * 否则若该方法抛出异常则会覆盖与finally同级的try语句块中已抛出的异常。<br>
	 * 
	 * 大多数情况下用户无需过多关注调用该方法可能抛出的异常，这时推荐使用closeQuietly()方法。
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException;

	/**
	 * 关闭底层数据库连接，并且不抛出SQLException<br>
	 * 该方法相当于<code>ResourceMgr.closeQuietly( dbSession )</code>
	 */
	public void closeQuietly();

	// -------------------------------------------------------------------------

	/**
	 * 开始一个事务
	 * 
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException;

	/**
	 * 提交当前事务。<br>
	 * 注意：如果之前并未调用beginTransaction开始事务，则什么都不做
	 * 
	 * @throws SQLException
	 */
	public void commitTransaction() throws SQLException;

	/**
	 * 回滚当前事务。<br>
	 * 注意：如果之前并未调用beginTransaction开始事务，则什么都不做
	 * 
	 * @throws SQLException
	 */
	public void rollbackTransaction() throws SQLException;

	// -------------------------------------------------------------------------

	/**
	 * 取得序列的下一个值
	 * 
	 * @param sequenceName 序列名
	 * @throws SQLException
	 */
	public long getSequenceNextValue( String sequenceName ) throws SQLException;

	// ------------------------------------------------------------------------

	/**
	 * 获得当前使用的数据库方言
	 */
	public Dialect getDialect();

	/**
	 * 批量执行多条DDL语句，采用用户指定分隔符断句
	 * 
	 * @param ddls 包含多条DDL语句的字符串
	 * @param delim 分隔DDL语句的字符串
	 * @return java.sql.Statement.executeBatch()的返回结果
	 * @throws SQLException
	 */
	public int[] executeDDL( String ddls, String delim ) throws SQLException;

	/**
	 * 批量执行多条DDL语句，采用默认分隔符断句
	 * 
	 * @param ddls 包含多条DDL语句的字符串
	 * @return java.sql.Statement.executeBatch()的返回结果
	 * @throws SQLException
	 */
	public int[] executeDDL( String ddls ) throws SQLException;

	// ------------------------------------------------------------------------
	// 高级方法
	
	/**
	 * 根据表名和id从数据库中载入一条记录，装配为实体对象返回。<br>
	 * <br>
	 * 如果该表未配置映射类，则默认映射到java.util.Map；<br>
	 * <br>
	 * 如果未配置列名与属性名的映射规则，则默认采用同名映射；<br>
	 * <br>
	 * 注意：表的标识字段由用户预先配置，也可动态修改。
	 * 
	 * @param tablename 表名
	 * @param id 标识字段的值
	 * @return 实体对象（POJO或Map）
	 * @throws SQLException
	 */
	public Object load( String tablename, Object id ) throws SQLException;
	
	/**
	 * 根据给定查询条件查询指定表，返回实体对象列表。<br>
	 * <br>
	 * 如果该表未配置映射类，则默认映射到java.util.Map；<br>
	 * <br>
	 * 如果未配置列名与属性名的映射规则，则默认采用同名映射
	 * 
	 * @param tablename 表名
	 * @param queryParams 查询参数 {列名 - 列值} 注意：不能含null值
	 * @return bean的集合（若此表存在mappingClass，则为该类实例；否则为Map实例）
	 * @throws SQLException
	 */
	public List query( String tablename, Map queryParams ) throws SQLException;

	/**
	 * 把给定实体对象存储为指定表的一条记录。<br>
	 * <br>
	 * 注意：POJO型实体根据映射规则把属性名转换为列名；Map型实体属性名直接解释为列名。
	 * 
	 * @param tablename 表名
	 * @param entity 实体对象（POJO或Map）
	 * @throws SQLException
	 */
	public void store( String tablename, Object entity ) throws SQLException;

	/**
	 * 更新指定表中与给定实体对象对应的表记录。<br>
	 * <br>
	 * 注意：该处理过程忽略为null值的属性；<br>
	 * 如果定义了主键字段，则根据主键生成限定条件；否则，根据所有非null列值生成限定条件；<br>
	 * POJO时dirtyProperties中存放属性名或列名；Map时存放列名
	 * 
	 * @param tablename 表名
	 * @param entity 实体对象（POJO或Map）
	 * @param dirtyProperties 脏属性（更新时只更新与这些属性对应的列）
	 * @throws SQLException
	 */
	public void update( String tablename, Object entity, String[] dirtyProperties )
			throws SQLException;

}
