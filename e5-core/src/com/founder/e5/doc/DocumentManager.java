/*
 * Created on 2005-6-21 16:55:07
 *
 */
package com.founder.e5.doc;

import com.founder.e5.context.E5Exception;

/**
 * 文档实体的管理接口，负责文档实体表的增删改查。
 * <br>依赖e5context
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-21 16:55:07
 */
public interface DocumentManager {
    
    /**
     * 指定文档库ID，创建空Document实例。
     * 
     * 步骤：
     * 1、根据文档库ID获得文档类型ID
     * 2、根据文档类型ID获得文档类型字段
     * 3、new 一个Document，设置文档库ID、文档类型ID、文档类型字段
     * 4、生成文档ID并赋值
     * 5、返回Document实例
     * 
     * @param docLibID 文档库ID
     * @return 空Document实例
     * @throws E5Exception
     */
    public Document newDocument( int docLibID ) throws E5Exception;

    /**
     * 与newDocument(int)相同，除了Document的ID由参数传入。
     * @param docLibID
     * @param docID
     * @return
     * @throws E5Exception
     */
    public Document newDocument( int docLibID, long docID ) throws E5Exception;
    
	/**
	 * 拷贝给定文档的除文档库ID外的所有属性（包括自定义属性），创建Document实例。
	 * 
	 * @param refDoc 参考文档实例
	 * @param docLibID 新文档所属文档库的ID
	 * @return Document实例
	 * @throws E5Exception
	 */
	public Document newDocument( Document refDoc, int docLibID )
			throws E5Exception;
    
	/**
	 * 拷贝给定文档的除文档库ID外的所有属性（包括自定义属性），创建Document实例。
     * 与newDocument(Document, int)相同，除了Document的ID由参数传入。
	 * 
	 * @param refDoc 参考文档实例
	 * @param docLibID 新文档所属文档库的ID
	 * @return Document实例
	 * @throws E5Exception
	 */
	public Document newDocument( Document refDoc, int docLibID, long docID )
			throws E5Exception;

	/**
     * 根据id载入文档实体
     * 
     * 从数据库中取出来的Document的状态为持久态（status=STATUS_PERSISTENT）;
     * 用户调用处于持久态的Document的set(...)方法更新其属性时，Document实例维护一个更新标记
     * 同时记录更新过的字段，以后调用DocumentManager.save(Document)时只更新那些实际更新
     * 过的字段（之后复位更新标记和更新字段信息）
     * 相当于get(docLibID, docID, true)
     * 
     * @param docLibID 文档库ID
     * @param docID 文档ID
     * @return 文档实例
     * @throws E5Exception
     */
    public Document get(int docLibID, long docID) throws E5Exception;
    
    /**
     * 根据id载入文档实体
     * 
     * 从数据库中取出来的Document的状态为持久态（status=STATUS_PERSISTENT）;
     * 用户调用处于持久态的Document的set(...)方法更新其属性时，Document实例维护一个更新标记
     * 同时记录更新过的字段，以后调用DocumentManager.save(Document)时只更新那些实际更新
     * 过的字段（之后复位更新标记和更新字段信息）
     * 
     * @param doclibID 文档库ID
     * @param docID 文档ID
     * @param lazyLoadLob 是否延迟加载Lob类型字段
     * @return 文档实例
     * @throws E5Exception
     */
    public Document get(int docLibID, long docID, boolean lazyLoadLob)
            throws E5Exception;
    
    /**
	 * 根据多个id载入多个文档实体<br>
	 * 便利方法
	 * 
	 * @see #get(int, long)
	 * @param docLibID 文档库ID
	 * @param docIDs 文档ID
	 * @return 文档实例数组
	 * @throws E5Exception
	 */
    public Document[] get( int docLibID, long[] docIDs ) throws E5Exception;
    
