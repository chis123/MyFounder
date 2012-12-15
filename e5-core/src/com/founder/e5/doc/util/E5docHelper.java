package com.founder.e5.doc.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.founder.e5.commons.Log;
import com.founder.e5.context.BaseField;
import com.founder.e5.context.Context;
import com.founder.e5.context.DBException;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.BfileException;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.DataType;
import com.founder.e5.db.IBfile;
import com.founder.e5.db.IResultSet;
import com.founder.e5.db.LobHelper;
import com.founder.e5.dom.DocLib;
import com.founder.e5.dom.DocLibReader;
import com.founder.e5.dom.DocTypeField;
import com.founder.e5.dom.DocTypeReader;

/**
 * 文档操作模块辅助类
 * 
 * @created 2005-6-29 9:18:42
 * @author liyanhui
 * @version 1.0
 */
public class E5docHelper {

	private static Log log = Context.getLog( "e5.doc" );

	/**
	 * 关联表表名
	 */
	public static final String DOCRELATION_TABLE = "DOM_RELATIONS";

	/**
	 * 根据文档库ID获得文档库实例
	 * 
	 * @param docLibID 文档库ID
	 * @return 文档库实例
	 * @throws E5Exception
	 */
	public static DocLib getDocLib( int docLibID ) throws E5Exception {
		DocLibReader dlReader = ( DocLibReader ) Context.getBean( DocLibReader.class );
		return dlReader.get( docLibID );
	}

	/**
	 * 根据文档库ID获得文档类型ID
	 * 
	 * @param docLibID 文档库ID
	 * @return 文档类型ID
	 * @throws E5Exception
	 */
	public static int getDocTypeID( int docLibID ) throws E5Exception {
		return getDocLib( docLibID ).getDocTypeID();
	}

	/**
	 * 根据文档库ID获得流程记录表名
	 * 
	 * @param docLibID 文档库ID
	 * @return 流程记录表名
	 * @throws E5Exception
	 */
	public static String getFlowRecordTable( int docLibID ) throws E5Exception {
		DocLibReader dlReader = ( DocLibReader ) Context.getBean( DocLibReader.class );
		return dlReader.getFlowTableInfo( docLibID ).getLibTable();
	}

	/**
	 * 根据文档库ID得到对应数据库的DBSession实例
	 * 
	 * @param docLibID
	 * @return DBSession实例
	 * @throws E5Exception
	 * @created 2005-7-16 21:51:13
	 */
	public static DBSession getDBSession( int docLibID ) throws E5Exception {
		DocLib docLib = getDocLib( docLibID );
		int dsID = docLib.getDsID();
		return Context.getDBSession( dsID );
	}

	/**
	 * 根据文档类型ID、文档字段名获得文档类型字段实例
	 * 
	 * @param docTypeID 文档类型ID
	 * @param fieldName 文档字段名
	 * @return 文档类型字段实例
	 * @throws E5Exception 当e5dom模块抛出异常时
	 */
	public static DocTypeField getDocTypeField( int docTypeID, String fieldName )
			throws E5Exception {
		DocTypeField[] fields = getDocTypeFields( docTypeID );
		for ( int i = 0; i < fields.length; i++ ) {
			if ( fieldName.equalsIgnoreCase( fields[i].getColumnCode() ) )
				return fields[i];
		}
		return null;
	}

	/**
	 * 根据文档类型ID获得该文档类型下所有文档类型字段实例数组
	 * 
	 * @param docTypeID 文档类型ID
	 * @return 文档类型字段实例数组
	 * @throws E5Exception 当e5dom模块抛出异常时
	 */
	public static DocTypeField[] getDocTypeFields( int docTypeID )
			throws E5Exception {
		DocTypeReader dtReader = ( DocTypeReader ) Context.getBean( DocTypeReader.class );
		return dtReader.getFields( docTypeID );
	}

