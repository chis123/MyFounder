package com.founder.e5.doc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.Context;
import com.founder.e5.context.DBException;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;
import com.founder.e5.dom.DataSourceConfigReader;

/**
 * DocAssociationManager实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-21 9:54:09
 */
class DocAssociationManagerImpl implements DocAssociationManager {

	/**
	 * 关联表表名
	 */
	private static final String DOCRELATION_TABLE = "DOM_RELATIONS";

	/**
	 * 关联表所在数据库的数据源ID
	 */
	private static int DSID;

	// static {
	// DataSourceConfigReader dscReader = (DataSourceConfigReader)
	// Context.getBean(DataSourceConfigReader.class);
	// assert (dscReader != null);
	// DSID = dscReader.get(DOCRELATION_TABLE);
	// }

	/**
	 * 取得关联表所在的数据源的ID
	 * 
	 * @throws E5Exception
	 * @created 2005-7-22 9:32:47
	 */
	private static int getDSID() throws E5Exception {
		if ( DSID == 0 ) {
			DataSourceConfigReader dscReader = ( DataSourceConfigReader ) Context.getBean( DataSourceConfigReader.class );
			assert ( dscReader != null );
			DSID = dscReader.get( DOCRELATION_TABLE );
		}
		return DSID;
	}

	/**
	 * 
	 */
	public DocAssociationManagerImpl() {
		super();
	}

	private static final String SQL_GET_ROOT = "select ROOTLIBID, ROOTID from "
			+ DOCRELATION_TABLE
			+ " where DESTDOCLIBID=? and DESTDOCID=? and RELATIONSHIP=?";