    /**
	 * 根据id载入文档实体，但只取指定的字段
	 * 
	 * @param docLibID 文档库ID
     * @param docID 文档库ID
     * @param columns 用户请求字段数组
     * @param lazyLoadLob 是否延迟加载Lob类型字段
	 * @return 文档实例（注意：其中只包含请求字段的值）
	 * @throws E5Exception
	 */
	public Document get( int docLibID, long docID, String[] columns, boolean lazyLoadLob )
			throws E5Exception;
    
    /**
	 * 根据id载入文档实体，但只取指定的字段<br>
	 * 注意：此方法相当于get(int, long, String[], true)
	 * 
	 * @see #get(int, long, String[], boolean)
	 * @param docLibID 文档库ID
     * @param docID 文档库ID
     * @param columns 用户请求字段数组
	 * @return 文档实例（注意：其中只包含请求字段的值）
	 * @throws E5Exception
	 */
	public Document get( int docLibID, long docID, String[] columns )
			throws E5Exception;
    
    /**
	 * 根据多个id载入多个文档实体，但只取指定的字段
	 * 
	 * @param docLibID 文档库ID
	 * @param docIDs 文档库ID
	 * @param columns 用户请求字段数组
	 * @return 文档实例数组（注意：其中只包含请求字段的值）
	 * @throws E5Exception
	 */
    public Document[] get( int docLibID, long[] docIDs, String[] columns )
			throws E5Exception;
    
    /**
     * 保存文档实体。<br>
     * 若为新文档（临时态），则在库中插入一条记录；若为已有文档（持久态），则更新库中记录<br>
     * <br>
     * 步骤：<br>
     * 1、判断Document.getStatus()，若为临时态（STATUS_TRANSIENT），则插入新记录；
     * 若为持久态（STATUS_PERSISTENT），则更新已有记录<br>
     * 2、更新已有记录时，若Document.isDirty()为假则直接返回，否则从Document.getDirtyColumns()
     * 中取被修改过的表字段，更新这些字段；然后清空Document.dirtyColumns，设置Document.dirty=false<br>
     * <br>
     * 实际更新文档时还要考虑关联文档、关联流程记录的数据一致性，做事务处理
     * 
     * @param doc 待保存文档实例
     * @throws E5Exception
     */
    public void save(Document doc) throws E5Exception;
    
    
    /**
     * 移动指定文档到另一文档库。<br>
     * 文档移库时文档ID和库ID发生变动，其余属性都保留用户传入时文档的状态
     * <br>
     * <br>
     * 文档必须处于持久态；移动过程同时保存文档当前状态；
     * 同时要移动关联的流程记录到目标文档库，并更新关联文档信息；<br>
     * 注意：文档移库后ID将发生变化!流程记录的ID也发生变化，关联稿件表中原稿ID会被替换为新稿ID
     * 
     * @param doc 文档实例
     * @param newDocLibID 目标文档库ID
     * @throws E5Exception
     * @return 新文档
     */
    public Document moveTo(Document doc, int newDocLibID) throws E5Exception;
    
    /**
     * 移动指定文档到另一文档库。<br>
     * <br>
     * <br>
     * 请参考moveTo(Document, int)方法
     * @param doc
     * @param newDocLibID
     * @param docID	指定ID
     * @return
     * @throws E5Exception
     */
    public Document moveTo(Document doc, int newDocLibID, long newDocID) throws E5Exception;
    /**
     * 从文档库删除文档记录（物理删除）
     * 
     * 删除文档时要同时删除文档相关的流程记录并更新关联信息，这是一个事务！
     * 
     * @param doc 待删文档
     * @throws E5Exception
     */
    public void delete(Document doc) throws E5Exception;

    
    /**
     * 根据ID删除表记录（物理删除）
     * 
     * 删除文档时要同时删除文档相关的流程记录并更新关联信息，这是一个事务！
     * 
     * @param docLibID 文档库ID
     * @param docID 文档ID
     * @throws E5Exception
     */
    public void delete(int docLibID, long docID) throws E5Exception;

}