	/**
	 * 取列名在给定范围内的文档类型字段对象数组
	 * 
	 * @param docTypeID 文档类型ID
	 * @param columns 请求字段名数组
	 * @return 文档类型字段对象数组
	 * @throws E5Exception
	 */
	public static DocTypeField[] getDocTypeFields( int docTypeID,
			String[] columns ) throws E5Exception {
		DocTypeReader dtReader = ( DocTypeReader ) Context.getBean( DocTypeReader.class );
		DocTypeField[] all = dtReader.getFields( docTypeID );

		ArrayList list = new ArrayList();
		for ( int i = 0; i < all.length; i++ ) {
			DocTypeField field = all[i];
			if ( containsIgnoreCase( columns, field.getColumnCode() ) )
				list.add( field );
		}
		return ( DocTypeField[] ) list.toArray( new DocTypeField[ list.size() ] );
	}

	private static boolean containsIgnoreCase( String[] ss, String str ) {
		for ( int i = 0; i < ss.length; i++ ) {
			if ( ss[i].equalsIgnoreCase( str ) )
				return true;
		}
		return false;
	}

	/**
	 * 根据字段类型选择合适的方法从IResultSet中取到字段值
	 * 
	 * @param resultSet 结果集
	 * @param docTypeField 字段信息
	 * @return 字段值
	 * @throws SQLException 当数据库异常时
	 * @throws IOException 当读取lob中流操作异常时
	 */
	public static Object getFieldValue( IResultSet rs, DocTypeField docTypeField )
			throws SQLException, IOException {

		String name = docTypeField.getColumnCode();
		String dataType = docTypeField.getDataType();

		Object value = null;

		if ( DataType.INTEGER.equals( dataType ) ) {
			value = new Integer( rs.getInt( name ) );
		} else if ( DataType.LONG.equals( dataType ) ) {
			value = new Long( rs.getLong( name ) );
		} else if ( DataType.FLOAT.equals( dataType ) ) {
			value = new Float( rs.getFloat( name ) );
		} else if ( DataType.DOUBLE.equals( dataType ) ) {
			value = new Double( rs.getDouble( name ) );
		} else if ( DataType.VARCHAR.equals( dataType ) ) {
			value = rs.getString( name );
		} else if ( DataType.DATE.equals( dataType ) ) {
			value = rs.getDate( name );
		} else if ( DataType.TIME.equals( dataType ) ) {
			value = rs.getTime( name );
		} else if ( DataType.TIMESTAMP.equals( dataType ) ) {
			value = rs.getTimestamp( name );
		} else if ( DataType.CLOB.equals( dataType ) ) {
			//value = rs.getClob( name );
			value = LobHelper.getClob( rs, name );
		} else if ( DataType.BLOB.equals( dataType ) ) {
			//value = rs.getBlob( name );
			value = LobHelper.getBlob( rs, name );
		} else if ( DataType.EXTFILE.equals( dataType ) ) {
			value = rs.getBfile( name );
		}

		return value;
	}

	/**
	 * 判断给定数据类型是否Lob类型
	 */
	public static boolean isLobType( String dataType ) {
		return ( DataType.CLOB.equals( dataType ) || DataType.BLOB.equals( dataType ) );
	}

	/**
	 * 上载bfile；该方法请求存储设备管理模块处理上载过程
	 * 
	 * @created 2005-7-16 20:16:37
	 * @param bfile
	 * @throws BfileException
	 * @throws E5Exception
	 * @deprecated replaced by IBfile.store()
	 */
	public static void uploadFile( IBfile bfile ) throws BfileException,
			E5Exception {
		try {
			bfile.store();
		} catch ( Exception e ) {
			throw new E5Exception( e );
		}
	}

