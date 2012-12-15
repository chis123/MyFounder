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
 * 用于实现大字段类型属性延迟加载的Document实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-20 11:00:09
 */
class LazyLoadLobDocument extends Document {

	/**
	 * 标志值：lob类型字段的属性值为该值时代表字段值尚未载入
	 */
	static final String UNLOAD_FLAG = "__UNLOAD__";

	// 缓存的lob类型字段
	private HashMap lobFields = new HashMap(); // 字段名-字段类型

	/**
	 * @param docTypeID
	 * @throws E5Exception 当取文档类型字段信息出错时
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
	 * 判断该属性字段是否lob类型
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
	 * @exception LazyLoadException 若实际加载时出错
	 */
	public Object get( String propertyName ) {
		propertyName = propertyName.toUpperCase();
		Object propertyValue = properties.get( propertyName );

		// 判断该属性是否lob类型，而且目前值为标志值
		if ( isLobType( propertyName ) && ( propertyValue == UNLOAD_FLAG ) ) {

			if ( log.isDebugEnabled() ) {
				log.debug( "loading lasyLoad property[" + propertyName + "]" );
			}

			propertyValue = queryDB( propertyName );

			// 更新属性值，这样下次调用时就可以直接返回
			properties.put( propertyName, propertyValue );
		}

		return propertyValue;
	}

	/**
	 * 从数据库载入属性值
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
	 * 加载所有尚未加载的lob字段值
	 */
	void forceFetchLob() {
		for ( Iterator i = lobFields.keySet().iterator(); i.hasNext(); ) {
			String propertyName = ( String ) i.next();
			get( propertyName );
		}
	}

}
