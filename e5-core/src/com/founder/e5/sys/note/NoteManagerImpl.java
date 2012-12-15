package com.founder.e5.sys.note;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IClob;
import com.founder.e5.db.IResultSet;
import com.founder.e5.db.lob.StringClob;

/**
 * 公告管理器的具体实现类 所有返回的NOTE数组均不读取COLB字段值， 用户阅读公告时在进行读取。
 * 包括个人消息的处理，因此作为复杂公告类，暂不使用
 * @created 3-1-2007 16:20:02
 * @author sun.xf
 * @version 1.0
 */

public class NoteManagerImpl implements NoteManager {
	private static final String SQL_GET = "select * from FSYS_NOTES where NOTEID=?";

	private static final String SQL_GETREAD = "select * from FSYS_NOTESREAD where NOTEID=? and USERID=?";

	private static final String SQL_INSERT = "insert into FSYS_NOTES(NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC) values (?,?,?,?,?)";

	private static final String SQL_INSERTREAD = "insert into FSYS_NOTESREAD(NOTEID,USERID,READTIME) values (?,?,?)";

	private static final String SQL_UPDATEREAD = "update FSYS_NOTESREAD set READTIME=? where USERID=? and NOTEID=?";

	private static final String SQL_DELETE = "delete from FSYS_NOTES where NOTEID=?";

	private static final String SQL_DELETEREAD = "delete from FSYS_NOTESREAD where NOTEID=?";

	private static final String SQL_GETALL = "Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES order by NOTEID desc";
	
	private static final String SELECT_BY_TIMEZONE = "select NOTEID from FSYS_NOTES where NOTETYPE<2 and SENDERTIME<=?";

	//private static final String SQL_NEEDREAD = "Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES where NOTETYPE=0 and NOTEID not in (select NOTEID from FSYS_NOTESREAD where USERID=?)order by NOTEID desc ";
	private static String SQL_NEEDREAD;
	static {
		StringBuffer sql = new StringBuffer();
		sql.append("Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES where NOTETYPE=0");
		sql.append(" and NOTEID not in (select NOTEID from FSYS_NOTESREAD where USERID=?)");
		sql.append(" Union ");
		sql.append("Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES where NOTETYPE=2");
		sql.append(" and NOTEID in (select NOTEID from FSYS_NOTESREAD");
		sql.append(" where USERID=? and READTIME is null) ");
		sql.append(" order by NOTEID desc");
		
		SQL_NEEDREAD = sql.toString();
	}

	private static String SQL_GETALLBYUSER;
	static {
		StringBuffer sql = new StringBuffer();
		sql.append("Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES where NOTETYPE<2");
		sql.append(" and NOTEID not in (select NOTEID from FSYS_NOTESREAD where USERID=?)");
		sql.append(" Union ");
		sql.append("Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES where NOTETYPE>1");
		sql.append(" and NOTEID in (select NOTEID from FSYS_NOTESREAD");
		sql.append(" where USERID=? and READTIME is null) ");
		sql.append(" order by NOTEID desc");
		
		SQL_GETALLBYUSER = sql.toString();
	}

