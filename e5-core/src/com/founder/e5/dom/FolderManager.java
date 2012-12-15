package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

import org.hibernate.Session;

/**
 * @version 1.0
 * @created 11-七月-2005 15:23:47
 */
public interface FolderManager extends FolderReader {

    /**
     * 创建一个文件夹
     * 
     * @param folder
     *            必须是新new出来的对象，除了folderID，其他属性必填
     * @throws E5Exception
     */
    public int create(Folder folder,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;

    /**
     * 创建一个文件夹，用在如创建文档库的场景下使用
     * 
     * @param session
     * @param folder
     * @throws E5Exception
     */
    public void create(Folder folder,Session session) throws E5Exception;

    /**
     * 删除某一文件夹，如果文件夹不存在，则什么也不做。 该方法会删除该文件夹及所有的子孙文件夹，包括文件夹上的规则和过滤器
     * 
     * @param folderID
     * @throws E5Exception
     */
    public void delete(int folderID) throws E5Exception;

    /**
     * 更新文件夹     * 
     * 2006-1-17 10:01:02
     * @author zhang_kaifeng
     * @param folder 是更新了名字的folder
     * @param fvRules 新赋予的规则
     * @param fvFilters 新赋予的过滤器
     * @throws E5Exception
     */
    public void update(Folder folder,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;

    /**
     * 由已经存在的session来删除folder记录，方法内不关闭session
     * 
     * @param docLibID
     * @param ss
     * @throws E5Exception
     */
    void delete(int folderID, Session ss) throws E5Exception;

  
    
    /**
     * 文件夹树上拖拽文件夹时，改变它的父节点
     * 2006-1-17 12:26:10
     * @author zhang_kaifeng
     * @param folder
     * @throws E5Exception
     */
    public void drag(Folder folder) throws E5Exception;
    
    /**
     * 更新文档库名称时，同步更新其作为更文件夹的名称
     * 2006-3-9 10:27:34
     * @author zhang_kaifeng
     * @param docLibID
     * @param newName
     * @throws E5Exception
     */
    public void updateFolderName(int docLibID, String newName) throws E5Exception;
    
    /**
     * 重新排列指定父文件夹下的所有儿子文件夹视图
     * 2006-3-17 16:12:12
     * @author zhang_kaifeng
     * @param parentID
     * @throws E5Exception
     */
    public void reArrangeSubFVs(int parentID) throws E5Exception;
    
    /**
     * 按照已经排好的顺序，给某文件夹下所有子结点排序
     * 2006-4-12 13:59:04 by Zhang Kaifeng
     * @param parentID
     * @param fvids
     * @throws E5Exception
     */
    public void reArrangeSubFVs(int[] fvids) throws E5Exception;

}