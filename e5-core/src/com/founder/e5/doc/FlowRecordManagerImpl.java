/**
 * $Id: e5project com.founder.e5.doc FlowRecordManagerImpl.java 
 * created on 2005-7-18 10:23:59
 * by liyanhui
 */
package com.founder.e5.doc;

import gnu.trove.TLongArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.Context;
import com.founder.e5.context.DBException;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSession;
import com.founder.e5.doc.util.E5docHelper;

/**
 * 流程记录管理接口实现。 <br>
 * 依赖e5context、e5commons
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-18 10:23:59
 */
class FlowRecordManagerImpl implements FlowRecordManager {

	private static Log log = Context.getLog( "e5.doc" );

	private static final String SQL_INSERT_1 = "insert into "; // + tablename

	private static final String SQL_INSERT_2 = " (FLOWRECORDID, DOCLIBID, DOCUMENTID, OPERATORID,"
			+ " OPERATOR, OPERATION, STARTTIME, ENDTIME, FROMPOSITION, TOPOSITION,"
			+ " LASTFLOWNODE, CURFLOWNODE, DETAIL) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 
	 */
	public FlowRecordManagerImpl() {
		super();
	}

	/**
	 * @see com.founder.e5.doc.FlowRecordManager#createFlowRecord(com.founder.e5.doc.Document,
	 *      com.founder.e5.doc.FlowRecord)
	 */
	public FlowRecord createFlowRecord( Document doc, FlowRecord fr )
			throws E5Exception {
		long id = insert(
				doc.getDocLibID(),
				doc.getDocID(),
				fr.getOperatorID(),
				fr.getOperator(),
				fr.getOperation(),
				fr.getStartTime(),
				fr.getEndTime(),
				fr.getFromPosition(),
				fr.getToPosition(),
				fr.getLastFlowNode(),
				fr.getCurrentFlowNode(),
				fr.getDetail() );
		fr.setFrID( id );
		return fr;
	}

	/**
	 * @see com.founder.e5.doc.FlowRecordManager#createFlowRecord(int, long,
	 *      com.founder.e5.doc.FlowRecord)
	 */
	public FlowRecord createFlowRecord( int docLibID, long docID, FlowRecord fr )
			throws E5Exception {
		long id = insert(
				docLibID,
				docID,
				fr.getOperatorID(),
				fr.getOperator(),
				fr.getOperation(),
				fr.getStartTime(),
				fr.getEndTime(),
				fr.getFromPosition(),
				fr.getToPosition(),
				fr.getLastFlowNode(),
				fr.getCurrentFlowNode(),
				fr.getDetail() );
		fr.setFrID( id );
		return fr;
	}

	/**
	 * 插入新流程记录，返回自动生成的ID
	 * 
	 * @created 2005-7-18 10:40:10
	 * @throws E5Exception
	 */
	private long insert( int docLibID, long docID, int optorID, String optor,
			String op, Timestamp start, Timestamp end, String from, String to,
			int lastFlowNode, int curFlowNode, String detail )
			throws E5Exception {

		String table = E5docHelper.getFlowRecordTable( docLibID );
		long frID = EUID.getID( "FlowRecordID" );

		if ( log.isDebugEnabled() )
			log.debug( "insert FlowRecord with libID=" + docLibID + ", docID="
					+ docID + ", frID=" + frID );

		String sql = SQL_INSERT_1 + table + SQL_INSERT_2;

		if ( log.isDebugEnabled() )
			log.debug( "sql=" + sql );

		Object[] params = { new Long( frID ), new Integer( docLibID ),
				new Long( docID ), new Integer( optorID ), optor, op, start,
				end, from, to, new Integer( lastFlowNode ),
				new Integer( curFlowNode ), detail };

		// 这里为了对付参数中有null的情况，同时提供类型信息
		int[] types = { Types.BIGINT, Types.INTEGER, Types.BIGINT,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP,
				Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.VARCHAR };

		DBSession dbsession = E5docHelper.getDBSession( docLibID );
		try {
			dbsession.executeUpdate( sql, params, types );
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			dbsession.closeQuietly();
		}

		return frID;
	}

