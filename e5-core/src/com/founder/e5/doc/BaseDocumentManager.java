package com.founder.e5.doc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.Context;
import com.founder.e5.context.DBException;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;
import com.founder.e5.db.LaterDataTransferException;
import com.founder.e5.doc.exception.DocumentException;
import com.founder.e5.doc.util.E5docHelper;
import com.founder.e5.dom.DocLib;
import com.founder.e5.dom.DocTypeField;

/**
 * DocumentManager的基本实现。 <br>
 * 依赖e5commons、e5context、e5db、e5dom
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-28 16:44:53
 */
abstract class BaseDocumentManager implements DocumentManager {

	protected static Log log = Context.getLog( "e5.doc" );

	/**
	 * Constructor
	 */
	public BaseDocumentManager() {
		super();
	}

	public Document newDocument( int docLibID ) throws E5Exception {
		long docID = EUID.getID( "DocumentID" );
		if ( log.isDebugEnabled() )
			log.debug( "allocated docID: " + docID );

		return newDocument(docLibID, docID);
	}
	
	public Document newDocument( int docLibID, long docID ) throws E5Exception {
		if ( log.isDebugEnabled() )
			log.debug( "create new document with libID=" + docLibID );

		int docTypeID = E5docHelper.getDocTypeID( docLibID );
		Document doc = new Document( docTypeID );

		if ( log.isDebugEnabled() )
			log.debug( "allocated docID: " + docID );

		doc.internalSetLibID( docLibID );
		doc.internalSetDocID( docID );
		return doc;
	}

	public Document newDocument( Document refDoc, int docLibID )
			throws E5Exception {
		Document doc = newDocument(docLibID);

		if (refDoc == null) return doc;

		// 从参考文档复制属性
		if ( refDoc instanceof LazyLoadLobDocument ) {
			( ( LazyLoadLobDocument ) refDoc ).forceFetchLob();
		}

		doc.copyPropertiesFrom( refDoc );

		return doc;
	}

	public Document newDocument( Document refDoc, int docLibID, long docID )
	throws E5Exception {
		Document doc = newDocument(docLibID, docID);
		
		if (refDoc == null) return doc;
		
		// 从参考文档复制属性
		if ( refDoc instanceof LazyLoadLobDocument ) {
			( ( LazyLoadLobDocument ) refDoc ).forceFetchLob();
		}
		
		doc.copyPropertiesFrom( refDoc );
		
		return doc;
	}

	public void save( Document doc ) throws E5Exception {
		int status = doc.getStatus();
		switch ( status ) {
			case Document.STATUS_TRANSIENT :
				insert( doc );
				break;

			case Document.STATUS_PERSISTENT :
				update( doc );
				break;

			default :
				throw new IllegalStateException( "Document status: " + status );
		}
	}

	/**
	 * 插入新文档到文档自身属性所指定的文档库
	 */
	protected void insert( Document doc ) throws E5Exception {
		int docLibID = doc.getDocLibID();
		int docTypeID = doc.getDocTypeID();

		if ( log.isDebugEnabled() )
			log.debug( "insert document with libID=" + docLibID + ", ID="
					+ doc.getDocID() );

		DocTypeField[] fields = E5docHelper.getDocTypeFields( docTypeID );
		DocLib docLib = E5docHelper.getDocLib( docLibID );

		if ( docLib == null )
			throw new IllegalStateException( "Illegal docLibID: " + docLibID );

		String tablename = docLib.getDocLibTable();

		// 拼sql语句
		StringBuffer sb = new StringBuffer();
		sb.append( "insert into " ).append( tablename ).append( " (" );

		// 生成插入语句中字段名、字段值占位符、待绑定参数值

		StringBuffer names = new StringBuffer(); // 插入语句中的列名片断
		StringBuffer values = new StringBuffer(); // 插入语句中的列值片断
		ArrayList params = new ArrayList(); // 待绑定到语句的实参

		DBSession dbsession = null;
		try {
			dbsession = E5docHelper.getDBSession( docLibID );

			for ( int i = 0; i < fields.length; i++ ) {
				DocTypeField field = fields[i];
				String columnCode = field.getColumnCode();
				Object columnValue = doc.get( columnCode );

				// 插入时忽略空值字段
				if ( columnValue == null )
					continue;

				names.append( columnCode ).append( "," );
				values.append( "?" ).append( "," );
				params.add( columnValue );
			}

			sb.append( E5docHelper.removeTailComma( names ) );
			sb.append( ") values (" );
			sb.append( E5docHelper.removeTailComma( values ) ).append( ")" );
			String sql = sb.toString();

			if ( log.isDebugEnabled() ) {
				log.debug( "sql=" + sql );
				log.debug( "binding parameters: " + params );
			}

			// 进入事务处理
			dbsession.beginTransaction();

			// 插入所有字段
			dbsession.executeUpdate( sql, params.toArray() );

			dbsession.commitTransaction();

		} catch ( Exception e ) {
			ResourceMgr.rollbackQuietly( dbsession );

			if ( e instanceof SQLException ) {
				throw new DBException( e );
			} else if ( e instanceof IOException ) {
				throw new DocumentException(
						"IO error while processing Clob/Blob.",
						e );
			} else if ( e instanceof LaterDataTransferException ) {
				throw new DocumentException( "Error while processing Bfile.", e );
			} else {
				throw new DocumentException( "Unexpected error.", e );
			}
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}

		doc.setStatus( Document.STATUS_PERSISTENT );
	}