	/**
	 * 取根
	 * 
	 * @param srcLibID
	 * @param srcID
	 * @param associationType
	 * @param dbsession
	 * @return Object[]{0-libID, 1-id}
	 * @throws SQLException
	 * @created 2005-7-21 17:07:54
	 */
	private static Object[] getRoot( Integer srcLibID, Long srcID,
			Integer associationType, DBSession dbsession ) throws SQLException {
		Object[] params = { srcLibID, srcID, associationType };

		ArrayList result = new ArrayList();
		ResultSet rs = dbsession.executeQuery( SQL_GET_ROOT, params );
		try {
			if ( rs.next() ) {
				result.add( new Integer( rs.getInt( 1 ) ) );
				result.add( new Long( rs.getLong( 2 ) ) );
			}
		} finally {
			ResourceMgr.closeQuietly( rs );
		}

		return result.toArray();
	}

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.doc.DocAssociationManager#addAssociation(int, long,
	 *      int, long, int, int)
	 * @deprecated
	 */
	public void addAssociation( int srcLibID, long srcID, int destLibID,
			long destID, int associationCode, int order ) throws E5Exception {
		Integer _srcLibID = new Integer( srcLibID );
		Long _srcID = new Long( srcID );
		Integer _destLibID = new Integer( destLibID );
		Long _destID = new Long( destID );
		Integer _associationType = new Integer( associationCode );

		DBSession dbsession = null;
		try {
			// 源节点的根
			Integer rootLibID;
			Long rootID;

			// 目的节点的根
			Integer rootLibID2;
			Long rootID2;

			dbsession = Context.getDBSession( getDSID() );

			// 查询源根；如无，则以源为根创建新根
			Object[] rt = getRoot(
					_srcLibID,
					_srcID,
					_associationType,
					dbsession );
			if ( rt.length > 0 ) {
				rootLibID = ( Integer ) rt[0];
				rootID = ( Long ) rt[1];
			} else {
				rootLibID = _srcLibID;
				rootID = _srcID;
				createNewRoot( rootLibID, rootID, _associationType, dbsession );
			}

			// 查询目的根
			Object[] rt2 = getRoot(
					_destLibID,
					_destID,
					_associationType,
					dbsession );

			// 若存在，合并两根
			if ( rt2.length > 0 ) {
				rootLibID2 = ( Integer ) rt2[0];
				rootID2 = ( Long ) rt2[1];

				// 如目的已有根且不同于源根，则合并到源根
				if ( !rootID2.equals( rootID ) ) {
					mergeTo(
							rootLibID2,
							rootID2,
							rootLibID,
							rootID,
							_associationType,
							dbsession );
				}

				// 关联关系已经存在，直接返回
				return;
			}

			// 不存在，建立新关联记录
			else {
				Long id = new Long( EUID.getID( "DocRelationID" ) );
				Object[] params3 = { id, rootID, rootLibID, _srcID, _srcLibID,
						_destID, _destLibID, _associationType,
						new Integer( order ) };
				dbsession.executeUpdate( SQL_NEW_RECORD, params3 );
			}

		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}

	}

	/**
	 * @see com.founder.e5.doc.DocAssociationManager#addAssociation(com.founder.e5.doc.Document,
	 *      com.founder.e5.doc.Document, int, int)
	 * @deprecated
	 */
	public void addAssociation( Document src, Document dest,
			int associationCode, int order ) throws E5Exception {
		addAssociation(
				src.getDocLibID(),
				src.getDocID(),
				dest.getDocLibID(),
				dest.getDocID(),
				associationCode,
				order );
	}

	private static final String SQL_NEW_RECORD = "insert into "
			+ DOCRELATION_TABLE
			+ " (RELATIONID,ROOTID,ROOTLIBID,SOURCEDOCID,SOURCEDOCLIBID,DESTDOCID,"
			+ "DESTDOCLIBID,RELATIONSHIP,NORDER) values (?,?,?,?,?,?,?,?,?)";

	/**
	 * 创建新根
	 * 
	 * @param rootLibID 根库ID
	 * @param rootID 根ID
	 * @param associationType 关联类型码
	 * @param dbsession
	 * @throws E5Exception
	 * @created 2005-7-21 13:26:20
	 */
	private void createNewRoot( Integer rootLibID, Long rootID,
			Integer associationType, DBSession dbsession ) throws E5Exception {
		long id = EUID.getID( "DocRelationID" );

		Object[] params = { new Long( id ), rootID, rootLibID, rootID,
				rootLibID, rootID, rootLibID, associationType, new Integer( 1 ) };

		try {
			dbsession.executeUpdate( SQL_GET_ROOT, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		}
	}

	private static final String SQL_MERGE = "update " + DOCRELATION_TABLE
			+ " set ROOTID=? and ROOTLIBID=? where ROOTID=? and ROOTLIBID=?";

	/**
	 * 源根合并到目的根
	 * 
	 * @param srcRootLibID 源根库ID
	 * @param srcRootID 源根ID
	 * @param destRootLibID 目的根库ID
	 * @param destRootID 目的根ID
	 * @param associationType 关联类型
	 * @param dbsession
	 * @throws SQLException
	 * @created 2005-7-21 16:38:26
	 */
	private void mergeTo( Integer srcRootLibID, Long srcRootID,
			Integer destRootLibID, Long destRootID, Integer associationType,
			DBSession dbsession ) throws SQLException {

		Object[] params = { srcRootID, srcRootLibID, srcRootID, srcRootLibID,
				srcRootID, srcRootLibID, associationType };
		dbsession.executeUpdate( SQL_DELETE, params );

		Object[] params2 = { destRootID, destRootLibID, srcRootID, srcRootLibID };
		dbsession.executeUpdate( SQL_MERGE, params2 );

	}

	private static final String SQL_DELETE = "delete from " + DOCRELATION_TABLE
			+ " where ROOTID=? and ROOTLIBID=?"
			+ " and SOURCEDOCID=? and SOURCEDOCLIBID=? and DESTDOCID=?"
			+ " and DESTDOCLIBID=? and RELATIONSHIP=?";

	/**
	 * @see com.founder.e5.doc.DocAssociationManager#delAssociation(int, long,
	 *      int, long, int)
	 * @deprecated
	 */
	public DocAssociation delAssociation( int srcLibID, long srcID,
			int destLibID, long destID, int associationCode ) {
		// Integer _srcLibID = new Integer(srcLibID);
		// Long _srcID = new Long(srcID);
		// Integer _destLibID = new Integer(destLibID);
		// Long _destID = new Long(destID);
		// Integer _associationType = new Integer(associationCode);
		return null;
	}

	// private static final String SQL_GET_BY_ROOT =
	// "select RELATIONID,ROOTID,ROOTLIBID,SOURCEDOCID,SOURCEDOCLIBID," +
	// "DESTDOCID,DESTDOCLIBID,RELATIONSHIP,NORDER from " + DOCRELATION_TABLE
	// + " where ROOTLIBID=? and ROOTID=? and RELATIONSHIP=?";

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.doc.DocAssociationManager#getAllAssociations(int,
	 *      long, int)
	 * @deprecated
	 */
	public DocAssociation[] getAllAssociations( int srcLibID, long srcID,
			int associationCode ) throws E5Exception {
		Integer _srcLibID = new Integer( srcLibID );
		Long _srcID = new Long( srcID );
		Integer _associationType = new Integer( associationCode );

		DBSession dbsession = null;

		try {
			dbsession = Context.getDBSession( getDSID() );
			Object[] rt = getRoot(
					_srcLibID,
					_srcID,
					_associationType,
					dbsession );

			Integer rootLibID = ( Integer ) rt[0];
			Long rootID = ( Long ) rt[1];

			Object[] params = { rootLibID, rootID, _associationType };
			IResultSet rs = dbsession.executeQuery( SQL_GET_ROOT, params );
			try {
				ArrayList list = new ArrayList();
				while ( rs.next() ) {
					DocAssociation da = new DocAssociation();
					da.setAssociationID( rs.getInt( 1 ) );
					da.setRootID( rs.getLong( 2 ) );
					da.setRootLibID( rs.getInt( 3 ) );
					da.setSrcID( rs.getLong( 4 ) );
					da.setSrcLibID( rs.getInt( 5 ) );
					da.setDestID( rs.getLong( 6 ) );
					da.setDestLibID( rs.getInt( 7 ) );
					da.setAssociationCode( rs.getInt( 8 ) );
					da.setOrder( rs.getInt( 9 ) );
					list.add( da );
				}

				return ( DocAssociation[] ) list.toArray( new DocAssociation[ list.size() ] );
			} finally {
				ResourceMgr.closeQuietly( rs );
			}

		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}

	}

	// -------------------------------------------------------------------------

	private static final String SQL_REPLACE_SRC = "update " + DOCRELATION_TABLE
			+ " set SOURCEDOCLIBID=?,SOURCEDOCID=?"
			+ " where SOURCEDOCLIBID=? and SOURCEDOCID=?";

	private static final String SQL_REPLACE_DEST = "update "
			+ DOCRELATION_TABLE + " set DESTDOCLIBID=?,DESTDOCID=?"
			+ " where DESTDOCLIBID=? and DESTDOCID=?";

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#replaceNode(int, long, int, long)
	 */
	public void replaceNode( int oldDocLibID, long oldDocID, int newDocLibID,
			long newDocID ) throws E5Exception {

		Integer oldLibID = new Integer( oldDocLibID );
		Integer newLibID = new Integer( newDocLibID );
		Long oldID = new Long( oldDocID );
		Long newID = new Long( newDocID );

		DBSession dbsession = null;
		try {
			dbsession = Context.getDBSession( getDSID() );
			dbsession.beginTransaction();

			Object[] params = { newLibID, newID, oldLibID, oldID };

			dbsession.executeUpdate( SQL_REPLACE_SRC, params );
			dbsession.executeUpdate( SQL_REPLACE_DEST, params );

			dbsession.commitTransaction();

		} catch ( SQLException e ) {
			ResourceMgr.rollbackQuietly( dbsession );

			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}
	}

	// -------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#create(com.founder.e5.doc.DocAssociation)
	 */
	public DocAssociation create( DocAssociation obj ) throws E5Exception {
		long id =  EUID.getID( "DocRelationID" ) ;
		Object[] params = { new Long(id), new Long( obj.getRootID() ),
				new Integer( obj.getRootLibID() ), new Long( obj.getSrcID() ),
				new Integer( obj.getSrcLibID() ), new Long( obj.getDestID() ),
				new Integer( obj.getDestLibID() ),
				new Integer( obj.getAssociationCode() ),
				new Integer( obj.getOrder() ) };
		DBSession sess = null;

		try {
			sess = Context.getDBSession( getDSID() );
			sess.executeUpdate( SQL_NEW_RECORD, params );

			obj.setAssociationID((int)id);
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}

		return obj;
	}

	private static final String SQL_DELETE_RECORD = "delete from "
			+ DOCRELATION_TABLE + " where RELATIONID = ?";

	private static final String SQL_DELETE_BYSRC = "delete from "
		+ DOCRELATION_TABLE + " where SOURCEDOCLIBID = ? and SOURCEDOCID = ?";

	private static final String SQL_DELETE_BYDEST = "delete from "
		+ DOCRELATION_TABLE + " where DESTDOCLIBID = ? and DESTDOCID = ?";

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#delete(long)
	 */
	public void delete( long recordID ) throws E5Exception {
		Object[] params = { new Long( recordID ) };
		DBSession sess = null;

		try {
			sess = Context.getDBSession( getDSID() );
			sess.executeUpdate( SQL_DELETE_RECORD, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}
	}
	
	
	private static final String SQL_GET_RECORD = "select * from "
		+ DOCRELATION_TABLE
		+ " where RELATIONID = ?";

	private static final String SQL_GET_BYSRC = "select * from "
			+ DOCRELATION_TABLE
			+ " where SOURCEDOCLIBID = ? and SOURCEDOCID = ? and RELATIONSHIP = ?";

	private static final String SQL_GET_BYDEST = "select * from "
			+ DOCRELATION_TABLE
			+ " where DESTDOCLIBID = ? and DESTDOCID = ? and RELATIONSHIP = ?";

	private static final String SQL_GET_BYROOT = "select * from "
			+ DOCRELATION_TABLE
			+ " where ROOTLIBID = ? and ROOTID = ? and RELATIONSHIP = ?";

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#getBySrc(int, long, int)
	 */
	public DocAssociation[] getBySrc( int srcLibID, long srcID,
			int associationCode ) throws E5Exception {
		Object[] params = { new Integer( srcLibID ), new Long( srcID ),
				new Integer( associationCode ) };
		DBSession sess = null;
		ArrayList list = null;

		try {
			sess = Context.getDBSession( getDSID() );
			IResultSet rs = sess.executeQuery( SQL_GET_BYSRC, params );
			list = getResult( rs );
			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}

		return ( DocAssociation[] ) list.toArray( new DocAssociation[ list.size() ] );
	}

	// 从结果集中取出数据，组合成DocAssociation对象，放入list中返回
	private static ArrayList getResult( IResultSet rs ) throws SQLException {
		ArrayList list = new ArrayList();
		while ( rs.next() ) {
			list.add( assemble(rs) );
		}

		return list;
	}
	private static DocAssociation assemble( IResultSet rs ) throws SQLException 
	{
		DocAssociation row = new DocAssociation();
		row.setAssociationID( rs.getInt( "RELATIONID" ) );
		row.setRootLibID( rs.getInt( "ROOTLIBID" ) );
		row.setRootID( rs.getLong( "ROOTID" ) );
		row.setSrcLibID( rs.getInt( "SOURCEDOCLIBID" ) );
		row.setSrcID( rs.getLong( "SOURCEDOCID" ) );
		row.setDestLibID( rs.getInt( "DESTDOCLIBID" ) );
		row.setDestID( rs.getLong( "DESTDOCID" ) );
		row.setAssociationCode( rs.getInt( "RELATIONSHIP" ) );
		row.setOrder( rs.getInt( "NORDER" ) );
		return row;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#getByDest(int, long, int)
	 */
	public DocAssociation[] getByDest( int destLibID, long destID,
			int associationCode ) throws E5Exception {
		Object[] params = { new Integer( destLibID ), new Long( destID ),
				new Integer( associationCode ) };
		DBSession sess = null;
		ArrayList list = null;

		try {
			sess = Context.getDBSession( getDSID() );
			IResultSet rs = sess.executeQuery( SQL_GET_BYDEST, params );
			list = getResult( rs );
			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}

		return ( DocAssociation[] ) list.toArray( new DocAssociation[ list.size() ] );
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#getByRoot(int, long, int)
	 */
	public DocAssociation[] getByRoot( int rootLibID, long rootID,
			int associationCode ) throws E5Exception {
		Object[] params = { new Integer( rootLibID ), new Long( rootID ),
				new Integer( associationCode ) };
		DBSession sess = null;
		ArrayList list = null;

		try {
			sess = Context.getDBSession( getDSID() );
			IResultSet rs = sess.executeQuery( SQL_GET_BYROOT, params );
			list = getResult( rs );
			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}

		return ( DocAssociation[] ) list.toArray( new DocAssociation[ list.size() ] );
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#deleteBySrc(int, long)
	 */
	public void deleteBySrc(int srcLibID, long srcID) throws E5Exception
	{
		Object[] params = { new Integer( srcLibID ),  new Long( srcID )};
		DBSession sess = null;

		try {
			sess = Context.getDBSession( getDSID() );
			sess.executeUpdate( SQL_DELETE_BYSRC, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.doc.DocAssociationManager#deleteByDest(int, long)
	 */
	public void deleteByDest(int destLibID, long destID) throws E5Exception
	{
		Object[] params = { new Integer( destLibID ),  new Long( destID )};
		DBSession sess = null;

		try {
			sess = Context.getDBSession( getDSID() );
			sess.executeUpdate( SQL_DELETE_BYDEST, params );
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}
	}

	public DocAssociation get(long recordID) throws E5Exception
	{
		Object[] params = { new Long( recordID )};

		DBSession sess = null;
		DocAssociation row = null;
		try {
			sess = Context.getDBSession( getDSID() );
			IResultSet rs = sess.executeQuery( SQL_GET_RECORD, params );
			
			if (rs.next())
				row = assemble(rs);
			rs.close();
		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( sess );
		}
		return row;
	}

}
