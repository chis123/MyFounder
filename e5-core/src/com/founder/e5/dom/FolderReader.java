package com.founder.e5.dom;

import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 2005-7-18 16:53:53
 * @author Zhang Kaifeng
 */
public interface FolderReader {
    
    /**
     * 根据文档库ID，获取其对应的文件夹对象
     * @param docLibID 文档库ID
     * @return 文件夹对象
     * @throws E5Exception
     */
    public Folder getRoot(int docLibID) throws E5Exception;

    /**
     * 获取指定id的FolderView对象
     * 
     * @param fvID 文件夹视图ID
     * @return FolderView 如果FolderView对象不存在，则返回null
     * @throws E5Exception
     */
    public FolderView get(int fvID) throws E5Exception;

    /**
     * 获取指定文档库下的所有文件夹和视图
     * 
     * @param docLibID 文档库ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public FolderView[] getChildrenFVsByDocLib(int docLibID) throws E5Exception;

    /**
     * 返回指定文件夹下的所有子文件夹和视图，按照一棵树的节点顺序排列了的
     * 
     * @param folderID 文件夹ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     * 
     */
    public FolderView[] getChildrenFVs(int folderID) throws E5Exception;

    /**
     * 返回指定文件夹下的所有子id，按照一棵树的节点顺序排列了的
     * 
     * @param folderID 文件夹ID
     * @return 如果文件夹下无子，则返回空数组
     * @throws E5Exception
     */
    public int[] getChildrenFVIDs(int folderID) throws E5Exception;

    /**
     * 获取指定文件夹上的所有规则，排了顺序的
     * 
     * @param folderID 文件夹ID
     * @return 如果文件夹上无规则，则返回空数组
     * @throws E5Exception
     */
    public Rule[] getRules(int folderID) throws E5Exception;

    /**
     * 获取指定文件夹上的所有规则id，排了顺序的
     * 
     * @param folderID 文件夹ID
     * @return 如果无规则，则返回空数组
     * @throws E5Exception
     */
    public int[] getRuleIDs(int folderID) throws E5Exception;

    /**
     * 获取指定文件夹上的所有过滤器，过滤器最多三组，存储在List中，每个元素是个Filter的数组，排了顺序的
     * 
     * @param folderID 文件夹ID
     * @return 如果文件夹上无过滤器，则返回空列表
     * @throws E5Exception
     */
    public List getFilters(int folderID) throws E5Exception;

    /**
     * 获取指定文件夹上的所有过滤器id，过滤器最多三组，存储在List中，每个元素是个int数组，排了顺序的
     * 
     * @param folderID 文件夹ID
     * @return 如果文件夹上无过滤器，则返回空数组
     * @throws E5Exception
     */
    public List getFilterIDs(int folderID) throws E5Exception;
    
    /**
     * 获取指定文件夹的儿子节点（文件夹）
     * 
     * @param folderID 文件夹
     * @return 如果文件夹下无子，则返回空数组
     * @throws E5Exception
     *             如果文件夹不存在，则抛出异常
     */
    public Folder[] getSubFolders(int folderID) throws E5Exception;

    /**
     * 获取指定文件夹的儿子节点的id数组 2006-1-16 16:35:04
     * 
     * @param folderID 指定的文件夹id
     * @return 如果文件夹下无子，则返回空数组
     * @throws E5Exception
     */
    public int[] getSubFoldersID(int folderID) throws E5Exception;
    
    /**
     * 获取指定文件夹的儿子节点的数组（包括视图）
     * @param folderID 文件夹ID
     * @return 如果无子，则返回空数组
     * @throws E5Exception
     */
    public FolderView[] getSubFVs(int folderID) throws E5Exception;
    
    /**
     * 获取指定id的FolderView对象数组
     * @param fvIDs 如果为null，则返回空数组
     * @return 如果某个id没有对应的FV，则返回空数组
     * @throws E5Exception
     */
    public FolderView[] getFVs(int[] fvIDs) throws E5Exception;

}