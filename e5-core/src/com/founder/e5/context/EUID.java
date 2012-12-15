/*
 * Created on 2005-7-11 9:46:18
 * 
 */
package com.founder.e5.context;

import java.sql.SQLException;
import java.util.HashMap;

import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.db.DBSession;

/**
 * ��������e5ϵͳ�и���ID������
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-11 9:46:18
 */
public class EUID {

	private static HashMap idconfig = new HashMap();

	/**
	 * ������������id����
	 * 
	 * @param idname id���ƣ�����һ����Ҫid��ʵ��
	 * @return
	 * @exception E5Exception
	 */
	public static long getID( String idname ) throws E5Exception {
		if ( idname == null )
			throw new NullPointerException();

		IDConfig aid = ( IDConfig ) idconfig.get( idname );

		DBSession dbsession = null;
		try {
			dbsession = Context.getDBSession();

			// �����û������id����ע��Ϊnormal����
			if ( aid == null ) {
				aid = registerID( idname, "normal", null );
			}

			if ( "sequence".equals( aid.type ) ) {
				return dbsession.getSequenceNextValue( aid.param );
			} else if ( "normal".equals( aid.type ) ) {
				return normalGenerate( dbsession, idname );
			} else
				throw new IllegalStateException( "unrecognized id type: "
						+ aid.type );

		} catch ( SQLException e ) {
			throw new DBException( e );
		} finally {
			ResourceMgr.closeQuietly( dbsession );
		}
	}

	/**
	 * ������������id����
	 * 
	 * @param sess
	 * @param idname
	 * @return
	 * @throws DBException
	 */
	public static long getID( DBSession sess, String idname )
			throws DBException {
		IDConfig ic = ( IDConfig ) idconfig.get( idname );

		try {
			// �����û������id����ע��Ϊnormal����
			if ( ic == null ) {
				ic = registerID( idname, "normal", null );
			}

			if ( "sequence".equals( ic.type ) ) {
				return sess.getSequenceNextValue( ic.param );
			}

			return normalGenerate( sess, idname );

		} catch ( SQLException e ) {
			throw new DBException( e );
		}
	}

	// TODO: ���Դ����Լ�e5id���������ֶεȶ��ô������ļ���ȡ
	private static final int MAX_RETRY = 10;

	private static final String selectsql = "select E5VALUE from E5ID where E5IDENTIFIER=?";

	private static final String updatesql = "update E5ID set E5VALUE=? where E5IDENTIFIER=? and E5VALUE=?";

	/**
	 * ���������ݿ��E5ID�л����һ��ID
	 * 
	 * @param dbsession ���ݿ�Ự
	 * @param idname id��
	 * @return
	 * @throws DBException
	 */
	private static long normalGenerate( DBSession dbsession, String idname )
			throws DBException {
		return normalGenerate( dbsession, idname, MAX_RETRY );
	}

	private static long normalGenerate( DBSession dbsession, String idname,
			int retryCounter ) throws DBException {
		try {
			long oldvalue = dbsession.getLong(
					selectsql,
					new Object[] { idname } );

			// �鲻����˵����һ����ID
			if ( oldvalue == 0 ) {
				registerIDInDB( dbsession, idname );
				oldvalue = 1;
			}

			long newvalue = oldvalue + 1;
			Object[] params = { new Long( newvalue ), idname,
					new Long( oldvalue ) };
			int rt = dbsession.executeUpdate( updatesql, params );

			if ( rt > 0 ) {
				return oldvalue;
			}

			if ( retryCounter > 0 ) {
				retryCounter--;
				return normalGenerate( dbsession, idname, retryCounter );
			} else {
				throw new DBException(
						"Concurrency update too frequent! (Retryed "
								+ MAX_RETRY + " times)" );
			}

		} catch ( SQLException e ) {
			throw new DBException( e );
		}
	}

	/**
	 * ע��һ��ID�������ã��Ժ����ͨ��EUID.getID(idname)����ID
	 * 
	 * @param name id�� (not null)
	 * @param type id���� (not null)
	 * @param param ��Ҫ�Ĳ��� (null safe)
	 */
	public static IDConfig registerID( String name, String type, String param ) {
		if ( name == null || type == null )
			throw new NullPointerException();

		IDConfig aid = new IDConfig( name, type, param );
		idconfig.put( name, aid );
		return aid;
	}

	private static final String insertsql = "insert into E5ID (E5IDENTIFIER, E5VALUE) values (?, 1)";

	/**
	 * ��E5ID���в���һ���¼�¼���Ǽ�һ����ID
	 * 
	 * @param dbsession
	 * @param name id��
	 * @throws SQLException
	 */
	private static void registerIDInDB( DBSession dbsession, String name )
			throws SQLException {
		dbsession.executeUpdate( insertsql, new Object[] { name } );
	}

	// ----------------------------------------------------------

	/**
	 * ������һ��ID��������������Ϣ
	 * 
	 * @author liyanhui
	 * @version 1.0
	 * @created 2005-7-11 9:52:08
	 */
	private static class IDConfig {
		String name;

		String type;

		String param;

		/**
		 * @param name
		 * @param type
		 * @param param
		 */
		public IDConfig( String name, String type, String param ) {
			super();
			this.name = name;
			this.type = type;
			this.param = param;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals( Object obj ) {
			if ( obj instanceof IDConfig ) {
				return name.equals( ( ( IDConfig ) obj ).name );
			}
			return false;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return name.hashCode();
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return name;
		}
	}

}
