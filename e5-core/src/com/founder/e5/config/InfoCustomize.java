package com.founder.e5.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作平台的个性化设置信息
 * @created 2006-8-31
 * @author Gong Lijie
 * @version 1.0
 */
public class InfoCustomize
{
	private boolean customizeToolkit;	//工具栏是否可定制操作顺序
	private String defaultButtonStyle; 	//缺省的操作按钮显示方式
	private List items = new ArrayList(5); //个性化定制的项目
	
	/**
	 * 读取缺省的工具栏操作按钮显示方式
	 * @return
	 */
	public String getDefaultButtonStyle()
	{
		return defaultButtonStyle;
	}

	public void setDefaultButtonStyle(String defaultButtonStyle)
	{
		this.defaultButtonStyle = defaultButtonStyle;
	}

	/**
	 * @return Returns the autoRefresh.
	 */
	public boolean isCustomizeToolkit()
	{
		return customizeToolkit;
	}
	
	/**
	 * @param autoRefresh The autoRefresh to set.
	 */
	public void setCustomizeToolkit(String info)
	{
		if ("true".equals(info))
			this.customizeToolkit = true;
		else
			this.customizeToolkit = false;
	}

	/**
     * 增加一个缓存加载项
     * @param info CacheInfo
     */
    public void addInfo(InfoCustomizeItem info)
    {
    	items.add(info);
    }
    
	/**
	 * @return Returns the map.
	 */
	public InfoCustomizeItem[] getItems()
	{
		return (InfoCustomizeItem[])items.toArray(new InfoCustomizeItem[0]);
	}
	public String toString(){
		StringBuffer sbInfo = new StringBuffer(400);
		sbInfo.append("[customizeToolkit:").append(customizeToolkit);
		sbInfo.append("\n DefaultButtonStyle:").append(defaultButtonStyle);
		InfoCustomizeItem[] itemArr = getItems();
		if (items != null)
			for (int i = 0; i < itemArr.length; i++)
				sbInfo.append("\n").append(itemArr[i].toString());
		sbInfo.append("\n]");
		return sbInfo.toString();
	}

}
