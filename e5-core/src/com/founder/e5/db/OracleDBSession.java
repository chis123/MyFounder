package com.founder.e5.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.CopyUtils;

import com.founder.e5.db.dialect.OracleDialect;
import com.founder.e5.db.util.CloseHelper;

/**
 * DBSession的Oracle实现。<br>
 * 该类覆盖了父类对clob/blob读写的方法，提供对使用oracle9及以前版本driver访问lob的支持
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-13 10:51:55
 */
public class OracleDBSession extends BaseDBSession {

	/**
	 * 
	 */
	public OracleDBSession() {
		super();
		this.dialect = new OracleDialect();
	}

	// 自己用的方法
	private Object queryAndFetchObj( String sql, Object[] params )
			throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement( sql );
			fillStatement( pst, params, null );
			rs = pst.executeQuery();

			if ( rs.next() ) {
				return rs.getObject( 1 );
			}
		} finally {
			CloseHelper.closeQuietly( rs );
			CloseHelper.closeQuietly( pst );
		}
		return null;
	}

	/**
	 * 注意：该方法必须配合beginTransaction、commitTransaction使用
	 * 
	 * @see com.founder.e5.db.DBSession#writeClob(long, java.lang.String,
	 *      String, java.lang.String, com.founder.e5.db.IClob)
	 */
	public void writeClob( long id, String tablename, String idColumnName,
			String lobColumnName, IClob clob ) throws SQLException, IOException {

		if ( tablename == null || idColumnName == null || lobColumnName == null
				|| clob == null )
			throw new NullPointerException();

		Long ID = new Long( id );

		StringBuffer sb = new StringBuffer();
		sb.append( "select " ).append( lobColumnName ).append( " from " );
		sb.append( tablename ).append( " where " ).append( idColumnName ).append(
				"=?" );
		sb.append( " for update" );

		Object obj = queryAndFetchObj( sb.toString(), new Object[] { ID } );

		// 如果此时lob字段为空，须先插入空值
		if ( obj == null ) {
			conn.setAutoCommit( false );

			StringBuffer sb2 = new StringBuffer();
			sb2.append( "update " ).append( tablename ).append( " set " );
			sb2.append( lobColumnName ).append( "=empty_clob() where " );
			sb2.append( idColumnName ).append( "=?" );

			// 插入空clob值
			executeUpdate( sb2.toString(), new Object[] { ID } );

			// 再取回来
			obj = queryAndFetchObj( sb.toString(), new Object[] { ID } );
		}

		// 写入数据库
		oracle.sql.CLOB out = ( oracle.sql.CLOB ) obj;
		Reader reader = null;
		Writer writer = null;
		try {
			reader = clob.getReader();
			writer = out.getCharacterOutputStream();
			CopyUtils.copy( reader, writer );
		} finally {
			CloseHelper.closeQuietly( reader );
			CloseHelper.closeQuietly( writer );
		}

	}

	/**
	 * 注意：该方法必须配合beginTransaction、commitTransaction使用
	 * 
	 * @see com.founder.e5.db.DBSession#writeBlob(long, java.lang.String,
	 *      String, java.lang.String, com.founder.e5.db.IBlob)
	 */
	public void writeBlob( long id, String tablename, String idColumnName,
			String lobColumnName, IBlob blob ) throws SQLException, IOException {

		if ( tablename == null || idColumnName == null || lobColumnName == null
				|| blob == null )
			throw new NullPointerException();

		Long ID = new Long( id );

		StringBuffer sb = new StringBuffer();
		sb.append( "select " ).append( lobColumnName ).append( " from " );
		sb.append( tablename ).append( " where " ).append( idColumnName ).append(
				"=?" );
		sb.append( " for update" );

		Object obj = queryAndFetchObj( sb.toString(), new Object[] { ID } );

		// 如果此时lob字段为空，须先插入空值
		if ( obj == null ) {
			conn.setAutoCommit( false );

			StringBuffer sb2 = new StringBuffer();
			sb2.append( "update " ).append( tablename ).append( " set " );
			sb2.append( lobColumnName ).append( "=empty_blob() where " );
			sb2.append( idColumnName ).append( "=?" );

			// 插入空blob值
			executeUpdate( sb2.toString(), new Object[] { ID } );

			// 再取回来
			obj = queryAndFetchObj( sb.toString(), new Object[] { ID } );
		}

		// 写入数据库
		oracle.sql.BLOB out = ( oracle.sql.BLOB ) obj;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = blob.getStream();
			os = out.getBinaryOutputStream();
			CopyUtils.copy( is, os );
		} finally {
			CloseHelper.closeQuietly( is );
			CloseHelper.closeQuietly( os );
		}

	}

}