	/**
	 * 更新文档
	 */
	protected void update( Document doc ) throws E5Exception, DBException,
			DocumentException {

		if ( !doc.isDirty() ) {
			if ( log.isDebugEnabled() )
				log.debug( "document[" + doc.idString() + "] not dirty!" );
			return;
		}

		if ( log.isDebugEnabled() )
			log.debug( "begin to update document[" + doc.idString() + "]" );

		long docID = doc.getDocID();
		int docLibID = doc.getDocLibID();
		String idFieldName = E5docHelper.DOCUMENTID;

		DocLib docLib = E5docHelper.getDocLib( docLibID );
		if ( docLib == null )
			throw new IllegalStateException( "Illegal docLibID: " + docLibID );
		String tablename = docLib.getDocLibTable();

		int docTypeID = doc.getDocTypeID();
		Collection columns = doc.getDirtyColumns();

		if ( log.isDebugEnabled() )
			log.debug( "dirty columns: " + columns );

		String[] ss = ( String[] ) columns.toArray( new String[ columns.size() ] );
		DocTypeField[] dirtyFields = E5docHelper.getDocTypeFields(
				docTypeID,
				ss );

		// 拼sql语句
		StringBuffer sb = new StringBuffer();
		sb.append( "update " ).append( tablename ).append( " set " );

		ArrayList params = new ArrayList(); // 待绑定到语句的实参

		for ( int i = 0; i < dirtyFields.length; i++ ) {
			DocTypeField field = dirtyFields[i];
			String columnCode = field.getColumnCode();
			Object columnValue = doc.get( columnCode );

			sb.append( columnCode ).append( "=" ).append( "?" );
			params.add( columnValue );

			if ( i < dirtyFields.length - 1 ) {
				sb.append( "," );
			}
		}

		sb.append( " where " ).append( idFieldName ).append( "=?" );
		params.add( new Long( docID ) );
		String sql = sb.toString();

		if ( log.isDebugEnabled() )
			log.debug( "sql=" + sql );

		DBSession dbsession = null;
		try {
			dbsession = E5docHelper.getDBSession( docLibID );

			dbsession.beginTransaction();

			// 更新普通类型字段
			dbsession.executeUpdate( sql, params.toArray() );

			dbsession.commitTransaction();

		} catch ( Exception e ) {
			ResourceMgr.rollbackQuietly( dbsession );

			if ( e instanceof SQLException ) {
				throw new DBException( e );
			} else if ( e instanceof IOException ) {
				throw new DocumentException(
						"IO error while processing Clob/Blob.",
						e );
			} else if ( e instanceof LaterDataTransferException ) {
				throw new DocumentException( "Error while processing Bfile.", e );
			} else {
				throw new DocumentException( "Unexpected error.", e );
			}
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}

		doc.setDirty( false );
		doc.clearDirtyColumns();
	}

	// ------------------------------------------------------------------------
	// 载入功能

	public Document get( int docLibID, long docID ) throws E5Exception {
		// 默认延迟加载lob标志为真
		return get( docLibID, docID, true );
	}

	public Document[] get( int docLibID, long[] docIDs ) throws E5Exception {
		ArrayList list = new ArrayList( docIDs.length );
		for ( int i = 0; i < docIDs.length; i++ ) {
			long docID = docIDs[i];
			list.add( get( docLibID, docID ) );
		}
		return ( Document[] ) list.toArray( new Document[ list.size() ] );
	}

	public Document get( int docLibID, long docID, boolean lazyLoadLob )
			throws E5Exception {

		int docTypeID = E5docHelper.getDocTypeID( docLibID );
		DocTypeField[] fields = E5docHelper.getDocTypeFields( docTypeID );
		String tablename = E5docHelper.getDocLib( docLibID ).getDocLibTable();

		return load( docLibID, docID, lazyLoadLob, docTypeID, fields, tablename );
	}

