package com.founder.e5.load;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.founder.e5.commons.Log;
import com.founder.e5.config.ConfigReader;
import com.founder.e5.context.Context;

/**
 * ���������ļ����ص�Servlet
 * web.xml�У�Ӧ��������Ƭ��
 * <pre>
 * <servlet>
 *     <servlet-name>e5load</servlet-name>
 *     <servlet-class>com.founder.e5.load.Load</servlet-class>
 *     <init-param>
 *       <param-name>config</param-name>
 *       <param-value>/WEB-INF/e5/e5-config.xml</param-value>
 *     </init-param>
 *     <load-on-startup>1</load-on-startup>
 * </servlet>
 * </pre>
 * <BR>Created on 2005-8-11
 * @author Gong Lijie
 * @version 1.0
 */
public class Load extends HttpServlet
{
	private Log log = Context.getLog("e5.context");
    /**
	 * 
	 */
	private static final long serialVersionUID = -1731941068850842427L;
	public void init(ServletConfig servletConfig) throws ServletException
    {
		log.info("[E5Load]");
        
		super.init(servletConfig);

        //ȡ��e5-config.xml���ļ�·��
        String configFile = servletConfig.getInitParameter("config");
        
        //����ConfigReader��ȡ����������Ϣ
        ConfigReader reader = ConfigReader.getInstance();
		boolean gotConfigure = reader.getConfigure(configFile);
		
		log.info("[E5Load]Configuration file loaded.");
		//������Ϣ��ȡ����������ʼ�ĸ�ֵ�Լ��Զ����ع���
		try
		{
			if (gotConfigure){
				LocalLoad local = new LocalLoad();
				local.load(reader);
			}
			log.info("[E5Load]Initialization finished.");
		}
		catch (Exception e)
		{
			log.error("[E5Load]", e);
		}
    }
    //Clean up resources
    public void destroy()
    {
    }
}