	/**
	 * 删除与某个文档相关的所有流程记录
	 * 
	 * @param dbsession DBSession实例
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @return 删除记录条数
	 * @throws E5Exception
	 */
	public static int deleteAssociatedFRs( DBSession dbsession, int docLibID,
			long docID ) throws E5Exception {
		String table = getFlowRecordTable( docLibID );
		StringBuffer sb = new StringBuffer();
		sb.append( "delete from " ).append( table )
			.append( " where DOCLIBID=? and DOCUMENTID=?" );
		String sql = sb.toString();

		if ( log.isDebugEnabled() ) {
			log.debug( "deleteAssociatedFRs with libID=" + docLibID
					+ ", docID=" + docID );
			log.debug( "deleteAssociatedFRs: sql=" + sql );
		}

		Object[] params = { new Integer( docLibID ), new Long( docID ) };
		try {
			return dbsession.executeUpdate( sql, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		}
	}

	/**
	 * 删除与某个文档关联的所有关联关系
	 * 
	 * @param dbsession DBSession实例
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @return 删除记录条数
	 * @throws E5Exception
	 */
	public static int deleteAssociations( DBSession dbsession, int docLibID,
			long docID ) throws E5Exception {
		StringBuffer sb = new StringBuffer();
		sb.append( "delete from " ).append( DOCRELATION_TABLE ).append(
				" where (SOURCEDOCLIBID=? and SOURCEDOCID=?) or " ).append(
				"(DESTDOCLIBID=? and DESTDOCID=?)" );
		String sql = sb.toString();
		Integer _libid = new Integer( docLibID );
		Long _id = new Long( docID );
		Object[] params = { _libid, _id, _libid, _id };

		if ( log.isDebugEnabled() ) {
			log.debug( "deleteAssociations with libID=" + docLibID + ", docID="
					+ docID );
			log.debug( "deleteAssociations: sql=" + sql );
		}

		try {
			return dbsession.executeUpdate( sql, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		}
	}

	// -------------------------------------------------------------------------
	// ------------- 工具方法

	/**
	 * 移去末尾的逗号（如果有的话）
	 * 
	 * @param StringBuffer
	 * @return String
	 * @created 2005-7-26 15:20:52
	 */
	public static String removeTailComma( StringBuffer sb ) {
		int tail = sb.length() - 1;
		if ( sb.charAt( tail ) == ',' )
			sb.deleteCharAt( tail );
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// ------------- 平台字段信息

	public static final String DOCUMENTID = BaseField.DOCUMENTID.getName().toUpperCase();

	public static final String DOCLIBID = BaseField.DOCLIBID.getName().toUpperCase();

	public static final String FOLDERID = BaseField.FOLDERID.getName().toUpperCase();

	public static final String DELETEFLAG = BaseField.DELETEFLAG.getName().toUpperCase();

	public static final String SYSCREATED = BaseField.SYSCREATED.getName().toUpperCase();

	public static final String LASTMODIFIED = BaseField.LASTMODIFIED.getName().toUpperCase();

	public static final String CURRENTFLOW = BaseField.CURRENTFLOW.getName().toUpperCase();

	public static final String ISLOCKED = BaseField.ISLOCKED.getName().toUpperCase();

	public static final String CURRENTNODE = BaseField.CURRENTNODE.getName().toUpperCase();

	public static final String CURRENTSTATUS = BaseField.CURRENTSTATUS.getName().toUpperCase();

	public static final String CURRENTUSERID = BaseField.CURRENTUSERID.getName().toUpperCase();

	public static final String CURRENTUSERNAME = BaseField.CURRENTUSERNAME.getName().toUpperCase();

	public static final String AUTHORS = BaseField.AUTHORS.getName().toUpperCase();

	public static final String TOPIC = BaseField.TOPIC.getName().toUpperCase();

	public static final String HAVERELATION = BaseField.HAVERELATION.getName().toUpperCase();

	public static final String ISKEEP = BaseField.ISKEEP.getName().toUpperCase();

	public static final String HAVEATTACH = BaseField.HAVEATTACH.getName().toUpperCase();

}