	private static String SQL_GETALREADYREAD;
	static {
		StringBuffer sql = new StringBuffer();
		sql.append("Select a.NOTEID,a.NOTETYPE,a.SENDER,a.SENDERTIME,a.TOPIC,");
		sql.append("b.READTIME from FSYS_NOTES a,FSYS_NOTESREAD b");
		sql.append(" where a.NOTEID=b.NOTEID and b.USERID=? and b.READTIME is not null order by a.NOTEID desc");
		
		SQL_GETALREADYREAD = sql.toString();
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#get(int)
	 */
	public Note get(int noteID) throws E5Exception {
		Object[] params = { new Integer(noteID) };
		
		DBSession db = Context.getDBSession();
		IResultSet rs = null;
		Note note = null;
		try {
			rs = db.executeQuery(SQL_GET, params);

			if (rs.next()) 
				note = getNote(rs);
			rs.close();
			
			return note;
		} catch (SQLException e) {
			throw new E5Exception("Get Note Error", e);
		} finally {
			db.closeQuietly();
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#getAll(java.lang.String)
	 */
	public Note[] getAll(String condition) throws E5Exception {
		StringBuffer sql = new StringBuffer(200);
		sql.append("Select NOTEID,NOTETYPE,SENDER,SENDERTIME,TOPIC from FSYS_NOTES where NOTETYPE<2 ");
		if (!StringUtils.isBlank(condition))
			sql.append(condition);
		sql.append(" order by NOTEID desc");

		return getNoteArray(sql.toString(), null);
	}

		
	/**
	 * 对SimpleNoteHelper方法的补充，加入了大字段的读取。
	 * @param rs 数据库指针
	 * @return Note 公告
	 * @throws E5Exception 异常
	 */
	private Note getNote(IResultSet rs) throws E5Exception {
		Note note = simpleNoteWrap(rs);
		
		IClob Clob;
		try {
			try{
			Clob = rs.getClob2("CONTENT");	
			}
			catch(IOException e){
				throw new E5Exception("Get clob error",e);
			}
			String content = Clob.toString();
			note.setContent(content);
			note.setSendTime(rs.getTimestamp("SENDERTIME"));
			return note;
		} catch (SQLException e) {
			throw new E5Exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#getAllByUser(int)
	 */
	public Note[] getAllByUser(int userID) throws E5Exception {
		Object[] params = { new Integer(userID), new Integer(userID) };
		return getNoteArray(SQL_GETALLBYUSER, params);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#getAlreadyRead(int)
	 */
	public Note[] getAlreadyRead(int userID) throws E5Exception {
		Object[] params = { new Integer(userID) };
		return getNoteArray(SQL_GETALREADYREAD, params);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#getHistory(int)
	 */
	public Note[] getHistory(int userID) throws E5Exception {
		Note[] NoteArray1 = getAllByUser(userID);
		List Notelist = new ArrayList(50);

		for (int i = 0; i < NoteArray1.length; i++) {
			Note note = NoteArray1[i];
			note.setRead(false);
			Notelist.add(note);
		}
		Note[] NoteArray2 = getAlreadyRead(userID);
		for (int i = 0; i < NoteArray2.length; i++) {
			Note note = NoteArray2[i];
			note.setRead(true);
			Notelist.add(note);
		}

		if (Notelist.size() != 0)
			return (Note[]) Notelist.toArray(new Note[0]);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#createNote(com.founder.e5.sys.note.Note)
	 */
	public int createNote(Note note) throws E5Exception {
		DBSession db = Context.getDBSession();
		try {
			note.setNoteID((int) EUID.getID("NoteID"));
			
			db.beginTransaction();
			String content = note.getContent();
			Object params[] = { new Integer(note.noteID),
					new Integer(note.noteType), note.sender, note.sendTime,
					note.topic };
			db.executeUpdate(SQL_INSERT, params);
			
			StringClob bb = new StringClob(content);
			db.writeClob((long) note.noteID, "FSYS_NOTES", "NOTEID", "CONTENT",
					bb);

			/**
			 * 对于个人消息，生成记录时同时生成接受者的读取记录，readtime为空。
			 */
			if (note.noteType > 1) {
				int[] receivers  = StringUtils.getIntArray(note.receivers);
				String insertsql = "insert into FSYS_NOTESREAD(NOTEID,USERID)values(?,?)";

				for (int i = 0; i < receivers.length; i++) {
					Object recordParams[] = { new Integer(note.noteID),
							 new Integer(receivers[i]) };
					db.executeUpdate(insertsql, recordParams);
				}
			}
			db.commitTransaction();

			return note.noteID;
		} catch (Exception e) {
			try {
				db.rollbackTransaction();
			} catch (Exception e1) {}
			
			throw new E5Exception("Create note error", e);
		} finally {
			db.closeQuietly();
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#readNote(int, int)
	 */
	public Note readNote(int noteID, int userID) throws E5Exception {
		Note note = get(noteID);

		Timestamp readtime = null;
		Object[] params1 = { new Integer(noteID), new Integer(userID) };
		DBSession db = Context.getDBSession();
		IResultSet rs = null;
		try {
			rs = db.executeQuery(SQL_GETREAD, params1);
			if (note.noteType <= 1) {
				if (!rs.next()) {
					readtime = new Timestamp(System.currentTimeMillis());

					Object[] params2 = { new Integer(noteID),
							new Integer(userID), readtime };
					db.executeUpdate(SQL_INSERTREAD, params2);
				}
			} else {
				if (rs.next()) {
					Timestamp read = rs.getTimestamp("READTIME");
					if (read == null) {
						readtime = new Timestamp(System.currentTimeMillis());
						Object[] params = { readtime, new Integer(userID),
								new Integer(noteID) };

						db.executeUpdate(SQL_UPDATEREAD, params);
					}
				}
			}
			rs.close();
			
			return note;
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			db.closeQuietly();
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#delete(int)
	 */
	public boolean delete(int NoteID) throws E5Exception {
		Object[] params = { new Integer(NoteID) };

		DBSession db = Context.getDBSession();
		try {
			db.beginTransaction();
			db.executeUpdate(SQL_DELETE, params);
			db.executeUpdate(SQL_DELETEREAD, params);
			db.commitTransaction();
			
			return true;
		} catch (Exception e) {
			try {
				db.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new E5Exception("delete ReadRecord Error", e);
		} finally {
			db.closeQuietly();
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#needRead(int)
	 */
	public boolean needRead(int userID) throws E5Exception {
		boolean NeedRead = true;
		Object params[] = { new Integer(userID), new Integer(userID) };
		DBSession db = Context.getDBSession();
		IResultSet rs = null;
		try {
			rs = db.executeQuery(SQL_NEEDREAD, params);
			if (!rs.next())
				NeedRead = false;
			rs.close();
			return NeedRead;
		} catch (SQLException e) {
			throw new E5Exception("See needRead Error", e);
		} finally {
			db.closeQuietly();
		}

	}

	/**
	 * 根据参数返回符合条件的note数组
	 * 
	 * @param sql 查询的SQL
	 * @param params 查询参数值
	 * @return Note[] Note数组
	 * @throws E5Exception 异常
	 */
	private Note[] getNoteArray(String sql, Object[] params) throws E5Exception {
		List noteList = getNoteList(sql, params);
		return (Note[]) noteList.toArray(new Note[0]);
	}
	private List getNoteList(String sql, Object[] params) throws E5Exception {
		List noteList = new ArrayList(20);

		DBSession db = Context.getDBSession();
		IResultSet rs = null;
		try {
			rs = db.executeQuery(sql, params);

			Note note = null;
			while (rs.next()) {
				note = simpleNoteWrap(rs);
				noteList.add(note);
			}
			rs.close();
			return noteList;
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			db.closeQuietly();
		}

	}

	/**
	 * 根据查询结果封装note,这里不对大字段进行封装。
	 * 
	 * @param rs 结果集指针
	 * @return note数组
	 * @throws E5Exception 异常
	 */
	private Note simpleNoteWrap(IResultSet rs) throws E5Exception {
		Note note = new Note();
		try {
			note.setNoteID(rs.getInt("NOTEID"));
			note.setSender(rs.getString("SENDER"));
			note.setNoteType(rs.getInt("NOTETYPE"));
			note.setTopic(rs.getString("TOPIC"));
			note.setSendTime(rs.getTimestamp("SENDERTIME"));

			return note;
		} catch (SQLException ex) {
			throw new E5Exception(ex);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#getAllNotes()
	 */
	public Note[] getAllNotes() throws E5Exception {
		return getNoteArray(SQL_GETALL, null);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.note.NoteManager#clearNote(java.util.Date)
	 */
	public boolean clearNote(Date timezone) throws E5Exception {
		Object[] params = {new Timestamp(timezone.getTime())};
		DBSession db = Context.getDBSession();
		IResultSet rs = null;
		List idList = null;		
		try {
			rs = db.executeQuery(SELECT_BY_TIMEZONE, params);
			idList = new ArrayList();
			while (rs.next())
				idList.add(new Integer(rs.getInt("NOTEID")));
			rs.close();
			
			int count = idList.size();
			if (count == 0) return false;
			else {
				for (int i = 0; i < count; i++)
					delete(((Integer) idList.get(i)).intValue());
			}
			return true;
		} catch (SQLException e) {
			throw new E5Exception("Delete notes error!", e);
		} finally {
			db.closeQuietly();
		}
	}
}
