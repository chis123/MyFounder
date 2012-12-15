package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * �����ȡ��,���ڶ�ȡ����ͷ�������<br>
 * ��ȡʵ���ķ�����CatReader reader = (CatReader)com.founder.e5.context.Context.getBean("CatReader");
 * 
 * @created 21-7-2005 16:28:14
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatReader {

	/**
	 * ���ݷ����������ơ�����ID����չ��������ȡ��һ�����ࡣ
	 * ���ݲ�����ָ������չ�������ͣ������Ƿ�ʹ�ñ������Լ�ʹ����һ��������
	 * ʹ�ñ������ñ�����ȡֵ�ͼ��������滻��ǰ��������ƺͷ��༶�����Ʒ��أ�
	 * ��ָ������չ�������ͶԸ÷�����Բ������ڣ��򷵻ص�������������
	 * 
	 * @param catTypeName    ����������
	 * @param catID    ����ID
	 * @param extType    ��չ�������ͣ�null��ʾ������
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(String catTypeName, int catID, String extType) 
	throws E5Exception;

	/**
	 * ���ݷ����������ơ�����IDȡ��һ������
	 * @param catTypeName ����������
	 * @param catID ����ID
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(String catTypeName, int catID) 
	throws E5Exception;

	/**
	 * ���ݷ�������ID������ID����չ��������ȡ��һ������
	 * ���ݲ�����ָ������չ�������ͣ������Ƿ�ʹ�ñ������Լ�ʹ����һ��������
	 * ʹ�ñ������ñ�����ȡֵ�ͼ��������滻��ǰ��������ƺͷ��༶�����Ʒ��أ�
	 * ��ָ������չ�������ͶԸ÷�����Բ������ڣ��򷵻ص�������������
	 * @param catType    ��������ID
	 * @param catID    ����ID
	 * @param extType    ��չ�������ͣ�null��ʾ������
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(int catType, int catID, String extType)
	throws E5Exception;

	/**
	 * ���ݷ�������ID������IDȡ��һ������
	 * @param catType ��������ID
	 * @param catID ����ID
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(int catType, int catID)
	throws E5Exception;

	/**
	 * ���ݷ�������ID����������ȡ��һ������
	 * @param catType ��������ID
	 * @param catName ��������
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCatByName(int catType, String catName)
	throws E5Exception;
	/**
	 * ȡһ���������͵����з��ࣨ��ĳ����չ�������ͣ�
	 * ���ݲ�����ָ������չ�������ͣ������Ƿ�ʹ�ñ������Լ�ʹ����һ��������
	 * ʹ�ñ������ñ�����ȡֵ�ͼ��������滻��ǰ��������ƺͷ��༶�����Ʒ��أ�
	 * ��ָ������չ�������ͶԸ÷�����Բ������ڣ��򷵻ص�������������
	 * @param catTypeName    ����������
	 * @param extType    ��չ�������ͣ�null��ʾ������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getCats(String catTypeName, String extType)
	throws E5Exception;

	/**
	 * ȡһ���������͵����з���
	 * @param catTypeName ������������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getCats(String catTypeName)
	throws E5Exception;
	/**
	 * ȡһ���������͵����з��ࣨ��ĳ����չ�������ͣ�
	 * ���ݲ�����ָ������չ�������ͣ������Ƿ�ʹ�ñ������Լ�ʹ����һ��������
	 * ʹ�ñ������ñ�����ȡֵ�ͼ��������滻��ǰ��������ƺͷ��༶�����Ʒ��أ�
	 * ��ָ������չ�������ͶԸ÷�����Բ������ڣ��򷵻ص�������������
	 * @param catType ��������ID
	 * @param extType    ��չ�������ͣ�null��ʾ������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getCats(int catType, String extType)
	throws E5Exception;

	/**
	 * ȡһ���������͵����з���
	 * @param catType
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getCats(int catType)
	throws E5Exception;
	/**
	 * ȡһ������������ӷ��ࣨ��ĳ����չ�������ͣ�
	 * @param catType ��������ID
	 * @param catID ������IDΪ0ʱ��ȡ�÷��������µ����з���
	 * @param extType ��չ�������ͣ�0��ʾ������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getChildrenCats(int catType, int catID, int extType)
	throws E5Exception;

	/**
	 * ȡһ������������ӷ��ࡣ������IDΪ0ʱ��ȡ�÷��������µ����з���
	 * @param catType ��������ID
	 * @param catID ����ID
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getChildrenCats(int catType, int catID)
	throws E5Exception;
	/**
	 * ȡһ������������ӷ��ࣨ��ĳ����չ�������ͣ�
	 * @param catType ��������ID
	 * @param catID ������IDΪ0ʱ��ȡ�÷��������µ����з���
	 * @param extType ��չ�������ͣ�null��ʾ������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getChildrenCats(int catType, int catID, String extType)
	throws E5Exception;
	/**
	 * ȡһ�����������"ֱ��"�ӷ���
	 * ���ݲ�����ָ������չ�������ͣ������Ƿ�ʹ�ñ������Լ�ʹ����һ��������
	 * ʹ�ñ������ñ�����ȡֵ�ͼ��������滻��ǰ��������ƺͷ��༶�����Ʒ��أ�
	 * ��ָ������չ�������ͶԸ÷�����Բ������ڣ��򷵻ص�������������
	 * @param catTypeName ����������
	 * @param catID ����ID ������IDΪ0ʱ��ȡ�÷��������µ����и�����
	 * @param extType    ��չ�������ͣ�null��ʾ������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getSubCats(String catTypeName, int catID, String extType)
	throws E5Exception;

	/**
	 * ȡһ�����������"ֱ��"�ӷ���
	 * @param catTypeName ������������
	 * @param catID ������IDΪ0ʱ��ȡ�÷��������µ����и�����
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getSubCats(String catTypeName, int catID)
	throws E5Exception;
	/**
	 * ȡһ�����������"ֱ��"�ӷ���
	 * ���ݲ�����ָ������չ�������ͣ������Ƿ�ʹ�ñ������Լ�ʹ����һ��������
	 * ʹ�ñ������ñ�����ȡֵ�ͼ��������滻��ǰ��������ƺͷ��༶�����Ʒ��أ�
	 * ��ָ������չ�������ͶԸ÷�����Բ������ڣ��򷵻ص�������������
	 * @param catType ��������ID
	 * @param catID ����ID ������IDΪ0ʱ��ȡ�÷��������µ����и�����
	 * @param extType    ��չ�������ͣ�null��ʾ������
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getSubCats(int catType, int catID, String extType)
	throws E5Exception;

	/**
	 * ȡһ�����������"ֱ��"�ӷ���
	 * @param catType ��������ID
	 * @param catID ������IDΪ0ʱ��ȡ�÷��������µ����и�����
	 * @return ��������
	 * @throws E5Exception
	 */
	public Category[] getSubCats(int catType, int catID)
	throws E5Exception;
	
	/**
	 * ���ݷ�������IDȡһ����������
	 * 
	 * @param id ��������ID
	 * @return ��������CatType
	 * @throws E5Exception
	 */
	public CatType getType(int catType)
	throws E5Exception;

	/**
	 * ��������ȡһ����������
	 * @param name ������������
	 * @return ��������CatType
	 * @throws E5Exception
	 */
	public CatType getType(String name)
	throws E5Exception;

	/**
	 * ȡ���еķ�������
	 * @return ������������
	 * @throws E5Exception
	 */
	public CatType[] getTypes()
	throws E5Exception;
	
	/**
	 * ���ݷ���ID����ȡ�÷���,���鳤��ӦС��1000
	 * @param catType ��������ID
	 * @param catIDs ����ID����,��ʽΪ:1,2,3 ...,n
	 * @return ����catIDs�ķ�������,�����鳤�ȿ�����catIDs�е�id������ͬ,��Щid����û�鵽
	 * @throws E5Exception
	 */
	public Category[] getCat(int catType,String catIDs)
	throws E5Exception;
	

}