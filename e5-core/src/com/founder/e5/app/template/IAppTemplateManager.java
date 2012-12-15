package com.founder.e5.app.template;

import org.dom4j.Document;

import com.founder.e5.context.E5Exception;

/**
 * 子系统模版加载
 * @author Qi Ming
 * @created 2005-7-11 09:47:41
 * @version 1.0
 */
public interface IAppTemplateManager {

	/**
	 * 根据给定的templateFile参数装载相应的子系统信息
	 * @param appID    子系统ID
	 * @param templateFile    需要装载的模板文件
	 * 
	 */
	public int load(int appID, String templateFile) throws E5Exception;
	
	/**
	 * 根据给定的DOM对象，装载相应的子系统信息
	 * @param appID 子系统ID
	 * @param doc	DOM对象
	 * @return
	 * @throws E5Exception
	 */
	public int load(int appID, Document doc) throws E5Exception;

	/**
	 * 卸载appID指定的子系统
	 * @param appID    子系统ID
	 * 
	 */
	public void unload(int appID) throws E5Exception;

	/**
	 * 导出appID指定的子系统
	 * @param appID    子系统ID
	 * 
	 */
	public String exportTemplate(int appID) throws E5Exception;

}