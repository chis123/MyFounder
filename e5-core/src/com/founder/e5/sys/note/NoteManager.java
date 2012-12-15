package com.founder.e5.sys.note;

import java.util.Date;

import com.founder.e5.context.E5Exception;

/**
 * 公告/消息管理器
 * 
 * @created 21-7-2005 16:20:02
 * @author sun.xf
 * @version 1.0
 */
public interface NoteManager {
	/**
	 * 根据公告ID读取一个公告
	 * @param noteID 公告ID
	 */
	public Note get(int noteID) throws E5Exception;

	/**
	 * 返回所有消息
	 */
	public Note[] getAllNotes() throws E5Exception;

	/**
	 * 根据指定条件返回公共消息
	 * @param condtion 过滤条件。为空时返回所有公共消息
	 */
	public Note[] getAll(String condtion) throws E5Exception;

	/**
	 * 返回一个用户的所有未读消息，包括公共未读消息，以及个人未读消息。
	 * @param UserID 用户ID
	 */
	public Note[] getAllByUser(int UserID) throws E5Exception;

	/**
	 * 返回一个用户接收的所有公告（已读和未读），包括公共消息，个人消息
	 * @param UserID 用户ID
	 */
	public Note[] getHistory(int userID) throws E5Exception;

	/**
	 * 返回某用户的所有已读消息，包括个人消息和公告 
	 * @param userID 用户ID
	 */
	public Note[] getAlreadyRead(int userID) throws E5Exception;
	/**
	 * 创建公告，返回创建的公告ID
	 * @param note 
	 */
	public int createNote(Note note) throws E5Exception;

	/**
	 * 删除一个公告 返回删除成功标志
	 * @param noteID 公告ID
	 * @return boolean 是否删除成功
	 */
	public boolean delete(int noteID) throws E5Exception;

	/**
	 * 判断某用户是否有未读的公共紧急消息。
	 * @param userID 用户ID
	 * @return boolean 当存在未读的公共紧急消息时为true
	 */
	public boolean needRead(int userID) throws E5Exception;

	/**
	 * 清除所给时间以前的公告
	 * @param timelimit 
	 */
	public boolean clearNote(Date timelimit) throws E5Exception;

	/**
	 * 阅读公告 如是该用户第一次阅读此公告，则更新阅读时间
	 * @param noteID 公告ID
	 * @param userID 用户ID
	 * @return 公告
	 * @throws E5Exception
	 */
	public Note readNote(int noteID, int userID) throws E5Exception;

}
