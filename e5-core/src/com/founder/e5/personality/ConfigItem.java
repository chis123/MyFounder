package com.founder.e5.personality;

/**
 * 该类保存预定义配置项常量名
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 15:22:38
 */
public class ConfigItem {

	/**
	 * 资源树根
	 */
	public static final String RESOURCETREEROOT = "ResourceTreeRoot";

	/**
	 * 工具栏上的图标和文字选项
	 */
	public static final String ICON_TEXT_OPTION = "IconTextOption";

	/**
	 * 这个配置项用于存储用户对特定列表方式的配置信息
	 */
	public static final String LISTPAGE_CFG = "ListPage_Cfg";

	/**
	 * 这个配置项用于存储用户选择的列表方式及其顺序信息
	 */
	public static final String LISTPAGE_CHOSEN = "ListPage_Chosen";

	/**
	 * 工具栏操作按钮的定制信息
	 */
	public static final String TOOLBAR = "Toolbar";

	// --------------------------------------------- 图标和文字选项配置常量

	/**
	 * 只有文字
	 */
	public static final int ONLY_TEXT = 1;

	/**
	 * 只有图标
	 */
	public static final int ONLY_ICON = 2;

	/**
	 * 图标在左，文字在右
	 */
	public static final int LEFTICON_RIGHTTEXT = 3;

	/**
	 * 图标在上，文字在下
	 */
	public static final int TOPICON_DOWNTEXT = 4;

}
