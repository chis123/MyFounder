package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * ���������
 * @created 21-7-2005 16:28:18
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatManager extends CatReader {

	/**
	 * ����һ���µķ�������
	 * @param catType
	 */
	public void createType(CatType catType)
	throws E5Exception;

	/**
	 * �޸�һ����������
	 * @param catType
	 */
	public void updateType(CatType catType)
	throws E5Exception;

	/**
	 * ɾ��һ����������
	 * ͬʱ�Ѹ������µ����з��඼ɾ��
	 * ��ɾ�������
	 * @param id ��������ID
	 */
	public void deleteType(int id)
	throws E5Exception;

	/**
	 * �����ֲ��ҷ���
	 * ֧��ģ����ѯ
	 * �ù����ڷ��������ʹ�ã����ڿ��ٶ�λһ������
	 * @param catType ��������ID
	 * @param catName ������
	 */
	public Category[] getCatsByName(int catType, String catName)
	throws E5Exception;

	/**
	 * ��������
	 * ֻ������������Ϣ����չ������ϢҪ��������
	 * @param cat
	 */
	public void createCat(Category cat)
	throws E5Exception;

	/**
	 * <p>�޸�һ������Ļ�������</p>
	 * <p>ע��������޸Ĳ������ǲ�α仯��Ҳ����˵��������parentID��level���Եı仯
	 * <BR>��α仯�ǵ������еĶ�������Ҫ����<code>move</code>����</p>
	 * <BR>����������Ʒ����仯ʱ�����Լ��������ӷ���ļ������ƶ�Ҫ�����仯
	 * @param cat
	 */
	public void updateCat(Category cat)
	throws E5Exception;

	/**
	 * ��һ�������ƶ�����һ������֮��
	 * ����Ĳ�ο��ܷ����仯
	 * �������α仯ʱ�����������ӷ���Ĳ�Σ��Լ���������Ҳͬʱ�����仯
	 * @param catType ��������ID
	 * @param cat ��Ҫ�ƶ��ķ���ID
	 * @param catID Ŀ�������ID
	 * @throws E5Exception
	 */
	public void move(int catType, int srcCatID, int destCatID)
	throws E5Exception;
	
	/**
	 * �ѶԸ������ĳЩ���Ե��޸Ĵ��ݵ��ӷ���
	 * @param cat ���������޸����Եĸ�����
	 * @param fields ָ����Ҫ����ͬ�������ԣ��μ�Category���еĳ�������
	 * @throws E5Exception 
	 */
	public void updateTransfer(Category cat, int[] fields) 
	throws E5Exception;

	/**
	 * ɾ��һ������
	 * ͬʱ�����е��ӷ���ɾ��
	 * @param catType ��������ID
	 * @param catID ����ID
	 */
	public void deleteCat(int catType, int catID)
	throws E5Exception;

	/**
	 * ɾ��һ������
	 * ͬʱ�����е��ӷ���ɾ��
	 * @param catTypeName ����������
	 * @param catID ����ID
	 */
	public void deleteCat(String catTypeName, int catID)
	throws E5Exception;
	
	/**
	 * �ָ�һ����ɾ���ķ���
	 * @param catType
	 * @param catID
	 * @throws E5Exception
	 */
	public void restoreCat(int catType, int catID)
	throws E5Exception;
	/**
	 * ȡ�����Ѿ�ɾ���ķ���
	 * @param catType ��������ID
	 * @return ��ɾ���������
	 * @throws E5Exception
	 */
	public Category[] getAllDeleted(int catType)
	throws E5Exception;
	
	
	/**
	 * ȡ�÷��������ڵ㣨�����Ѿ�ɾ���ķ��ࣩ
	 * 
	 * @param catType - ��������
	 * @param catID - ����ID
	 * @return ��������
	 */
	public Category[] getChildrenCatsIncludeDeleted(int catType,int catID,int extType)
	throws E5Exception;
	
}