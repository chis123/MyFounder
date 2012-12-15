package com.founder.e5.dom;

import org.dom4j.Element;

import com.founder.e5.context.E5Exception;

/**
 * @created 05-����-2005 9:02:26
 * @author kaifeng zhang
 * @version 1.0
 */
public interface DocTypeManager extends DocTypeReader {
        

    /**
     * �����ĵ�����
     * 
     * @param docType1��������õ���ʱ̬docType������ID��create�����и�ֵ��
     * @throws E5Exception
     *             ����Ѿ�������ͬname��docType����������docType����ʱ�����쳣�����׳�E5Exception
     */
    public int create(DocType docType) throws E5Exception;
    
    /**
     * �����ĵ���������ϵͳ�Ĺ���
     * 2006-4-7 10:36:41 by Zhang Kaifeng
     * @param docTypeApps
     * @throws E5Exception
     */
    public void create(DocTypeApps docTypeApps) throws E5Exception;
    
    /**
     * �����ĵ�����
     * 2006-3-13 14:51:55
     * @author zhang_kaifeng
     * @param docTypeName
     * @param appID
     * @return �ĵ�����ID
     * @throws E5Exception
     */
    public int create(String docTypeName,int appID) throws E5Exception;

    /**
     * �����ĵ������ֶΡ�
     * 
     * ��������һ���ֶ�ʱ���������漰����ÿһ���ĵ��⣨�����ĵ�����ȷ������
     * 
     * ����һ��doctypefieldApps��¼��isupdated��isadded����Ϊ0��
     * 
     * �����ĵ�������������������ֶζ���0�������
     * 
     * ���¶�Ӧ���ĵ����idadded��Ϊ1��idupdated����
     * 
     * @param doctypeField
     * @throws E5Exception
     */
    public int create(DocTypeField doctypeField) throws E5Exception;

    /**
     * ����IDɾ��һ���ĵ����͡�ж����ϵͳʱʹ�á��ճ��������ʱ����ɾ���ĵ����͵Ĳ�����
     * 
     * @param doctypeID
     * @throws E5Exception
     */
    public void delete(int doctypeID) throws E5Exception;
    
    /**
     * ɾ��һ���ĵ����͸�ĳ��ϵͳ�Ĺ���
     * 2006-4-7 10:37:31 by Zhang Kaifeng
     * @param appID
     * @param docTypeID
     * @throws E5Exception
     */
    public void delete(int appID,int docTypeID)throws E5Exception;

    /**
     * ɾ��һ���ֶ�
     * 
     * ��ɾ��һ���ֶ�ʱ����fieldapps���ҵ���Ӧ�ֶμ�¼�����û�����²��롣
     * 
     * ��isupdatedΪ��1
     * 
     * @param fieldID
     * @throws E5Exception
     */
    public void deleteField(int fieldID) throws E5Exception;

    /**
     * ��ȡdocTypeFieldApps����ȡ��Ҫ���µ�docLib��id
     * ���ڸ���DocTypeFieldApps��isDBAdded��isDBUpdated�������жϣ���0��
     * @param docTypeID 
     * 
     * @return ��Ҫ���µ��ĵ������飬���û���򷵻ؿ�����
     * @throws E5Exception
     */
    public DocLib[] getDocLibsToBeUpdated(int docTypeID) throws E5Exception;

    /**
     * 
     * @param appID
     * @throws E5Exception
     */
    public String saveTemplateXMLByAppID(int appID) throws E5Exception;

    /**
     * 
     * @param appID
     * @param xmlFileName
     * @throws E5Exception
     */
    public String saveTemplateXMLByAppID(int appID, String xmlFileName)
            throws E5Exception;

    /**
     * 
     * @param docTypeID
     * @throws E5Exception
     */
    public String saveTemplateXMLByDocTypeID(int docTypeID) throws E5Exception;

    /**
     * 
     * @param docTypeID
     * @param xmlFileName
     * @throws E5Exception
     */
    public String saveTemplateXMLByDocTypeID(int docTypeID, String xmlFileName)
            throws E5Exception;

    /**
     * @param typeID
     * 
     * @param doctypeField
     * @throws E5Exception
     */
    public void update(DocTypeField doctypeField) throws E5Exception;
    
    /**
     * �����ĵ����ͣ������ĵ����͹���ʱʹ��
     * 2006-1-20 11:11:23
     * @author zhang_kaifeng
     * @param docTypeID
     * @param relatedTypeIDs TODO
     * @throws E5Exception
     */
    public void updateTypeRelation(int docTypeID, String relatedTypeIDs) throws E5Exception;
    
    /**
     * �����ĵ����͵�ȱʡ����
     * 2006-2-10 14:52:17
     * @author zhang_kaifeng
     * @param docTypeID
     * @param flowID
     * @throws E5Exception
     */
    public void setDefaultFlow(int docTypeID,int flowID) throws E5Exception;

    /**
     * @param typesElement
     * @param type
     */
    public void exportDocType(Element typesElement, DocType type);
    
    public String exportDocType(int docTypeID) throws E5Exception;

}