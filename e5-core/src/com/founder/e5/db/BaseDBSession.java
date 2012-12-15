package com.founder.e5.db;

import gnu.trove.TIntArrayList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.founder.e5.db.dialect.Dialect;
import com.founder.e5.db.util.CloseHelper;
import com.founder.e5.db.util.StringUtils;

/**
 * DBSession的基本实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 29-六月-2005 10:00:08
 */
public class BaseDBSession implements DBSession {

	protected static Log log = LogFactory.getLog("e5");

	protected Connection conn;

	public Connection getConnection() {
		return this.conn;
	}

	public void setConnection( Connection conn ) {
		this.conn = conn;

		// 默认应该自动提交
		try {
			this.conn.setAutoCommit( true );
		} catch ( SQLException e ) {
			log.error( e );
		}
	}

	/**
	 * 当前使用的数据库方言
	 */
	protected Dialect dialect;

	/**
	 */
	public BaseDBSession() {
	}

	/**
	 * 构造DBSession实例，保存Connection对象供后续使用；同时根据
	 * DataBaseMetaData猜测数据库类型，若成功则自动获取相应的方言
	 * 
	 * @param conn
	 */
	public BaseDBSession( Connection conn ) {
		if ( conn == null )
			throw new NullPointerException();
		this.conn = conn;
		try {
			String dbType = DBType.guessDBType( conn );
			this.dialect = Dialect.getDialect( dbType );
		} catch ( SQLException e ) {
			log.error( "", e );
		}
	}

	/**
	 * @param dialect
	 */
	public BaseDBSession( Dialect dialect ) {
		this.dialect = dialect;
	}

	public void setDialect( Dialect dialect ) {
		this.dialect = dialect;
	}

	// -----------------------------------------------------------------------

	private static final Object[] EMPTY_ARRAY_1D = new Object[ 0 ];

	private static final int[] EMTPY_ARRAY_INT = new int[ 0 ];

	/**
	 * 默认情况下采用的DDL语句分隔符
	 */
	public static final String DEFAULT_DDL_DELIMITER = "--==--";

	// -----------------------------------------------------------------------

	/**
	 * 填充参数值到语句中。<br>
	 * 判断字段类型时首先检查参数值是否为null，若不为null则根据参数类型确定字段类型；否则查询types数组得到类型码
	 * 
	 * @param pst 待填充语句（不可空）
	 * @param params 待绑定参数值（可空）
	 * @param types 参数类型（参数值全非空时可空）
	 * @throws SQLException
	 */
	protected PreparedStatement fillStatement( PreparedStatement pst,
			Object[] params, int[] types ) throws SQLException {

		if ( params != null ) {
			int nextIndex = 1; // 下一个待绑定参数在语句中的位置

			for ( int i = 0; i < params.length; i++ ) {
				Object obj = params[i];

				if ( obj == null ) {
					if ( types == null ) {
						String msg = "Type required when param is null!";
						throw new NullPointerException( msg );
					}

					pst.setNull( nextIndex++, types[i] );
				}

				else {
					if ( types != null ) {
						nextIndex = bindParam( pst, nextIndex, obj, types[i] );
					} else {
						nextIndex = bindParam( pst, nextIndex, obj );
					}
				}
			}
		}

		return pst;
	}

