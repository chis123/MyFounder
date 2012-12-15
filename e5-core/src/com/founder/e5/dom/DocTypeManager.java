package com.founder.e5.dom;

import org.dom4j.Element;

import com.founder.e5.context.E5Exception;

/**
 * @created 05-七月-2005 9:02:26
 * @author kaifeng zhang
 * @version 1.0
 */
public interface DocTypeManager extends DocTypeReader {
        

    /**
     * 创建文档类型
     * 
     * @param docType1，事先造好的临时态docType对象，其ID在create方法中赋值。
     * @throws E5Exception
     *             如果已经存在相同name的docType，或在生成docType对象时发生异常，则抛出E5Exception
     */
    public int create(DocType docType) throws E5Exception;
    
    /**
     * 创建文档类型与子系统的关联
     * 2006-4-7 10:36:41 by Zhang Kaifeng
     * @param docTypeApps
     * @throws E5Exception
     */
    public void create(DocTypeApps docTypeApps) throws E5Exception;
    
    /**
     * 创建文档类型
     * 2006-3-13 14:51:55
     * @author zhang_kaifeng
     * @param docTypeName
     * @param appID
     * @return 文档类型ID
     * @throws E5Exception
     */
    public int create(String docTypeName,int appID) throws E5Exception;

    /**
     * 创建文档类型字段。
     * 
     * 当新增了一个字段时，对于其涉及到的每一个文档库（根据文档类型确定），
     * 
     * 增加一条doctypefieldApps记录，isupdated，isadded都置为0。
     * 
     * 更新文档库的依据则是这两个字段都是0的情况；
     * 
     * 更新对应的文档库后，idadded变为1，idupdated不变
     * 
     * @param doctypeField
     * @throws E5Exception
     */
    public int create(DocTypeField doctypeField) throws E5Exception;

    /**
     * 根据ID删除一个文档类型。卸载子系统时使用。日常管理操作时不做删除文档类型的操作。
     * 
     * @param doctypeID
     * @throws E5Exception
     */
    public void delete(int doctypeID) throws E5Exception;
    
    /**
     * 删除一个文档类型跟某子系统的关联
     * 2006-4-7 10:37:31 by Zhang Kaifeng
     * @param appID
     * @param docTypeID
     * @throws E5Exception
     */
    public void delete(int appID,int docTypeID)throws E5Exception;

    /**
     * 删除一个字段
     * 
     * 当删除一个字段时，在fieldapps中找到对应字段记录，如果没有则新插入。
     * 
     * 置isupdated为－1
     * 
     * @param fieldID
     * @throws E5Exception
     */
    public void deleteField(int fieldID) throws E5Exception;

    /**
     * 读取docTypeFieldApps表，获取需要更新的docLib的id
     * 现在根据DocTypeFieldApps的isDBAdded和isDBUpdated属性来判断，是0的
     * @param docTypeID 
     * 
     * @return 需要更新的文档库数组，如果没有则返回空数组
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
     * 更新文档类型，创建文档类型关联时使用
     * 2006-1-20 11:11:23
     * @author zhang_kaifeng
     * @param docTypeID
     * @param relatedTypeIDs TODO
     * @throws E5Exception
     */
    public void updateTypeRelation(int docTypeID, String relatedTypeIDs) throws E5Exception;
    
    /**
     * 设置文档类型的缺省流程
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