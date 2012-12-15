package com.founder.e5.config;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ƽ̨�ĸ��Ի�������Ϣ
 * @created 2006-8-31
 * @author Gong Lijie
 * @version 1.0
 */
public class InfoCustomize
{
	private boolean customizeToolkit;	//�������Ƿ�ɶ��Ʋ���˳��
	private String defaultButtonStyle; 	//ȱʡ�Ĳ�����ť��ʾ��ʽ
	private List items = new ArrayList(5); //���Ի����Ƶ���Ŀ
	
	/**
	 * ��ȡȱʡ�Ĺ�����������ť��ʾ��ʽ
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
     * ����һ�����������
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