	/**
	 * 绑定参数值到语句对象中（该版本根据实参类型决定绑定类型）
	 * 
	 * @param pst 语句对象
	 * @param index 参数位置
	 * @param obj 参数值
	 * @return 语句中下一个待绑定参数的索引
	 * @throws SQLException
	 */
	protected int bindParam( PreparedStatement pst, int index, Object obj )
			throws SQLException {

		String logValue = null;
		int logIndex = 0;
		boolean alreadyLoged = false;

		if ( obj instanceof Integer ) {
			int value = ( ( Integer ) obj ).intValue();
			logIndex = index;
			logValue = String.valueOf( value );

			pst.setInt( index++, value );
		}

		else if ( obj instanceof Long ) {
			long value = ( ( Long ) obj ).longValue();
			logIndex = index;
			logValue = String.valueOf( value );

			pst.setLong( index++, value );
		}

		else if ( obj instanceof Float ) {
			float value = ( ( Float ) obj ).floatValue();
			logIndex = index;
			logValue = String.valueOf( value );

			pst.setFloat( index++, value );
		}

		else if ( obj instanceof Double ) {
			double value = ( ( Double ) obj ).doubleValue();
			logIndex = index;
			logValue = String.valueOf( value );

			pst.setDouble( index++, value );
		}

		else if ( obj instanceof String ) {
			logIndex = index;
			logValue = String.valueOf( obj );

			pst.setString( index++, ( String ) obj );
		}

		else if ( obj instanceof Date ) {
			logIndex = index;
			logValue = String.valueOf( obj );

			pst.setDate( index++, ( Date ) obj );
		}

		else if ( obj instanceof Time ) {
			logIndex = index;
			logValue = String.valueOf( obj );

			pst.setTime( index++, ( Time ) obj );
		}

		else if ( obj instanceof Timestamp ) {
			logIndex = index;
			logValue = String.valueOf( obj );

			pst.setTimestamp( index++, ( Timestamp ) obj );
		}

		else if ( obj instanceof java.util.Date ) {
			logIndex = index;
			logValue = String.valueOf( obj );

			pst.setDate( index++, convert( ( java.util.Date ) obj ) );
		}

		else if ( obj instanceof IClob ) {
			logIndex = index;
			logValue = "IClob#" + obj.hashCode();

			IClob clob = ( IClob ) obj;
			pst.setCharacterStream( index++, clob.getReader(), clob.length() );
		}

		else if ( obj instanceof IBlob ) {
			logIndex = index;
			logValue = "IBlob#" + obj.hashCode();

			IBlob blob = ( IBlob ) obj;
			pst.setBinaryStream(
					index++,
					blob.getStream(),
					( int ) blob.length() );
		}

		else if ( obj instanceof IBfile ) {
			index = ( ( IBfile ) obj ).setParameter( pst, index );
			alreadyLoged = true;
		}

		else {
			logIndex = index;
			logValue = String.valueOf( obj );

			pst.setObject( index++, obj );
		}

		if ( !alreadyLoged && log.isDebugEnabled() ) {
			log.debug( "binding parameter #" + logIndex + " : " + logValue );
		}

		return index;
	}

	/**
	 * 绑定参数值到语句对象中（该版本根据指定的sql类型决定绑定类型）
	 * 
	 * @param pst 语句对象
	 * @param index 参数位置
	 * @param obj 参数值
	 * @param type 参数的sql类型
	 * @return 语句中下一个待绑定参数的索引
	 * @throws SQLException
	 */
	protected int bindParam( PreparedStatement pst, int index, Object obj,
			int type ) throws SQLException {

		String logValue = null;
		int logIndex = 0;
		boolean alreadyLoged = false;

		switch ( type ) {

			case Types.INTEGER : {
				int value = ( ( Integer ) obj ).intValue();
				logIndex = index;
				logValue = String.valueOf( value );

				pst.setInt( index++, value );
				break;
			}

			case Types.BIGINT : {
				long value = ( ( Long ) obj ).longValue();
				logIndex = index;
				logValue = String.valueOf( value );

				pst.setLong( index++, value );
				break;
			}

			case Types.REAL :
			case Types.FLOAT : {
				float value = ( ( Float ) obj ).floatValue();
				logIndex = index;
				logValue = String.valueOf( value );

				pst.setFloat( index++, value );
				break;
			}

			case Types.DOUBLE : {
				double value = ( ( Double ) obj ).doubleValue();
				logIndex = index;
				logValue = String.valueOf( value );

				pst.setDouble( index++, value );
				break;
			}

			case Types.VARCHAR : {
				logIndex = index;
				logValue = String.valueOf( obj );

				pst.setString( index++, ( String ) obj );
				break;
			}

			case Types.TIMESTAMP : {
				logIndex = index;
				logValue = String.valueOf( obj );

				pst.setTimestamp( index++, ( Timestamp ) obj );
				break;
			}

			case Types.DATE : {
				logIndex = index;
				logValue = String.valueOf( obj );

				pst.setDate( index++, convert( ( java.util.Date ) obj ) );
				break;
			}

			case Types.TIME : {
				logIndex = index;
				logValue = String.valueOf( obj );

				pst.setTime( index++, ( Time ) obj );
				break;
			}

			case Types.CLOB : {
				logIndex = index;
				Reader param = null;
				int length = 0;

				if ( obj instanceof IClob ) {
					logValue = "IClob#" + obj.hashCode();

					IClob clob = ( IClob ) obj;
					param = clob.getReader();
					length = clob.length();
				} else {
					logValue = obj.toString();
					param = new StringReader( logValue );
					length = logValue.length();
				}

				pst.setCharacterStream( index++, param, length );
				break;
			}

			case Types.BLOB : {
				logIndex = index;
				InputStream param = null;
				int length = 0;

				if ( obj instanceof IBlob ) {
					logValue = "IBlob#" + obj.hashCode();

					IBlob blob = ( IBlob ) obj;
					param = blob.getStream();
					length = ( int ) blob.length();
				}

				else if ( obj instanceof byte[] ) {
					logValue = "byte[]#" + obj.hashCode();

					byte[] bytes = ( byte[] ) obj;
					param = new ByteArrayInputStream( bytes );
					length = bytes.length;
				}

				else
					throw new IllegalArgumentException( "Illegal type ["
							+ obj.getClass() + "] for Blob!" );

				pst.setBinaryStream( index++, param, length );
				break;
			}

			case Types.OTHER : {
				if ( obj instanceof IBfile ) {
					index = ( ( IBfile ) obj ).setParameter( pst, index );
					alreadyLoged = true;
				}
				else
				{
					logIndex = index;
					logValue = String.valueOf( obj );
	
					pst.setObject( index++, obj );
				}
				break;
			}

			default : {
				logIndex = index;
				logValue = String.valueOf( obj );

				pst.setObject( index++, obj );
				break;
			}
		}

		if ( !alreadyLoged && log.isDebugEnabled() ) {
			log.debug( "binding parameter #" + logIndex + " : " + logValue );
		}

		return index;
	}

