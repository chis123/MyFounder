package com.founder.e5.personality;

import com.founder.e5.context.E5Exception;

/**
 * ���Ի����ƹ��ܹ���ӿ�
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 15:33:07
 */
public interface PersonalSettingManager extends PersonalSettingReader {

	/**
	 * ����ĳ�û�����Դ�����б�<br>
	 * <br>
	 * ע�⣺����֮ǰ��鴫����ļ���ID�Ƿ�Ϸ����Ϸ����������£�<br>
	 * 1�����ڶ�Ӧ���ļ��У���黺������Ӧ���ļ���ʵ���Ƿ���ڣ�<br>
	 * 2���û���ɫ�Ը��ļ�����Ȩ��<br>
	 * 3���������δ����
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @param folderIDs �ļ���ID����
	 * @throws E5Exception
	 */
	public void setResourceTreeRoot( int userID, int roleID, int[] folderIDs )
			throws E5Exception;

	/**
	 * ����ĳ�û�����Դ�����б�<br>
	 * <br>
	 * ע�⣺����֮ǰ��鴫����ļ���ID�Ƿ�Ϸ����Ϸ����������£�<br>
	 * 1�����ڶ�Ӧ���ļ��У���黺������Ӧ���ļ���ʵ���Ƿ���ڣ�<br>
	 * 2���û���ɫ�Ը��ļ�����Ȩ��<br>
	 * 3���������δ����
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @param folderIDs �ö��Ŵ������ļ���ID
	 * @throws E5Exception
	 */
	public void setResourceTreeRoot( int userID, int roleID, String folderIDs )
			throws E5Exception;

	/**
	 * �����Ƿ���ʾ��Դ�����ڵ�<br>
	 * <br>
	 * ��������Ӱ�칤��ƽ̨��Դ����չʾ��ʽ������û���ĳ�ڵ���Ȩ�ޣ��������������ڵ���Ȩ�ޣ�������Ϊ��ʱ����ʾ���ڵ㣻
	 * 
	 * @param userID
	 * @param roleID
	 * @param enabled �Ƿ�����
	 * @throws E5Exception
	 */
	public void setShowTreeRoot( int userID, int roleID, boolean enabled )
			throws E5Exception;

	/**
	 * �����Ƿ���ʾ��Դ�����ڵ�<br>
	 * <br>
	 * ��������Ӱ�칤��ƽ̨��Դ����չʾ��ʽ������û���ĳ�ڵ���Ȩ�ޣ������丸�ڵ���Ȩ�ޣ�������Ϊ��ʱ����ʾ�丸�ڵ㣻
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @param enabled �Ƿ�����
	 * @throws E5Exception
	 */
	public void setShowTreeParent( int userID, int roleID, boolean enabled )
			throws E5Exception;

	/**
	 * �����ض��û���ͼ�������ѡ������
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @param configValue ͼ�������ѡ������
	 * @throws E5Exception
	 * @see ConfigItem#ONLY_TEXT
	 * @see ConfigItem#ONLY_ICON
	 * @see ConfigItem#LEFTICON_RIGHTTEXT
	 * @see ConfigItem#TOPICON_DOWNTEXT
	 */
	public void setIconAndTextOption( int userID, int roleID, int configValue )
			throws E5Exception;

	/**
	 * �����û�ѡ�õ��б�ʽ
	 * 
	 * @param userID
	 * @param roleID
	 * @param listIDs �Զ������ӵ��б�ʽID�����а��������ĵ������µ��б�ʽ��ͬһ�ĵ��������б�ʽID��˳��������
	 * @throws E5Exception
	 */
	public void setListPages( int userID, int roleID, String listIDs )
			throws E5Exception;

	/**
	 * �����û���ĳ���б�ʽ������
	 * 
	 * @param userID
	 * @param roleID
	 * @param listPageID
	 * @param listXml �����ֶε����ã���ʽͬListPage.listXml
	 * @param templateSlice ��ʾ�ֶε����ã���ʽͬListPage.templateSlice
	 * @throws E5Exception
	 */
	public void setListPageCfg( int userID, int roleID, int listPageID,
			String listXml, String templateSlice ) throws E5Exception;

	/**
	 * ���ù�����������ť������Ϣ
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @param docTypeID �ĵ�����ID����flowNodeID=0ʱ������
	 * @param fvID �ļ���ID�����衣Ϊ0ʱ���������á�
	 * @param flowNodeID ���̽ڵ�ID��Ϊ0ʱ��ʾֻ�з����̲���
	 * @param procIDs ����ID���У��Զ������ӣ���˳��������
	 * @throws E5Exception
	 */
	public void setToolbarCfg( int userID, int roleID, int docTypeID,
			int fvID, int flowNodeID, String procIDs ) throws E5Exception;

	/**
	 * ɾ��ĳ�û���ĳ��ɫ�����еĸ��Ի�����������Ϣ
	 * 
	 * @param userID
	 * @param roleID
	 * @throws E5Exception
	 */
	public void deleteAllSetting( int userID, int roleID ) throws E5Exception;

	/**
	 * ɾ��ĳ�û����еĸ��Ի�����������Ϣ
	 * 
	 * @param userID
	 * @throws E5Exception
	 */
	public void deleteAllSetting( int userID ) throws E5Exception;

}
