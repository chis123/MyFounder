package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * ������չ���Թ�����
 * ������չ�������͵Ĺ���
 * @created 21-7-2005 16:20:02
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatExtManager extends CatExtReader
{
	/**
	 * ����Ķ����չ����ֻ��һ���Ա���
	 * ����ʱ��������÷�������о���չ���ԣ�Ȼ�󱣴��µ���չ����
	 * ���������չ����������null����ֻ���������
	 * @param catType ��������
	 * @param catID ����ID
	 * @param catExtArray ��չ�������飻ע����Щ��չ���Ա��붼����ͬһ������
	 */
	public void saveExt(int catType, int catID, CatExt[] catExtArray)
	throws E5Exception;

	/**
	 * ɾ��һ������ʱ�ѷ����������չ����ͬʱɾ��
	 * 
	 * �������չ���������屣��ģ�
	 * û�����ĳһ����չ���Խ���ɾ���ķ���
	 * 
	 * @param catType
	 * @param catID
	 */
	public void deleteCat(int catType, int catID)
	throws E5Exception;

	/**
	 * ������չ��������
	 * 
	 * @param extType
	 */
	public void createExtType(CatExtType extType)
	throws E5Exception;

	/**
	 * ɾ��һ����չ��������
	 * ͬʱɾ���������µ�������չ����
	 * @param extType
	 */
	public void deleteExtType(int extType)
	throws E5Exception;

	/**
	 * �޸���չ��������
	 * 
	 * @param extType
	 */
	public void updateExtType(CatExtType extType)
	throws E5Exception;

}