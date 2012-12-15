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
 * �ĵ�����ģ�鸨����
 * 
 * @created 2005-6-29 9:18:42
 * @author liyanhui
 * @version 1.0
 */
public class E5docHelper {

	private static Log log = Context.getLog( "e5.doc" );

	/**
	 * ���������
	 */
	public static final String DOCRELATION_TABLE = "DOM_RELATIONS";

	/**
	 * �����ĵ���ID����ĵ���ʵ��
	 * 
	 * @param docLibID �ĵ���ID
	 * @return �ĵ���ʵ��
	 * @throws E5Exception
	 */
	public static DocLib getDocLib( int docLibID ) throws E5Exception {
		DocLibReader dlReader = ( DocLibReader ) Context.getBean( DocLibReader.class );
		return dlReader.get( docLibID );
	}

	/**
	 * �����ĵ���ID����ĵ�����ID
	 * 
	 * @param docLibID �ĵ���ID
	 * @return �ĵ�����ID
	 * @throws E5Exception
	 */
	public static int getDocTypeID( int docLibID ) throws E5Exception {
		return getDocLib( docLibID ).getDocTypeID();
	}

	/**
	 * �����ĵ���ID������̼�¼����
	 * 
	 * @param docLibID �ĵ���ID
	 * @return ���̼�¼����
	 * @throws E5Exception
	 */
	public static String getFlowRecordTable( int docLibID ) throws E5Exception {
		DocLibReader dlReader = ( DocLibReader ) Context.getBean( DocLibReader.class );
		return dlReader.getFlowTableInfo( docLibID ).getLibTable();
	}

	/**
	 * �����ĵ���ID�õ���Ӧ���ݿ��DBSessionʵ��
	 * 
	 * @param docLibID
	 * @return DBSessionʵ��
	 * @throws E5Exception
	 * @created 2005-7-16 21:51:13
	 */
	public static DBSession getDBSession( int docLibID ) throws E5Exception {
		DocLib docLib = getDocLib( docLibID );
		int dsID = docLib.getDsID();
		return Context.getDBSession( dsID );
	}

	/**
	 * �����ĵ�����ID���ĵ��ֶ�������ĵ������ֶ�ʵ��
	 * 
	 * @param docTypeID �ĵ�����ID
	 * @param fieldName �ĵ��ֶ���
	 * @return �ĵ������ֶ�ʵ��
	 * @throws E5Exception ��e5domģ���׳��쳣ʱ
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
	 * �����ĵ�����ID��ø��ĵ������������ĵ������ֶ�ʵ������
	 * 
	 * @param docTypeID �ĵ�����ID
	 * @return �ĵ������ֶ�ʵ������
	 * @throws E5Exception ��e5domģ���׳��쳣ʱ
	 */
	public static DocTypeField[] getDocTypeFields( int docTypeID )
			throws E5Exception {
		DocTypeReader dtReader = ( DocTypeReader ) Context.getBean( DocTypeReader.class );
		return dtReader.getFields( docTypeID );
	}

	/**
	 * ȡ�����ڸ�����Χ�ڵ��ĵ������ֶζ�������
	 * 
	 * @param docTypeID �ĵ�����ID
	 * @param columns �����ֶ�������
	 * @return �ĵ������ֶζ�������
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
	 * �����ֶ�����ѡ����ʵķ�����IResultSet��ȡ���ֶ�ֵ
	 * 
	 * @param resultSet �����
	 * @param docTypeField �ֶ���Ϣ
	 * @return �ֶ�ֵ
	 * @throws SQLException �����ݿ��쳣ʱ
	 * @throws IOException ����ȡlob���������쳣ʱ
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
	 * �жϸ������������Ƿ�Lob����
	 */
	public static boolean isLobType( String dataType ) {
		return ( DataType.CLOB.equals( dataType ) || DataType.BLOB.equals( dataType ) );
	}

	/**
	 * ����bfile���÷�������洢�豸����ģ�鴦�����ع���
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
	 * ɾ����ĳ���ĵ���ص��������̼�¼
	 * 
	 * @param dbsession DBSessionʵ��
	 * @param docLibID �ĵ���ID
	 * @param docID �ĵ�ID
	 * @return ɾ����¼����
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
	 * ɾ����ĳ���ĵ����������й�����ϵ
	 * 
	 * @param dbsession DBSessionʵ��
	 * @param docLibID �ĵ���ID
	 * @param docID �ĵ�ID
	 * @return ɾ����¼����
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
	// ------------- ���߷���

	/**
	 * ��ȥĩβ�Ķ��ţ�����еĻ���
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
	// ------------- ƽ̨�ֶ���Ϣ

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