	/**
	 * 投影查询，只取用户指定的字段，返回Document实例；若文档不存在，则返回null
	 * 
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @param lazyLoadLob 是否延迟加载Lob类型字段
	 * @param docTypeID 文档类型ID
	 * @param fields 用户指定读取的字段
	 * @param tablename 文档库表名
	 * @return Document实例
	 * @throws E5Exception
	 * @throws DBException
	 * @throws DocumentException
	 */
	private Document load( int docLibID, long docID, boolean lazyLoadLob,
			int docTypeID, DocTypeField[] fields, String tablename )
			throws E5Exception, DBException, DocumentException {

		if ( log.isDebugEnabled() )
			log.debug( "load document with libID=" + docLibID + ", ID=" + docID );

		// 拼sql语句
		StringBuffer sb = new StringBuffer();
		sb.append( "select " );
		for ( int i = 0; i < fields.length; i++ ) {
			DocTypeField field = fields[i];

			// 延迟加载lob时,lob字段就不取了
			if ( !( lazyLoadLob && E5docHelper.isLobType( field.getDataType() ) ) ) {
				sb.append( field.getColumnCode() ).append( "," );

				if ( log.isTraceEnabled() )
					log.trace( "required column: " + field.getColumnCode() );
			}
		}

		int tail = sb.length() - 1;
		if ( sb.charAt( tail ) == ',' )
			sb.deleteCharAt( tail );
		sb.append( " from " ).append( tablename ).append( " where " );
		sb.append( E5docHelper.DOCUMENTID ).append( "=?" );

		String sql = sb.toString();

		if ( log.isDebugEnabled() )
			log.debug( "sql=" + sql );

		DBSession dbSession = null;
		IResultSet rs = null;
		Document doc = null;

		try {
			dbSession = E5docHelper.getDBSession( docLibID );
			Object[] param = { new Long( docID ) };
			rs = dbSession.executeQuery( sql, param );

			if ( rs.next() ) {

				if ( lazyLoadLob )
					doc = new LazyLoadLobDocument( docTypeID );
				else
					doc = new Document( docTypeID );
				doc.internalSetDocID( docID );
				doc.internalSetLibID( docLibID );

				for ( int i = 0; i < fields.length; i++ ) {
					DocTypeField field = fields[i];
					String columnCode = field.getColumnCode();

					if ( columnCode.equals( E5docHelper.DOCUMENTID )
							|| columnCode.equals( E5docHelper.DOCLIBID ) )
						continue;

					// 延迟加载lob时,lob字段就不取了
					if ( !( lazyLoadLob && E5docHelper.isLobType( field.getDataType() ) ) ) {
						Object value = E5docHelper.getFieldValue( rs, field );
						doc.set( columnCode, value );
					}

					// 设置延迟载入标志
					else {
						doc.set( columnCode, LazyLoadLobDocument.UNLOAD_FLAG );
					}
				}

				doc.setStatus( Document.STATUS_PERSISTENT );
			}

			else {
				log.warn( "record not exist!" );
			}
		} catch ( SQLException e ) {
			throw new DBException( e );

		} catch ( IOException e ) {
			throw new DocumentException( "Error while reading LOB.", e );

		} finally {
			ResourceMgr.closeQuietly( rs );
			ResourceMgr.closeQuietly( dbSession );
		}

		return doc;
	}

	// --------------------------------------------- 投影查询

	public Document get( int docLibID, long docID, String[] columns )
			throws E5Exception {
		// 默认延迟加载lob标志为真
		return get( docLibID, docID, columns, true );
	}

	public Document get( int docLibID, long docID, String[] columns,
			boolean lazyLoadLob ) throws E5Exception {

		int docTypeID = E5docHelper.getDocTypeID( docLibID );
		String tablename = E5docHelper.getDocLib( docLibID ).getDocLibTable();
		DocTypeField[] fields = E5docHelper.getDocTypeFields(
				docTypeID,
				columns );

		return load( docLibID, docID, lazyLoadLob, docTypeID, fields, tablename );
	}

	public Document[] get( int docLibID, long[] docIDs, String[] columns )
			throws E5Exception {
		ArrayList list = new ArrayList( docIDs.length );
		for ( int i = 0; i < docIDs.length; i++ ) {
			long docID = docIDs[i];
			list.add( get( docLibID, docID, columns ) );
		}
		return ( Document[] ) list.toArray( new Document[ list.size() ] );
	}

	// ------------------------------------------------------------------------
	// 删除功能

