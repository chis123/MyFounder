/*
 * Created on 2005-6-23 10:16:39
 * 
 */
package com.founder.e5.doc;

import com.founder.e5.context.E5Exception;

/**
 * DocAssociation实体表管理接口，负责表的增删改查。 <br>
 * 依赖e5context
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-23 10:16:39
 */
public interface DocAssociationManager {

	/**
	 * 创建关联关系
	 * 
	 * @param obj 包含关联稿件信息，不含记录ID
	 * @return 生成了记录ID后的对象
	 * @throws E5Exception
	 */
	public DocAssociation create( DocAssociation obj ) throws E5Exception;

	/**
	 * 删除关联关系
	 * 
	 * @param recordID 关联关系记录ID
	 * @throws E5Exception
	 */
	public void delete( long recordID ) throws E5Exception;

	/**
	 * 删除指定源稿的所有关联关系
	 * @param srcLibID
	 * @param srcID
	 * @throws E5Exception
	 */
	public void deleteBySrc( int srcLibID, long srcID ) throws E5Exception;

	/**
	 * 删除指定目的稿的所有关联关系
	 * @param destLibID
	 * @param destID
	 * @throws E5Exception
	 */
	public void deleteByDest( int destLibID, long destID ) throws E5Exception;

	/**
	 * 读取关联关系
	 * 
	 * @param recordID 关联关系记录ID
	 * @throws E5Exception
	 */
	public DocAssociation get( long recordID ) throws E5Exception;
	
	/**
	 * 根据源稿信息查询关联关系
	 * 
	 * @param srcLibID 库ID
	 * @param srcID 稿件ID
	 * @param associationCode 关联码
	 * @return
	 * @throws E5Exception
	 */
	public DocAssociation[] getBySrc( int srcLibID, long srcID,
			int associationCode ) throws E5Exception;

	/**
	 * 根据目的稿信息查询关联关系
	 * 
	 * @param destLibID 库ID
	 * @param destID 稿件ID
	 * @param associationCode 关联码
	 * @return
	 * @throws E5Exception
	 */
	public DocAssociation[] getByDest( int destLibID, long destID,
			int associationCode ) throws E5Exception;

	/**
	 * 根据根信息查询关联关系
	 * 
	 * @param rootLibID 库ID
	 * @param rootID 稿件ID
	 * @param associationCode 关联码
	 * @return
	 * @throws E5Exception
	 */
	public DocAssociation[] getByRoot( int rootLibID, long rootID,
			int associationCode ) throws E5Exception;

	// -------------------------------------------------------------------------

	/**
	 * 创建两条文档间的关联关系
	 * 
	 * @param srcLibID 源文档库ID
	 * @param srcID 源文档ID
	 * @param destLibID 目的文档库ID
	 * @param destID 目的文档ID
	 * @param associationCode 关联码
	 * @param order 目的文档序号
	 * @throws E5Exception
	 * @deprecated
	 */
	public void addAssociation( int srcLibID, long srcID, int destLibID,
			long destID, int associationCode, int order ) throws E5Exception;

	/**
	 * 创建两条文档间的关联关系
	 * 
	 * @param src 源文档
	 * @param dest 目的文档
	 * @param associationCode 关联码
	 * @param order 目的文档序号
	 * @throws E5Exception
	 * @deprecated
	 */
	public void addAssociation( Document src, Document dest,
			int associationCode, int order ) throws E5Exception;

	/**
	 * 删除关联关系
	 * 
	 * @param srcLibID
	 * @param srcID
	 * @param destLibID
	 * @param destID
	 * @param associationCode
	 * @return
	 * @deprecated
	 */
	public DocAssociation delAssociation( int srcLibID, long srcID,
			int destLibID, long destID, int associationCode );

	/**
	 * 取得一个稿件所有的关联关系
	 * 
	 * @param srcLibID
	 * @param srcID
	 * @param associationCode
	 * @return
	 * @throws E5Exception
	 * @deprecated
	 */
	public DocAssociation[] getAllAssociations( int srcLibID, long srcID,
			int associationCode ) throws E5Exception;

	// -------------------------------------------------------------------------

	/**
	 * 在关联关系表中用新节点(文档)替换旧节点
	 * 
	 * @param oldDocLibID 旧文档库ID
	 * @param oldDocID 旧文档ID
	 * @param newDocLibID 新文档库ID
	 * @param newDocID 新文档ID
	 * @throws E5Exception
	 * @created 2005-7-25 10:12:18
	 */
	public void replaceNode( int oldDocLibID, long oldDocID, int newDocLibID,
			long newDocID ) throws E5Exception;

}
