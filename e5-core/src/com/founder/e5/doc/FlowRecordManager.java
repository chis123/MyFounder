/*
 * Created on 2005-6-21 17:01:39
 *
 */
package com.founder.e5.doc;

import com.founder.e5.context.E5Exception;

/**
 * 流程记录实体管理接口，提供对数据库中表的增删改查操作方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-21 17:01:39
 */
public interface FlowRecordManager {

	/**
	 * 在数据库中创建一条流程记录的数据记录
	 * 
	 * @param 流程记录所关联的文档
	 * @param FlowRecord实例(注意:不要有ID值,会被丢弃)
	 * @return FlowRecord实例（包含新生成的ID值）
	 * @throws E5Exception
	 * @created 2005-7-25 9:37:14
	 */
	public FlowRecord createFlowRecord( Document doc, FlowRecord fr )
			throws E5Exception;

	/**
	 * 在数据库中创建一条流程记录的数据记录
	 * 
	 * @param 流程记录所关联的文档的库ID
	 * @param 流程记录所关联的文档的ID
	 * @param FlowRecord实例(注意:不要有ID值,会被丢弃)
	 * @return FlowRecord实例（包含新生成的ID值）
	 * @throws E5Exception
	 * @created 2005-7-18 10:06:56
	 */
	public FlowRecord createFlowRecord( int docLibID, long docID, FlowRecord fr )
			throws E5Exception;

	/**
	 * 根据文档库ID、流程记录ID取流程记录实例
	 * 
	 * @param docLibID 文档库ID
	 * @param frID 流程记录ID
	 * @return FlowRecord实例
	 * @throws E5Exception
	 */
	public FlowRecord get( int docLibID, long frID ) throws E5Exception;

	/**
	 * 取得关联到指定文档的所有流程记录的ID<br>
	 * <br>
	 * 流程记录ID的集合逆序排序
	 * 
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @return 流程记录ID数组
	 * @throws E5Exception
	 * @created 2005-7-24 14:26:38
	 */
	public long[] getAssociatedFRIDs( int docLibID, long docID )
			throws E5Exception;

	/**
	 * 取得关联到指定文档的所有流程记录<br>
	 * <br>
	 * 流程记录集合按ID倒序排序
	 * 
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @return 流程记录数组
	 * @throws E5Exception
	 * @created 2005-7-20 10:10:26
	 */
	public FlowRecord[] getAssociatedFRs( int docLibID, long docID )
			throws E5Exception;

	/**
	 * 取得关联到指定文档的所有流程记录<br>
	 * <br>
	 * 流程记录集合按指定的顺序进行排序
	 * 
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @param asc 排序参数 true表示按升序排列 false表示倒序排列
	 * @return 流程记录数组
	 * @throws E5Exception
	 */
	public FlowRecord[] getAssociatedFRs( int docLibID, long docID, boolean asc )
			throws E5Exception;
	/**
	 * 从数据库中删除与给定文档相关联的所有流程记录<br>
	 * 
	 * @param docLibID 文档库ID
	 * @param docID 文档ID
	 * @return 已删除的流程记录条数
	 * @throws E5Exception
	 */
	public int deleteAssociatedFRs( int docLibID, long docID )
			throws E5Exception;

	/**
	 * 根据文档库ID、流程记录ID删除流程记录
	 * 
	 * @param docLibID 文档库ID
	 * @param frID 流程记录ID
	 * @throws E5Exception
	 */
	public void delete( int docLibID, long frID ) throws E5Exception;

	/**
	 * 给定一个文档,移动相关的流程记录到新文档库
	 * 
	 * @param docLibID 目前所在的文档库ID
	 * @param docID 文档ID
	 * @param newDocLibID 新文档库ID
	 * @return 相关流程记录的条数
	 * @throws E5Exception
	 * @created 2005-7-20 9:24:49
	 */
	public int moveAssociatedFRs( int docLibID, long docID, int newDocLibID )
			throws E5Exception;

}