	public static final String SQL_DELETE = "delete from $TABLE where "
			+ E5docHelper.DOCUMENTID + "=?";

	public void delete( Document doc ) throws E5Exception, DBException {
		delete( doc.getDocLibID(), doc.getDocID() );
	}

	public void delete( int docLibID, long docID ) throws E5Exception {

		if ( log.isDebugEnabled() )
			log.debug( "delete document with libID=" + docLibID + ", ID="
					+ docID );

		String tablename = E5docHelper.getDocLib( docLibID ).getDocLibTable();

		StringBuffer sb = new StringBuffer();
		sb.append( "delete from " ).append( tablename ).append( " where " ).append(
				E5docHelper.DOCUMENTID ).append( "=?" );
		String sql = sb.toString();
		Object[] param = { new Long( docID ) };

		DBSession dbSession = null;
		try {
			dbSession = E5docHelper.getDBSession( docLibID );
			dbSession.beginTransaction();

			// 删除稿件
			int rt = dbSession.executeUpdate( sql, param );
			if ( rt == 0 )
				log.debug( "record not exist!" );

			// 删除流程记录
			E5docHelper.deleteAssociatedFRs( dbSession, docLibID, docID );

			// 删除关联关系
			E5docHelper.deleteAssociations( dbSession, docLibID, docID );

			dbSession.commitTransaction();

		} catch ( SQLException e ) {
			ResourceMgr.rollbackQuietly( dbSession );

			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbSession );
		}
	}

	// ------------------------------------------------------------------------
	// 移动功能
	public Document moveTo( Document doc, int newDocLibID ) throws E5Exception {
		return moveTo(doc, newDocLibID, 0);
	}
	public Document moveTo( Document doc, int newDocLibID, long newDocID ) throws E5Exception {
		int oldLibID = doc.getDocLibID();
		if ( oldLibID == newDocLibID ) return doc;

		long oldDocID = doc.getDocID();
		if (newDocID == 0) newDocID = EUID.getID( "DocumentID" );

		doc.internalSetDocID( newDocID );
		doc.internalSetLibID( newDocLibID );

		FlowRecordManager frmgr = ( FlowRecordManager ) Context.getBean( FlowRecordManager.class );

		DocAssociationManager damgr = ( DocAssociationManager ) Context.getBean( DocAssociationManager.class );

		int step = 0;
		ArrayList frsInNewLib = new ArrayList(); // 已拷贝到新库的流程记录

		try {

			// step1: 文档数据复制到新库
			insert( doc );
			step++;

			// step2: 流程记录复制到新库
			FlowRecord[] frs = frmgr.getAssociatedFRs( oldLibID, oldDocID );
			long[] oldFrIDs = new long[ frs.length ];
			for ( int i = 0; i < frs.length; i++ ) {
				FlowRecord fr = frs[i];
				oldFrIDs[i] = fr.getFrID(); // 保存旧ID,待会儿删
				frmgr.createFlowRecord( doc, fr );
				frsInNewLib.add( fr ); // 保存复制过去的流程记录，待必要时回滚
			}
			step++;

			// step3: 更新关联关系
			damgr.replaceNode( oldLibID, oldDocID, newDocLibID, newDocID );
			step++;

			// step4: 删除旧流程记录
			for ( int i = 0; i < oldFrIDs.length; i++ ) {
				frmgr.delete( oldLibID, oldFrIDs[i] );
			}
			step++;

			// step5: 删除旧文档
			delete( oldLibID, oldDocID );
			step++;

		} catch ( E5Exception e ) {
			switch ( step ) {
				case 3 :
					// 关联关系再改回去
					damgr.replaceNode(
							newDocLibID,
							newDocID,
							oldLibID,
							oldDocID );
				case 2 :
				case 1 :
					// 删除已拷贝到新库的流程记录
					for ( Iterator i = frsInNewLib.iterator(); i.hasNext(); ) {
						FlowRecord fr = ( FlowRecord ) i.next();
						frmgr.delete( fr.getDocLibID(), fr.getFrID() );
					}
					// 删除已拷贝到新库的文档
					delete( doc );
					break;
				case 4 :

					break;

				default :
					break;
			}

			throw e;
		}

		// TODO:事务控制

		return doc;
	}

	public Document moveTo( int docLibID, long docID, int newDocLibID )
			throws E5Exception {
		Document doc = get( docLibID, docID );
		if ( doc == null )
			throw new E5Exception( "Document with docLibID=" + docLibID
					+ ", docID=" + docID + " not exist." );

		return moveTo( doc, newDocLibID );
	}

	// ---------------------------------------------------------------------

}
