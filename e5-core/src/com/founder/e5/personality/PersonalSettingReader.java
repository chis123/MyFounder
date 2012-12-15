package com.founder.e5.personality;

import com.founder.e5.context.E5Exception;

/**
 * 个性化定制功能用户接口。<br>
 * <br>
 * 注意：该接口所有返回数组类型的方法，若返回null则表示相应的记录不存在
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 15:28:46
 */
public interface PersonalSettingReader {

	/**
	 * 根据用户ID、角色ID取得其定制的资源树根（文件夹ID）列表<br>
	 * 注意：若数据库中没有这样的记录，则返回null<br>
	 * 从数据库中取出之后过滤掉不合法的文件夹ID后返回
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @return 文件夹ID数组（数据库中无相应记录时返回null）
	 * @throws E5Exception
	 */
	public int[] getResourceTreeRoot( int userID, int roleID )
			throws E5Exception;

	/**
	 * 是否显示资源树根节点？<br>
	 * <br>
	 * 该项设置影响工作平台资源树的展示方式；如果用户对某节点有权限，但对其所属根节点无权限，该项设为真时，显示根节点；否则不显示
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @return
	 * @throws E5Exception
	 */
	public boolean showTreeRoot( int userID, int roleID ) throws E5Exception;

	/**
	 * 是否显示资源树父节点？<br>
	 * <br>
	 * 此项设置影响工作平台资源树的展示方式；如果用户对某节点有权限，但对其父节点无权限，该项设为真时，显示其父节点；否则不显示
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @return
	 * @throws E5Exception
	 */
	public boolean showTreeParent( int userID, int roleID ) throws E5Exception;

	/**
	 * 取得特定用户的图标和文字选项配置
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @return 图标和文字选项配置
	 * @throws E5Exception
	 * @see ConfigItem#ONLY_TEXT
	 * @see ConfigItem#ONLY_ICON
	 * @see ConfigItem#LEFTICON_RIGHTTEXT
	 * @see ConfigItem#TOPICON_DOWNTEXT
	 */
	public int getIconAndTextOption( int userID, int roleID )
			throws E5Exception;

	/**
	 * 针对某种文档类型，获取用户选用的列表方式。<br>
	 * <br>
	 * 注意：如果文档类型ID=0，则返回所有列表方式
	 * 
	 * @param userID
	 * @param roleID
	 * @param docTypeID 文档类型ID，若为0，则返回所有列表方式
	 * @return 列表方式ID数组，顺序代表了它们在界面上显示的顺序（数据库中无相应记录时返回null）
	 * @throws E5Exception
	 */
	public int[] getListPages( int userID, int roleID, int docTypeID )
			throws E5Exception;

	/**
	 * 获取某种列表方式的配置信息。返回值为xml格式<br>
	 * <br>
	 * 注意：若返回值为null，表示数据库中没有针对给定列表方式的配置信息
	 * 
	 * @param userID
	 * @param roleID
	 * @param listPageID 列表方式ID
	 * @return String[2]: 0-listXml 1-templateSlice
	 *         参考ListPage的listXml、templateSlice属性含义（数据库中无相应记录时返回null）
	 * @throws E5Exception
	 */
	public String[] getListPageCfg( int userID, int roleID, int listPageID )
			throws E5Exception;

	/**
	 * 获取工具栏操作按钮的定制信息。<br>
	 * <br>
	 * 当前支持的操作由文档类型和流程节点决定 <br>
	 * 注意：若返回null，表示数据库中不存在该定制信息
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 * @param docTypeID 文档类型ID，当flowNodeID=0时起作用
	 * @param fvID 文件夹ID。必需。为0时不进行设置。
	 * @param flowNodeID 流程节点ID。为0时表示只有非流程操作
	 * @return 操作ID数组，其顺序代表了操作按钮的展示顺序（数据库中无相应记录时返回null）
	 * @see com.founder.e5.flow.Proc
	 * @throws E5Exception
	 */
	public int[] getToolbarCfg( int userID, int roleID, int docTypeID, int fvID,
			int flowNodeID ) throws E5Exception;

}