	private static final String sql_getAll_1 = "select FLOWRECORDID, DOCUMENTID, OPERATORID, "
			+ "OPERATOR, OPERATION, STARTTIME, ENDTIME, FROMPOSITION, TOPOSITION,"
			+ " LASTFLOWNODE, CURFLOWNODE, DETAIL from ";

	private static final String sql_getAllById_2 = " where FLOWRECORDID = ?";

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.doc.FlowRecordManager#get(int, long)
	 */
	public FlowRecord get( int docLibID, long frID ) throws E5Exception {
		String table = E5docHelper.getFlowRecordTable( docLibID );
		String sql = sql_getAll_1 + table + sql_getAllById_2;

		if ( log.isDebugEnabled() ) {
			log.debug( "get FlowRecord with libID=" + docLibID + ", frID="
					+ frID );
			log.debug( "sql=" + sql );
		}

		Object[] params = { new Long( frID ) };

		FlowRecord result = null;
		DBSession dbsession = E5docHelper.getDBSession( docLibID );
		try {
			ResultSet rs = dbsession.executeQuery( sql, params );
			if ( rs.next() ) {
				result = new FlowRecord();
				result.setDocLibID( docLibID );
				assemble( rs, result );
			}

			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			dbsession.closeQuietly();
		}

		return result;
	}

	/**
	 * 从ResultSet中取一行数据组装到给定bean中，字段顺序依据sql_getAll_1
	 */
	private static FlowRecord assemble( ResultSet rs, FlowRecord bean )
			throws SQLException {
		bean.setFrID( rs.getLong( 1 ) );
		bean.setDocID( rs.getLong( 2 ) );
		bean.setOperatorID( rs.getInt( 3 ) );
		bean.setOperator( rs.getString( 4 ) );
		bean.setOperation( rs.getString( 5 ) );
		bean.setStartTime( rs.getTimestamp( 6 ) );
		bean.setEndTime( rs.getTimestamp( 7 ) );
		bean.setFromPosition( rs.getString( 8 ) );
		bean.setToPosition( rs.getString( 9 ) );
		bean.setLastFlowNode( rs.getInt( 10 ) );
		bean.setCurrentFlowNode( rs.getInt( 11 ) );
		bean.setDetail( rs.getString( 12 ) );
		return bean;
	}

	private static final String sql_getId_1 = "select FLOWRECORDID from ";

	private static final String sql_whereByDoc_2 = " where DOCLIBID=? and DOCUMENTID=?";
	
	private static final String sql_orderByID_3 = " order by FLOWRECORDID";

	private static final String sql_orderByID_DESC_4 = " order by FLOWRECORDID desc";
	/**
	 * 取得关联到指定文档的所有流程记录的ID
	 * 
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @return 流程记录ID的数组
	 * @throws E5Exception
	 * @created 2005-7-20 10:17:38
	 */
	public long[] getAssociatedFRIDs( int docLibID, long docID )
			throws E5Exception {
		String table = E5docHelper.getFlowRecordTable( docLibID );
		String sql = sql_getId_1 + table + sql_whereByDoc_2 + sql_orderByID_3;

		if ( log.isDebugEnabled() ) {
			log.debug( "getAssociatedFRIDs with libID=" + docLibID + ", docID="
					+ docID );
			log.debug( "sql=" + sql );
		}

		TLongArrayList result = new TLongArrayList();
		Object[] params = { new Integer( docLibID ), new Long( docID ) };
		DBSession dbsession = E5docHelper.getDBSession( docLibID );
		try {
			ResultSet rs = dbsession.executeQuery( sql, params );
			while ( rs.next() ) {
				result.add( rs.getLong( 1 ) );
			}
			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			dbsession.closeQuietly();
		}

		return result.toNativeArray();
	}

