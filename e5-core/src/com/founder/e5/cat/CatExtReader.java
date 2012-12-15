package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * ������չ���Զ�ȡ��
 * ������չ�������͵Ķ�ȡ<br>
 * ��չ������ϵͳ�����ڷ���ı�����
 * 
 * @created 21-7-2005 16:20:07
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatExtReader {

	/**
	 * ���ݷ�������ID������ID����չ������������,�õ��÷������չ����
	 * 
	 * @param catType ��������ID
	 * @param catID   ����ID
	 * @param extType ������չ��������������Ҫ�Ȳ�����չ��������ID
	 * @return CatExt 
	 * @throws E5Exception
	 */
	public CatExt getExt(int catType, int catID, String extType) 
	throws E5Exception;

	/**
	 * ���ݷ�������ID������ID��������չ��������ID,�õ��÷������չ����
	 * 
	 * @param catType ��������ID
	 * @param catID   ����ID
	 * @param extType ������չ��������ID
	 * @return CatExt
	 * @throws E5Exception
	 */
	public CatExt getExt(int catType, int catID, int extType) 
	throws E5Exception;

	/**
	 * ���ݷ�������ID������IDȡ�÷����������չ����
	 * 
	 * @param catType ��������ID
	 * @param catID ����ID
	 * @return ��չ��������
	 * @throws E5Exception
	 */
	public CatExt[] getExts(int catType, int catID) 
	throws E5Exception;

	/**
	 * ȡһ���������͵����з����ĳ����չ����
	 * @param catType ��������ID
	 * @param extType ��չ����������Ҫ�Ȳ���һ����չ����
	 * @return ��չ�������� 
	 * @throws E5Exception
	 */
	public CatExt[] getAllExts(int catType, String extType)
	throws E5Exception;

	/**
	 * ȡһ���������͵����з����ĳ����չ����
	 * @param catType ��������ID
	 * @param extType ��չ����ID
	 * @return ��չ����ȡֵ����
	 * @throws E5Exception
	 */
	public CatExt[] getAllExts(int catType, int extType)
	throws E5Exception;

	/**
	 * ȡһ�����������ֱ���ӷ����ĳһ����չ����
	 * @param catType ��������ID
	 * @param catID   ����ID
	 * @param extType ������չ��������������Ҫ�Ȳ���һ����չ����ID
	 * @return ��չ��������
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int catID, String extType) 
	throws E5Exception;

	/**
	 * ȡһ�����������ֱ���ӷ����ĳһ����չ����
	 * @param catType ��������ID
	 * @param catID   ����ID
	 * @param extType ������չ��������ID
	 * @return ��չ��������
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int catID, int extType) 
	throws E5Exception;

	/**
	 * ȡĳЩ���������ֱ���ӷ����ĳһ����չ����
	 * ���ص����鰴����ID�����������
	 * @param catType ��������ID
	 * @param catIDs  ����ID����
	 * @param extType ������չ��������������Ҫ�Ȳ���һ����չ����ID
	 * @return ��չ��������
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, String extType) 
	throws E5Exception;

	/**
	 * ȡĳЩ���������ֱ���ӷ����ĳһ����չ����
	 * ���ص����鰴����ID�����������
	 * @param catType ��������ID
	 * @param catIDs  ����ID����
	 * @param extType ������չ��������ID
	 * @return ��չ��������
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, int extType) 
	throws E5Exception;

	/**
	 * ������ȡһ����չ��������
	 * @param extType ��չ������������
	 * @return ������չ��������
	 * @throws E5Exception
	 */
	public CatExtType getExtType(String extType)
	throws E5Exception;

	/**
	 * ��ID��ȡ��չ��������
	 * @param extType ��չ��������ID
	 * @return ������չ�������� 
	 * @throws E5Exception
	 */
	public CatExtType getExtType(int extType)
	throws E5Exception;
	
	/**
	 * ȡ�����з�����չ��������
	 * @return ������չ������������
	 * @throws E5Exception
	 */
	public CatExtType[] getExtTypes()
	throws E5Exception;

	/**
	 * ���ݷ������ͣ���ȡ��ǰ��������֧�ֵ���������
	 *
	 * @param catType
	 * @return ������չ������������
	 * @throws E5Exception
	 */
	public CatExtType[] getExtTypes(int catType)
	throws E5Exception;
}