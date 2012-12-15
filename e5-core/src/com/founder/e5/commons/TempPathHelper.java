package com.founder.e5.commons;
import java.io.*;
import javax.servlet.*;

/**
 * Created on 2004-4-6
 * @author dingning
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TempPathHelper {
	
	/**
	 * ĿǰӦ�ó����Ƿ�ΪwebApp
	 */
	private static boolean isWebApp = false;
	/**
	 * @return Returns the isWebApp.
	 */
	public static boolean isWebApp() {
		return isWebApp;
	}

	/**
	 * @param isWebApp The isWebApp to set.
	 */
	public static void setWebApp(boolean isWebApp) {
		TempPathHelper.isWebApp = isWebApp;
	}
	
	private static String tmpPath = null;
	
	/**
	 * ������ʱĿ¼
	 * @param tmpPath The tmpPath to set.
	 */
	public static void setTmpPath(String tmpPath) {
		TempPathHelper.tmpPath = tmpPath;
	}
	
	public static String getTmpPath()
	{				
		if(StringHelper.isEmpty(tmpPath))			
		{
			if(isWebApp && servlet!=null)
			{
				ServletContext context = servlet.getServletConfig().getServletContext();	
				try {
					tmpPath = (String) context.getAttribute("javax.servlet.context.tempdir");
				}
				catch (ClassCastException cce) {
					tmpPath = ((File) context.getAttribute("javax.servlet.context.tempdir")).getAbsolutePath();
				}
			}	
			else
			{
				tmpPath = System.getProperty("java.io.tmpdir");
			}	
		}	
		return tmpPath;
	}
	private static Servlet servlet = null;
	
	public static void setServlet(Servlet servlet)
	{
		TempPathHelper.servlet = servlet;
	}
	

}