	/*
	 * @see com.founder.e5.doc.FlowRecordManager#getAssociatedFRs(int, long)
	 */
	public FlowRecord[] getAssociatedFRs( int docLibID, long docID )
	throws E5Exception 
	{
		return getAssociatedFRs(docLibID, docID, false);
		
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.doc.FlowRecordManager#getAssociatedFRs(int, long, boolean)
	 */
	public FlowRecord[] getAssociatedFRs(int docLibID, long docID, boolean asc) 
	throws E5Exception
	{
		String table = E5docHelper.getFlowRecordTable( docLibID );
		
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append(sql_getAll_1).append(table).append(sql_whereByDoc_2);
		if (asc) sbSQL.append(sql_orderByID_3);
		else sbSQL.append(sql_orderByID_DESC_4);
		
		String sql = sbSQL.toString();

		if ( log.isDebugEnabled() )
			log.debug( "getAssociatedFRs: sql=" + sql );

		ArrayList result = new ArrayList();
		Object[] params = { new Integer( docLibID ), new Long( docID ) };
		DBSession dbsession = E5docHelper.getDBSession( docLibID );
		try {
			ResultSet rs = dbsession.executeQuery( sql, params );
			while ( rs.next() ) {
				FlowRecord bean = new FlowRecord();
				bean.setDocLibID( docLibID );
				assemble( rs, bean );
				result.add( bean );
			}

			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			dbsession.closeQuietly();
		}

		return ( FlowRecord[] ) result.toArray( new FlowRecord[ result.size() ] );
	}

	private static final String sql_delete_1 = "delete from ";

	private static final String sql_whereById_2 = " where FLOWRECORDID = ?";

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.doc.FlowRecordManager#delete(int, long)
	 */
	public void delete( int docLibID, long frID ) throws E5Exception {
		String table = E5docHelper.getFlowRecordTable( docLibID );
		String sql = sql_delete_1 + table + sql_whereById_2;

		if ( log.isDebugEnabled() ) {
			log.debug( "delete FlowRecord with libID=" + docLibID + ", frID="
					+ frID );
			log.debug( "delete FlowRecord: sql=" + sql );
		}

		Object[] params = { new Long( frID ) };
		DBSession dbsession = E5docHelper.getDBSession( docLibID );
		try {
			dbsession.executeUpdate( sql, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}
	}

	/**
	 * @see com.founder.e5.doc.FlowRecordManager#deleteAssociatedFRs(int, long)
	 */
	public int deleteAssociatedFRs( int docLibID, long docID )
			throws E5Exception {
		String table = E5docHelper.getFlowRecordTable( docLibID );
		String sql = sql_delete_1 + table + sql_whereByDoc_2;

		if ( log.isDebugEnabled() ) {
			log.debug( "delete FlowRecord with libID=" + docLibID + ", docID="
					+ docID );
			log.debug( "delete FlowRecord: sql=" + sql );
		}

		Object[] params = { new Integer( docLibID ), new Long( docID ) };
		DBSession dbsession = E5docHelper.getDBSession( docLibID );
		try {
			return dbsession.executeUpdate( sql, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}
	}

	/**
	 * @see com.founder.e5.doc.FlowRecordManager#moveAssociatedFRs(int, long,
	 *      int)
	 */
	public int moveAssociatedFRs( int docLibID, long docID, int newDocLibID )
			throws E5Exception {
		
		long[] ids = getAssociatedFRIDs( newDocLibID, docID );
		for ( int i = 0; i < ids.length; i++ ) {
			long frID = ids[i];
			FlowRecord fr = get( docLibID, frID );

			delete( docLibID, frID );

			insert(
					newDocLibID,
					fr.getDocID(),
					fr.getOperatorID(),
					fr.getOperator(),
					fr.getOperation(),
					fr.getStartTime(),
					fr.getEndTime(),
					fr.getFromPosition(),
					fr.getToPosition(),
					fr.getLastFlowNode(),
					fr.getCurrentFlowNode(),
					fr.getDetail() );
		}
		
		return ids.length;
		
		//TODO：这个方法实现得不成熟，没有事务控制，以后应该改进
	}
}

