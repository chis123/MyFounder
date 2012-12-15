package com.founder.e5.sys.org;
import com.founder.e5.sys.org.OrgType;

/**
 * @created 04-����-2005 14:40:11
 * @version 1.0
 * @updated 11-����-2005 12:41:44
 */
public interface OrgTypeReader {

	/**
	 * ͨ��ָ������֯����ID�����֯����������
	 * @param typeID    ��֯���������ID
	 * 
	 */
	public String getTypeName(String typeID);

	/**
	 * ͨ����֯���������ƻ����֯������ID
	 * @param typeName    ��֯��������
	 * 
	 */
	public String getTypeID(String typeName);

	/**
	 * ������е���֯����ID���������Ƶ�ӳ�����
	 */
	public OrgType[] get();

}