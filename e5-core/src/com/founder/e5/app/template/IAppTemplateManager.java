package com.founder.e5.app.template;

import org.dom4j.Document;

import com.founder.e5.context.E5Exception;

/**
 * ��ϵͳģ�����
 * @author Qi Ming
 * @created 2005-7-11 09:47:41
 * @version 1.0
 */
public interface IAppTemplateManager {

	/**
	 * ���ݸ�����templateFile����װ����Ӧ����ϵͳ��Ϣ
	 * @param appID    ��ϵͳID
	 * @param templateFile    ��Ҫװ�ص�ģ���ļ�
	 * 
	 */
	public int load(int appID, String templateFile) throws E5Exception;
	
	/**
	 * ���ݸ�����DOM����װ����Ӧ����ϵͳ��Ϣ
	 * @param appID ��ϵͳID
	 * @param doc	DOM����
	 * @return
	 * @throws E5Exception
	 */
	public int load(int appID, Document doc) throws E5Exception;

	/**
	 * ж��appIDָ������ϵͳ
	 * @param appID    ��ϵͳID
	 * 
	 */
	public void unload(int appID) throws E5Exception;

	/**
	 * ����appIDָ������ϵͳ
	 * @param appID    ��ϵͳID
	 * 
	 */
	public String exportTemplate(int appID) throws E5Exception;

}