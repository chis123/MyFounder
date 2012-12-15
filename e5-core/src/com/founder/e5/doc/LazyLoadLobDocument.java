package com.founder.e5.doc;

import java.util.HashMap;
import java.util.Iterator;

import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.DataType;
import com.founder.e5.doc.exception.LazyLoadException;
import com.founder.e5.doc.util.E5docHelper;
import com.founder.e5.dom.DocTypeField;

/**
 * ����ʵ�ִ��ֶ����������ӳټ��ص�Documentʵ��
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-20 11:00:09
 */
class LazyLoadLobDocument extends Document {

	/**
	 * ��־ֵ��lob�����ֶε�����ֵΪ��ֵʱ�����ֶ�ֵ��δ����
	 */
	static final String UNLOAD_FLAG = "__UNLOAD__";

	// �����lob�����ֶ�
	private HashMap lobFields = new HashMap(); // �ֶ���-�ֶ�����

	/**
	 * @param docTypeID
	 * @throws E5Exception ��ȡ�ĵ������ֶ���Ϣ����ʱ
	 */
	public LazyLoadLobDocument( int docTypeID ) throws E5Exception {
		super( docTypeID );

		DocTypeField[] fields = E5docHelper.getDocTypeFields( docTypeID );

		for ( int i = 0; i < fields.length; i++ ) {
			String columnCode = fields[i].getColumnCode();
			String dataType = fields[i].getDataType();
			
			if ( E5docHelper.isLobType( dataType ) )
				this.lobFields.put( columnCode.toUpperCase(), dataType );
		}
	}

	/**
	 * �жϸ������ֶ��Ƿ�lob����
	 * 
	 * @param propertyName
	 * @return
	 * @created 2005-7-20 11:09:08
	 */
	private boolean isLobType( String propertyName ) {
		return lobFields.containsKey( propertyName );
	}

	/**
	 * @see com.founder.e5.doc.Document#get(java.lang.String)
	 * @exception LazyLoadException ��ʵ�ʼ���ʱ����
	 */
	public Object get( String propertyName ) {
		propertyName = propertyName.toUpperCase();
		Object propertyValue = properties.get( propertyName );

		// �жϸ������Ƿ�lob���ͣ�����ĿǰֵΪ��־ֵ
		if ( isLobType( propertyName ) && ( propertyValue == UNLOAD_FLAG ) ) {

			if ( log.isDebugEnabled() ) {
				log.debug( "loading lasyLoad property[" + propertyName + "]" );
			}

			propertyValue = queryDB( propertyName );

			// ��������ֵ�������´ε���ʱ�Ϳ���ֱ�ӷ���
			properties.put( propertyName, propertyValue );
		}

		return propertyValue;
	}

	/**
	 * �����ݿ���������ֵ
	 * 
	 * @param propertyName
	 * @return
	 */
	private Object queryDB( String propertyName ) {
		String dataType = ( String ) lobFields.get( propertyName );

		long docID = getDocID();
		int docLibID = getDocLibID();

		DBSession dbsession = null;
		try {
			String table = E5docHelper.getDocLib( docLibID ).getDocLibTable();

			StringBuffer sb = new StringBuffer();
			sb.append( "select " ).append( propertyName ).append( " from " );
			sb.append( table ).append( " where " ).append(
					E5docHelper.DOCUMENTID );
			sb.append( "=?" );
			String sql = sb.toString();

			if ( log.isDebugEnabled() )
				log.debug( "queryDB: sql=" + sql );

			Object[] param = { new Long( docID ) };
			dbsession = E5docHelper.getDBSession( docLibID );

			if ( DataType.CLOB.equals( dataType ) ) {
				return dbsession.getClob( sql, param );
			} else if ( DataType.BLOB.equals( dataType ) ) {
				return dbsession.getBlob( sql, param );
			}
		} catch ( Exception e ) {
			String msg = new StringBuffer().append( "docID=" ).append( docID ).append(
					", docLibID=" ).append( docLibID ).append( " propertyName=" ).append(
					propertyName ).toString();
			throw new LazyLoadException( msg, e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}

		return null;
	}

	/**
	 * ����������δ���ص�lob�ֶ�ֵ
	 */
	void forceFetchLob() {
		for ( Iterator i = lobFields.keySet().iterator(); i.hasNext(); ) {
			String propertyName = ( String ) i.next();
			get( propertyName );
		}
	}

}
