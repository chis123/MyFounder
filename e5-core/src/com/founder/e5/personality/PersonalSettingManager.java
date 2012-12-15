package com.founder.e5.personality;

import com.founder.e5.context.E5Exception;

/**
 * 个性化定制功能管理接口
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 15:33:07
 */
public interface PersonalSettingManager extends PersonalSettingReader {

	/**
	 * 设置某用户的资源树根列表<br>
	 * <br>
	 * 注意：设置之前检查传入的文件夹ID是否合法；合法的条件如下：<br>
	 * 1、存在对应的文件夹（检查缓存中相应的文件夹实体是否存在）<br>
	 * 2、用户角色对该文件夹有权限<br>
	 * 3、父结点尚未加入
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @param folderIDs 文件夹ID数组
	 * @throws E5Exception
	 */
	public void setResourceTreeRoot( int userID, int roleID, int[] folderIDs )
			throws E5Exception;

	/**
	 * 设置某用户的资源树根列表<br>
	 * <br>
	 * 注意：设置之前检查传入的文件夹ID是否合法；合法的条件如下：<br>
	 * 1、存在对应的文件夹（检查缓存中相应的文件夹实体是否存在）<br>
	 * 2、用户角色对该文件夹有权限<br>
	 * 3、父结点尚未加入
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @param folderIDs 用逗号串联的文件夹ID
	 * @throws E5Exception
	 */
	public void setResourceTreeRoot( int userID, int roleID, String folderIDs )
			throws E5Exception;

	/**
	 * 设置是否显示资源树根节点<br>
	 * <br>
	 * 该项设置影响工作平台资源树的展示方式；如果用户对某节点有权限，但对其所属根节点无权限，该项设为真时，显示根节点；
	 * 
	 * @param userID
	 * @param roleID
	 * @param enabled 是否允许
	 * @throws E5Exception
	 */
	public void setShowTreeRoot( int userID, int roleID, boolean enabled )
			throws E5Exception;

	/**
	 * 设置是否显示资源树父节点<br>
	 * <br>
	 * 该项设置影响工作平台资源树的展示方式；如果用户对某节点有权限，但对其父节点无权限，该项设为真时，显示其父节点；
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @param enabled 是否允许
	 * @throws E5Exception
	 */
	public void setShowTreeParent( int userID, int roleID, boolean enabled )
			throws E5Exception;

	/**
	 * 设置特定用户的图标和文字选项配置
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @param configValue 图标和文字选项配置
	 * @throws E5Exception
	 * @see ConfigItem#ONLY_TEXT
	 * @see ConfigItem#ONLY_ICON
	 * @see ConfigItem#LEFTICON_RIGHTTEXT
	 * @see ConfigItem#TOPICON_DOWNTEXT
	 */
	public void setIconAndTextOption( int userID, int roleID, int configValue )
			throws E5Exception;

	/**
	 * 设置用户选用的列表方式
	 * 
	 * @param userID
	 * @param roleID
	 * @param listIDs 以逗号连接的列表方式ID，其中包含所有文档类型下的列表方式，同一文档类型下列表方式ID的顺序有意义
	 * @throws E5Exception
	 */
	public void setListPages( int userID, int roleID, String listIDs )
			throws E5Exception;

	/**
	 * 设置用户对某个列表方式的配置
	 * 
	 * @param userID
	 * @param roleID
	 * @param listPageID
	 * @param listXml 排序字段的配置，格式同ListPage.listXml
	 * @param templateSlice 显示字段的配置，格式同ListPage.templateSlice
	 * @throws E5Exception
	 */
	public void setListPageCfg( int userID, int roleID, int listPageID,
			String listXml, String templateSlice ) throws E5Exception;

	/**
	 * 设置工具栏操作按钮定制信息
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @param docTypeID 文档类型ID，当flowNodeID=0时起作用
	 * @param fvID 文件夹ID。必需。为0时不进行设置。
	 * @param flowNodeID 流程节点ID。为0时表示只有非流程操作
	 * @param procIDs 操作ID序列，以逗号连接，其顺序有意义
	 * @throws E5Exception
	 */
	public void setToolbarCfg( int userID, int roleID, int docTypeID,
			int fvID, int flowNodeID, String procIDs ) throws E5Exception;

	/**
	 * 删除某用户在某角色下所有的个性化定制配置信息
	 * 
	 * @param userID
	 * @param roleID
	 * @throws E5Exception
	 */
	public void deleteAllSetting( int userID, int roleID ) throws E5Exception;

	/**
	 * 删除某用户所有的个性化定制配置信息
	 * 
	 * @param userID
	 * @throws E5Exception
	 */
	public void deleteAllSetting( int userID ) throws E5Exception;

}
