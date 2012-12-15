package com.founder.e5.sys.note;

import java.util.Date;

import com.founder.e5.context.E5Exception;

/**
 * ����/��Ϣ������
 * 
 * @created 21-7-2005 16:20:02
 * @author sun.xf
 * @version 1.0
 */
public interface NoteManager {
	/**
	 * ���ݹ���ID��ȡһ������
	 * @param noteID ����ID
	 */
	public Note get(int noteID) throws E5Exception;

	/**
	 * ����������Ϣ
	 */
	public Note[] getAllNotes() throws E5Exception;

	/**
	 * ����ָ���������ع�����Ϣ
	 * @param condtion ����������Ϊ��ʱ�������й�����Ϣ
	 */
	public Note[] getAll(String condtion) throws E5Exception;

	/**
	 * ����һ���û�������δ����Ϣ����������δ����Ϣ���Լ�����δ����Ϣ��
	 * @param UserID �û�ID
	 */
	public Note[] getAllByUser(int UserID) throws E5Exception;

	/**
	 * ����һ���û����յ����й��棨�Ѷ���δ����������������Ϣ��������Ϣ
	 * @param UserID �û�ID
	 */
	public Note[] getHistory(int userID) throws E5Exception;

	/**
	 * ����ĳ�û��������Ѷ���Ϣ������������Ϣ�͹��� 
	 * @param userID �û�ID
	 */
	public Note[] getAlreadyRead(int userID) throws E5Exception;
	/**
	 * �������棬���ش����Ĺ���ID
	 * @param note 
	 */
	public int createNote(Note note) throws E5Exception;

	/**
	 * ɾ��һ������ ����ɾ���ɹ���־
	 * @param noteID ����ID
	 * @return boolean �Ƿ�ɾ���ɹ�
	 */
	public boolean delete(int noteID) throws E5Exception;

	/**
	 * �ж�ĳ�û��Ƿ���δ���Ĺ���������Ϣ��
	 * @param userID �û�ID
	 * @return boolean ������δ���Ĺ���������ϢʱΪtrue
	 */
	public boolean needRead(int userID) throws E5Exception;

	/**
	 * �������ʱ����ǰ�Ĺ���
	 * @param timelimit 
	 */
	public boolean clearNote(Date timelimit) throws E5Exception;

	/**
	 * �Ķ����� ���Ǹ��û���һ���Ķ��˹��棬������Ķ�ʱ��
	 * @param noteID ����ID
	 * @param userID �û�ID
	 * @return ����
	 * @throws E5Exception
	 */
	public Note readNote(int noteID, int userID) throws E5Exception;

}