	private static java.sql.Date convert( java.util.Date date ) {
		return new java.sql.Date( date.getTime() );
	}

	/**
	 * 执行参数化查询，返回结果集。<br>
	 * 如果sql语句中不含?，则params可为null；<br>
	 * 如果params中不含null值，则types可为null; <br>
	 * 注意：使用者用完ResultSet后必须先关闭ResultSet，然后通过 ResultSet.getStatement()得到父语句对象然后关闭
	 * 
	 * @param sql 参数化查询语句
	 * @param params 待绑定参数数组
	 * @param types 字段类型，由java.sql.Types中定义的类型码表示
	 * @param resultSetType one of the following <code>ResultSet</code>
	 *            constants: <code>ResultSet.TYPE_FORWARD_ONLY</code>,
	 *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
	 *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
	 * @param resultSetConcurrency one of the following <code>ResultSet</code>
	 *            constants: <code>ResultSet.CONCUR_READ_ONLY</code> or
	 *            <code>ResultSet.CONCUR_UPDATABLE</code>
	 * @return 结果集
	 * @throws SQLException
	 * @created 2005-7-18 13:18:14
	 */
	protected ResultSet executeQuery0( String sql, Object[] params,
			int[] types, int resultSetType, int resultSetConcurrency )
			throws SQLException {
		if ( log.isDebugEnabled() )
			log.debug( "executeQuery0: sql=" + sql );

		PreparedStatement pst = conn.prepareStatement(
				sql,
				resultSetType,
				resultSetConcurrency );
		fillStatement( pst, params, types );
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	/**
	 * 执行参数化查询，返回结果集。<br>
	 * 如果sql语句中不含?，则params可为null；<br>
	 * 如果params中不含null值，则types可为null; <br>
	 * 注意：使用者用完ResultSet后必须先关闭ResultSet，然后通过 ResultSet.getStatement()得到父语句对象然后关闭
	 * 
	 * @param sql 参数化查询语句
	 * @param params 待绑定参数数组
	 * @param types 字段类型，由java.sql.Types中定义的类型码表示
	 * @return 结果集
	 * @throws SQLException
	 * @created 2005-7-18 13:18:14
	 */
	protected ResultSet executeQuery0( String sql, Object[] params, int[] types )
			throws SQLException {
		if ( log.isDebugEnabled() )
			log.debug( "executeQuery0: sql=" + sql );

		PreparedStatement pst = conn.prepareStatement( sql );
		fillStatement( pst, params, types );
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	// ------------------------------------------------------------------------

	/**
	 * @see com.founder.e5.db.DBSession#executeQuery(java.lang.String)
	 */
	public IResultSet executeQuery( String sql ) throws SQLException {
		return executeQuery( sql, null, null );
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeQuery(java.lang.String,
	 *      java.lang.Object[])
	 */
	public IResultSet executeQuery( String sql, Object[] params )
			throws SQLException {
		return executeQuery( sql, params, null );
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeQuery(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types )
			throws SQLException {
		ResultSet rs = executeQuery0( sql, params, types );
		return BaseResultSet.createResultSet( rs );
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeQuery(java.lang.String,
	 *      java.lang.Object[], int[], int, int)
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types,
			int resultSetType, int resultSetConcurrency ) throws SQLException {
		ResultSet rs = executeQuery0(
				sql,
				params,
				types,
				resultSetType,
				resultSetConcurrency );
		return BaseResultSet.createResultSet( rs );
	}

	// ------------------------------------------------------------------------

	/**
	 * @see com.founder.e5.db.DBSession#executeDDL(java.lang.String,
	 *      java.lang.String)
	 */
	public int[] executeDDL( String ddls, String delim ) throws SQLException {
		delim = ( delim == null ? DEFAULT_DDL_DELIMITER : delim );
		String[] sql = StringUtils.split( ddls, delim );

		if ( sql != null ) {
			int[] rt = new int[ sql.length ];
			beginTransaction();

			try {
				for ( int i = 0; i < sql.length; i++ ) {
					if ( !StringUtils.isBlank( sql[i] ) ) {
						String _sql = sql[i];
						_sql = _sql.replaceAll( "\r", "" );

						Statement stmt = conn.createStatement();
						rt[i] = stmt.executeUpdate( _sql );
						stmt.close();

						if ( log.isDebugEnabled() )
							log.debug( "executeDDL: sql(" + ( i + 1 ) + ")="
									+ _sql );
					}
				}

				commitTransaction();

			} finally {
				rollbackTransaction();
			}

			return rt;
		}

		return EMTPY_ARRAY_INT;
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeDDL(java.lang.String)
	 */
	public int[] executeDDL( String ddls ) throws SQLException {
		executeDDL( ddls, DEFAULT_DDL_DELIMITER );
		return EMTPY_ARRAY_INT;
	}

	// ------------------------------------------------------------------------

	/**
	 * 执行参数化更新语句，返回更新的记录数
	 * 
	 * @exception SQLException, LaterDataTransferException
	 */
	public int executeUpdate( String sql, Object[] params ) throws SQLException {
		return executeUpdate( sql, params, null );
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeUpdate(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public int executeUpdate( String sql, Object[] params, int[] types )
			throws SQLException, LaterDataTransferException {

		int result = 0;
		String replacedSql = sql;
		ArrayList bfiles = new ArrayList(); // 保存bfile类型值，以备后续上传数据

		// 若有bfile，则以其实际占位符（如在Oracle下为：bfilename(dir, name)）替换通用
		// 占位符"?"，生成新的sql语句；若有SqlValue，则以其sql表达式替换“?”

		ArrayList newParams = new ArrayList();
		TIntArrayList newTypes = new TIntArrayList();

		if ( params != null ) {
			replacedSql = preProcessSql(
					replacedSql,
					params,
					newParams,
					types,
					newTypes,
					bfiles );
		}

		params = newParams.toArray();
		if ( types != null )
			types = newTypes.toNativeArray();

		if ( log.isDebugEnabled() ) {
			log.debug( "executeUpdate: sql=" + replacedSql );
		}

		// 执行sql更新
		PreparedStatement pst = conn.prepareStatement( replacedSql );
		try {
			fillStatement( pst, params, types );
			result = pst.executeUpdate();
		} finally {
			CloseHelper.closeQuietly( pst );
		}
		if ( log.isDebugEnabled() )
			log.debug( "executeUpdate: begin to deal with Bfile!");

		// 若有bfile，则传输其数据
		for ( Iterator i = bfiles.iterator(); i.hasNext(); ) {
			IBfile bfile = ( IBfile ) i.next();

			try {
				bfile.store();
			} catch ( Exception e ) {
				throw new LaterDataTransferException( e );
			}
		}

		return result;
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeUpdateWithRetrive(java.lang.String,
	 *      java.lang.Object[])
	 */
	public int executeUpdateWithRetrive( String sql, Object[] params )
			throws SQLException, LaterDataTransferException {

		int result = 0;
		String replacedSql = sql;
		ArrayList bfiles = new ArrayList(); // 保存bfile类型值，以备后续上传数据

		// 若有bfile，则以其实际占位符（如在Oracle下为：bfilename(dir, name)）替换通用
		// 占位符"?"，生成新的sql语句；若有SqlValue，则以其sql表达式替换“?”

		ArrayList newParams = new ArrayList();

		if ( params != null ) {
			replacedSql = preProcessSql(
					replacedSql,
					params,
					newParams,
					null,
					null,
					bfiles );
		}

		params = newParams.toArray();

		if ( log.isDebugEnabled() ) {
			log.debug( "executeUpdate: sql=" + replacedSql );
		}

		// 执行sql更新
		PreparedStatement pst = conn.prepareStatement(
				replacedSql,
				Statement.RETURN_GENERATED_KEYS );
		try {
			fillStatement( pst, params, null );
			pst.executeUpdate();

			ResultSet rs = pst.getGeneratedKeys();
			if ( rs != null ) {
				if ( rs.next() ) {
					result = rs.getInt( 1 );
				}
				rs.close();
			}
		} finally {
			CloseHelper.closeQuietly( pst );
		}

		// 若有bfile，则传输其数据
		for ( Iterator i = bfiles.iterator(); i.hasNext(); ) {
			IBfile bfile = ( IBfile ) i.next();

			try {
				bfile.store();
			} catch ( Exception e ) {
				throw new LaterDataTransferException( e );
			}
		}

		return result;
	}

	/**
	 * 根据实参对sql语句进行预处理，包括：如有SqlValue，则以其sql值替换？；如有IBfile，则以其placeHolder字符串替换？
	 * 
	 * @param sql
	 * @param params 处理过程中从中删除SqlValue类型对象（OUT型参数）
	 * @param newParams 删除SqlValue类型对象后的参数列表（OUT型参数）
	 * @param bfiles 如有IBfile值，则存入该参数（OUT型参数）
	 * @return 实施替换后的sql语句
	 */
	private static String preProcessSql( String sql, Object[] params,
			ArrayList newParams, int[] types, TIntArrayList newTypes,
			ArrayList bfiles ) {

		TIntArrayList positions = new TIntArrayList(); // 保存待替换的问号的序号
		ArrayList replacements = new ArrayList(); // 保存待替换的目标字符串

		// 若有bfile类型字段，则记录其位置和实际占位符；若有SqlValue类型值，同样记录
		for ( int i = 0; i < params.length; i++ ) {
			Object object = params[i];

			if ( object instanceof IBfile ) {
				IBfile bfile = ( IBfile ) object;
				bfiles.add( bfile );

				positions.add( i + 1 ); // 记录bfile在params数组中的序号（从1开始计数）
				replacements.add( bfile.getPlaceHolder() );

				newParams.add( object );
				if ( types != null )
					newTypes.add( types[i] );
			}

			else if ( object instanceof SqlValue ) {
				SqlValue sqlValue = ( SqlValue ) object;
				positions.add( i + 1 );
				replacements.add( sqlValue.toSqlString() );
			}

			else {
				newParams.add( object );
				if ( types != null )
					newTypes.add( types[i] );
			}

		}

		String replacedSql = replacePlaceHolder(
				sql,
				positions.toNativeArray(),
				( String[] ) replacements.toArray( new String[ replacements.size() ] ) );

		return replacedSql;
	}

	/**
	 * 取得第num个问号在给定字符串中的索引
	 * 
	 * @param sql语句
	 * @param num 问号的序号
	 * @return
	 */
	static int getIndexForPlaceHolder( String sql, int num ) {
		int cnt = 0;
		for ( int i = 0; i < sql.length(); i++ ) {
			if ( sql.charAt( i ) == '?' ) {
				if ( ++cnt == num )
					return i;
			}
		}

		throw new IllegalStateException( "The #" + num
				+ " placeholder[?] doesn't exist in sql[" + sql + "]" );
	}

	/**
	 * positions[0]表示?在sql语句中的序号，把该?替换为replacements[0]；依此类推，把positions中指定了序号的所有?替换完毕返回
	 * 
	 * @param sql sql语句
	 * @param positions ?在sql语句中的序号组成的数组
	 * @param replacements 以之替换相应?的字符串组成的数组
	 * @return 替换了相应？后的sql语句
	 */
	static String replacePlaceHolder( String sql, int[] positions,
			String[] replacements ) {
		if ( positions.length == 0 )
			return sql;

		StringBuffer result = new StringBuffer();
		int lastPos = 0;

		for ( int i = 0; i < positions.length; i++ ) {
			int num = positions[i];
			String replacement = replacements[i];

			int index = getIndexForPlaceHolder( sql, num );
			result.append( sql.substring( lastPos, index ) );
			result.append( replacement );
			lastPos = index + 1;
		}

		if ( lastPos < sql.length() )
			result.append( sql.substring( lastPos ) );

		return result.toString();
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeSP(java.lang.String,
	 *      java.lang.Object[], boolean[], int[])
	 */
	public Object[] executeSP( String sql, Object[] params,
			boolean[] paramTypes, int[] columnTypes ) throws SQLException {

		CallableStatement cst = conn.prepareCall( sql );

		// 无out型参数
		if ( paramTypes == null ) {
			fillStatement( cst, params, columnTypes );
		}

		// 可能有out型参数
		else {
			for ( int i = 0; i < paramTypes.length; i++ ) {
				boolean in = paramTypes[i];
				int pos = i + 1;
				Object obj = params[i];

				// in型参数
				if ( in ) {
					if ( obj == null ) {
						cst.setNull( pos, columnTypes[i] );
					} else {
						bindParam( cst, pos, obj );
					}
				}

				// out型参数
				else {
					cst.registerOutParameter( pos, columnTypes[i] );
				}
			}
		}

		cst.executeUpdate();

		ArrayList out = new ArrayList();
		for ( int i = 0; i < paramTypes.length; i++ ) {

			// 取回out型参数的值
			if ( !paramTypes[i] ) {
				int pos = i + 1;
				Object value;

				switch ( columnTypes[i] ) {
					case Types.INTEGER :
						value = new Integer( cst.getInt( pos ) );
						break;
					case Types.BIGINT :
						value = new Long( cst.getLong( pos ) );
						break;
					case Types.FLOAT :
						value = new Float( cst.getFloat( pos ) );
						break;
					case Types.DOUBLE :
						value = new Double( cst.getDouble( pos ) );
						break;
					case Types.VARCHAR :
						value = cst.getString( pos );
						break;
					case Types.DATE :
						value = cst.getDate( pos );
						break;
					case Types.TIME :
						value = cst.getTime( pos );
						break;
					case Types.TIMESTAMP :
						value = cst.getTimestamp( pos );
						break;
					case Types.CLOB :
						value = cst.getClob( pos );
						break;
					case Types.BLOB :
						value = cst.getBlob( pos );
						break;

					default : {
						value = cst.getObject( pos );
					}
				}

				out.add( value );
			}
		}
		cst.close();
		
		int size = out.size();
		if ( size == 0 )
			return EMPTY_ARRAY_1D;
		return out.toArray( new Object[ size ] );
	}

	/**
	 * @see com.founder.e5.db.DBSession#createQuery(java.lang.String)
	 */
	public DBQuery createQuery( String sql ) {
		return BaseDBQuery.getInstance( this, sql );
	}

	// -------------------------------------------------------------------------

	// javadoc copied from their interface

	// 为提高性能，不使用queryAndFetch

	public int getInt( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getInt( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return 0;
	}

	public long getLong( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getLong( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return 0;
	}

	public float getFloat( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getFloat( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return 0;
	}

	public double getDouble( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getDouble( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return 0;
	}

	public String getString( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getString( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return null;
	}

	public java.sql.Date getDate( String sql, Object[] params )
			throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getDate( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return null;
	}

	public java.sql.Time getTime( String sql, Object[] params )
			throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getTime( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return null;
	}

	public java.sql.Timestamp getTimestamp( String sql, Object[] params )
			throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getTimestamp( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return null;
	}

	public Object getObject( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				return rs.getObject( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs.getStatement() );
			CloseHelper.closeQuietly( rs );
		}
		return null;
	}

	public IClob getClob( String sql, Object[] params ) throws SQLException,
			IOException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() )
				return LobHelper.getClob( rs, 1 );
		} finally {
			Statement pmt = rs.getStatement();
			CloseHelper.closeQuietly( rs );
			CloseHelper.closeQuietly( pmt );
		}
		return null;
	}

	public IBlob getBlob( String sql, Object[] params ) throws SQLException,
			IOException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() )
				return LobHelper.getBlob( rs, 1 );
		} finally {
			Statement pmt = rs.getStatement();
			CloseHelper.closeQuietly( rs );
			CloseHelper.closeQuietly( pmt );
		}
		return null;
	}

	public IBfile getBfile( String sql, Object[] params ) throws SQLException {
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() ) {
				Object value = rs.getObject( 1 );
				if ( value != null )
					return BfileFactoryManager.convert( value );
			}
		} finally {
			Statement pmt = rs.getStatement();
			CloseHelper.closeQuietly( rs );
			CloseHelper.closeQuietly( pmt );
		}
		return null;
	}

	// -------------------------------------------------------------------------

	/**
	 * 开始一个事务
	 * 
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException {
		conn.setAutoCommit( false );
	}

	/**
	 * 提交当前事务<br>
	 * 注意：提交后数据库的自动提交模式仍为false
	 */
	public void commitTransaction() throws SQLException {
		conn.commit();
	}

	/**
	 * 回滚当前事务<br>
	 * 注意：提交后数据库的自动提交模式仍为false
	 */
	public void rollbackTransaction() throws SQLException {
		conn.rollback();
	}

	// -------------------------------------------------------------------------

	/**
	 * @see com.founder.e5.db.DBSession#close()
	 */
	public void close() throws SQLException {
		if ( conn != null )
			conn.close();
	}

	/**
	 * @see com.founder.e5.db.DBSession#closeQuietly()
	 */
	public void closeQuietly() {
		CloseHelper.closeQuietly( conn );
	}

	/**
	 * @see com.founder.e5.db.DBSession#getSequenceNextValue(java.lang.String)
	 */
	public long getSequenceNextValue( String sequenceName ) throws SQLException {
		String sql = dialect.getSelectSequenceNextValString( sequenceName );
		return getLong( sql, null );
	}

	// -------------------------------------------------------------------------

	/**
	 * @see com.founder.e5.db.DBSession#getDialect()
	 * @created 2006-3-29 17:31:02
	 */
	public Dialect getDialect() {
		return dialect;
	}

	// -------------------------------------------------------------------------

	/**
	 * @see com.founder.e5.db.DBSession#writeBlob(long, java.lang.String,
	 *      java.lang.String, java.lang.String, com.founder.e5.db.IBlob)
	 */
	public void writeBlob( long id, String tablename, String idColumnName,
			String lobColumnName, IBlob blob ) throws SQLException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append( "update " ).append( tablename ).append( " set " ).append(
				lobColumnName ).append( " = ? where " ).append( idColumnName ).append(
				" = ?" );
		String sql = sb.toString();

		if ( log.isDebugEnabled() )
			log.debug( "writeBlob: sql=" + sql );

		Object[] params = { blob, new Long( id ) };
		executeUpdate( sql, params );
	}

	/**
	 * @see com.founder.e5.db.DBSession#writeClob(long, java.lang.String,
	 *      java.lang.String, java.lang.String, com.founder.e5.db.IClob)
	 */
	public void writeClob( long id, String tablename, String idColumnName,
			String lobColumnName, IClob clob ) throws SQLException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append( "update " ).append( tablename ).append( " set " ).append(
				lobColumnName ).append( " = ? where " ).append( idColumnName ).append(
				" = ?" );
		String sql = sb.toString();

		if ( log.isDebugEnabled() )
			log.debug( "writeClob: sql=" + sql );

		Object[] params = { clob, new Long( id ) };
		executeUpdate( sql, params );
	}

	// ------------------------------------------------------------------------

	/**
	 * @see com.founder.e5.db.DBSession#load(java.lang.String, java.lang.Object)
	 */
	public Object load( String tablename, Object id ) throws SQLException {
		return MyPersister.load( this, tablename, id );
	}

	/**
	 * @see com.founder.e5.db.DBSession#query(java.lang.String, java.util.Map)
	 */
	public List query( String tablename, Map queryParams ) throws SQLException {
		return MyPersister.query( this, tablename, queryParams );
	}

	/**
	 * @see com.founder.e5.db.DBSession#store(java.lang.String,
	 *      java.lang.Object)
	 */
	public void store( String tablename, Object valueObject )
			throws SQLException {
		MyPersister.store( this, tablename, valueObject );
	}

	/**
	 * @see com.founder.e5.db.DBSession#update(java.lang.String,
	 *      java.lang.Object, java.lang.String[])
	 */
	public void update( String tablename, Object entity,
			String[] dirtyProperties ) throws SQLException {
		MyPersister.update( this, tablename, entity, dirtyProperties );
	}

}
