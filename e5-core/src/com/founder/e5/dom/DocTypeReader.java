package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * �ĵ����͹��������ṩ��Ӧ�ò�ʹ�� depends on context.jar
 * 
 * @version 1.0
 * @updated 11-����-2005 13:59:28
 */
public interface DocTypeReader {

    /**
     * �����ĵ�����ID��ȡ�ĵ����Ͷ���
     * 
     * @param docTypeID �ĵ�����ID
     * @return ��������ڣ��򷵻�null
     * @throws E5Exception
     */
    public DocType get(int docTypeID) throws E5Exception;

    /**
     * �����ĵ����ͣ�Ψһ�����ƻ�ȡ�ĵ����Ͷ���
     * 
     * @param docTypeName �ĵ���������
     * @return ��������ڣ��򷵻�null
     * @throws E5Exception
     */
    public DocType get(String docTypeName) throws E5Exception;

    /**
     * ����Ӧ����ϵͳ��ID��ȡ�����ĵ����ͣ�Ĭ�ϰ����ĵ�����ID��������
     * 
     * @param appID ��ϵͳID�����Ϊ0���򷵻����е��ĵ�����
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocType[] getTypes(int appID) throws E5Exception;

    /**
     * ��ȡָ���ĵ�����������Ӧ�õ�id
     * 
     * @param docTypeID �ĵ�����ID
     * @return ��������ڣ��򷵻�0
     * @throws E5Exception
     */
    public int getAppID(int docTypeID) throws E5Exception;

    /**
     * ������ϵͳID����ȡ���е��ĵ�����ID��Ĭ�ϰ����ĵ�����ID��������
     * 
     * @param appID ��ϵͳID
     * @return ��������ڣ��򷵻ؿ����飻�������Ϊ0���򷵻����е��ĵ�����ID
     * @throws E5Exception
     */
    public int[] getIDs(int appID) throws E5Exception;

    /**
     * ��ȡָ��id���ֶ�
     * 
     * @param fieldID �ĵ������ֶ�ID
     * @return ���û���򷵻�null
     * @throws E5Exception
     */
    public DocTypeField getField(int fieldID) throws E5Exception;

    /**
     * ��ȡָ���ĵ������µ������ֶ�
     * ���ص������ֶ�Ĭ�ϰ���id��������
     * 
     * @param docTypeID �ĵ�����ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocTypeField[] getFields(int docTypeID) throws E5Exception;
    
    /**
     * ��ȡָ���ĵ������µ����е��ֶΣ�����ָ���ֶε�˳��
     * 
     * @param docTypeID �ĵ�����ID
     * @param fieldName ������ֶ�����
     * @param order ����Ϊ���ַ���������Ϊdesc
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsOrderBy(int docTypeID ,String fieldName, String order) throws E5Exception;

    /**
     * ��ȡָ���ֶε�����
     * 
     * @param fieldID
     * @return ���û���򷵻�null
     * @throws E5Exception
     */
    public String getFieldName(int fieldID) throws E5Exception;

    /**
     * ��ȡָ�����͵�������չ�ֶΣ�����Ӧ�ü��ֶκ��û����ֶ�
     * Ĭ�ϰ���id����������
     * 
     * @param docTypeID �ĵ�����ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsExt(int docTypeID) throws E5Exception;
    
    /**
     * ��ȡָ�����͵�����Ӧ�ü��ֶΣ�
     * Ĭ�ϰ���id����������
     * 
     * @param docTypeID �ĵ�����ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsApp(int docTypeID) throws E5Exception;
    
    /**
     * ��ȡָ�����͵������û����ֶΣ�
     * Ĭ�ϰ���id����������
     * 
     * @param docTypeID �ĵ�����ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsUser(int docTypeID) throws E5Exception;
    
    /**
     * ��ȡָ�����͵�����ϵͳ�ֶΣ�Ĭ�ϰ���id������
     * @param docTypeID �ĵ�����ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DocTypeField[] getSysFields(int docTypeID) throws E5Exception;
    
    /**
     * �����ĵ�����ID�������ֶε�Code������ֶζ���
     * ����û���ж��ֶ��Ƿ��Ѿ���ɾ������Ϊ�����ĵ������ֶ�ʱ��ֻҪ����ͬ���ģ����ܴ���
     * @param docTypeID �ĵ�����ID
     * @param columnCode �ֶ�Ӣ����
     * @return ���û�У��򷵻�null
     * @throws E5Exception
     */
    public DocTypeField getField(int docTypeID,String columnCode)throws E5Exception;

}