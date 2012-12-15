package com.founder.e5.personality;

import com.founder.e5.context.E5Exception;

/**
 * ���Ի����ƹ����û��ӿڡ�<br>
 * <br>
 * ע�⣺�ýӿ����з����������͵ķ�����������null���ʾ��Ӧ�ļ�¼������
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 15:28:46
 */
public interface PersonalSettingReader {

	/**
	 * �����û�ID����ɫIDȡ���䶨�Ƶ���Դ�������ļ���ID���б�<br>
	 * ע�⣺�����ݿ���û�������ļ�¼���򷵻�null<br>
	 * �����ݿ���ȡ��֮����˵����Ϸ����ļ���ID�󷵻�
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @return �ļ���ID���飨���ݿ�������Ӧ��¼ʱ����null��
	 * @throws E5Exception
	 */
	public int[] getResourceTreeRoot( int userID, int roleID )
			throws E5Exception;

	/**
	 * �Ƿ���ʾ��Դ�����ڵ㣿<br>
	 * <br>
	 * ��������Ӱ�칤��ƽ̨��Դ����չʾ��ʽ������û���ĳ�ڵ���Ȩ�ޣ��������������ڵ���Ȩ�ޣ�������Ϊ��ʱ����ʾ���ڵ㣻������ʾ
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @return
	 * @throws E5Exception
	 */
	public boolean showTreeRoot( int userID, int roleID ) throws E5Exception;

	/**
	 * �Ƿ���ʾ��Դ�����ڵ㣿<br>
	 * <br>
	 * ��������Ӱ�칤��ƽ̨��Դ����չʾ��ʽ������û���ĳ�ڵ���Ȩ�ޣ������丸�ڵ���Ȩ�ޣ�������Ϊ��ʱ����ʾ�丸�ڵ㣻������ʾ
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @return
	 * @throws E5Exception
	 */
	public boolean showTreeParent( int userID, int roleID ) throws E5Exception;

	/**
	 * ȡ���ض��û���ͼ�������ѡ������
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @return ͼ�������ѡ������
	 * @throws E5Exception
	 * @see ConfigItem#ONLY_TEXT
	 * @see ConfigItem#ONLY_ICON
	 * @see ConfigItem#LEFTICON_RIGHTTEXT
	 * @see ConfigItem#TOPICON_DOWNTEXT
	 */
	public int getIconAndTextOption( int userID, int roleID )
			throws E5Exception;

	/**
	 * ���ĳ���ĵ����ͣ���ȡ�û�ѡ�õ��б�ʽ��<br>
	 * <br>
	 * ע�⣺����ĵ�����ID=0���򷵻������б�ʽ
	 * 
	 * @param userID
	 * @param roleID
	 * @param docTypeID �ĵ�����ID����Ϊ0���򷵻������б�ʽ
	 * @return �б�ʽID���飬˳������������ڽ�������ʾ��˳�����ݿ�������Ӧ��¼ʱ����null��
	 * @throws E5Exception
	 */
	public int[] getListPages( int userID, int roleID, int docTypeID )
			throws E5Exception;

	/**
	 * ��ȡĳ���б�ʽ��������Ϣ������ֵΪxml��ʽ<br>
	 * <br>
	 * ע�⣺������ֵΪnull����ʾ���ݿ���û����Ը����б�ʽ��������Ϣ
	 * 
	 * @param userID
	 * @param roleID
	 * @param listPageID �б�ʽID
	 * @return String[2]: 0-listXml 1-templateSlice
	 *         �ο�ListPage��listXml��templateSlice���Ժ��壨���ݿ�������Ӧ��¼ʱ����null��
	 * @throws E5Exception
	 */
	public String[] getListPageCfg( int userID, int roleID, int listPageID )
			throws E5Exception;

	/**
	 * ��ȡ������������ť�Ķ�����Ϣ��<br>
	 * <br>
	 * ��ǰ֧�ֵĲ������ĵ����ͺ����̽ڵ���� <br>
	 * ע�⣺������null����ʾ���ݿ��в����ڸö�����Ϣ
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 * @param docTypeID �ĵ�����ID����flowNodeID=0ʱ������
	 * @param fvID �ļ���ID�����衣Ϊ0ʱ���������á�
	 * @param flowNodeID ���̽ڵ�ID��Ϊ0ʱ��ʾֻ�з����̲���
	 * @return ����ID���飬��˳������˲�����ť��չʾ˳�����ݿ�������Ӧ��¼ʱ����null��
	 * @see com.founder.e5.flow.Proc
	 * @throws E5Exception
	 */
	public int[] getToolbarCfg( int userID, int roleID, int docTypeID, int fvID,
			int flowNodeID ) throws E5Exception;

}
