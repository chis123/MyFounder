package com.founder.e5.load;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.founder.e5.context.Context;

/**
 * ʵ��Spring��ApplicationContextAware�ӿ�
 * <BR>�����������Spring��context.xml��
 * <BR>��web container����ʱ��ȡSpring��ApplicationContext
 * <BR>Ȼ�󸳸�com.founder.e5.context.Context
 * 
 * @created 2006-02-13
 * @author Gong Lijie
 * @version 1.0
 */
public class ContextAware implements ApplicationContextAware {
	private ApplicationContext mycontext;
	
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		mycontext = arg0;
	}
	public void setToContext()
	{
		Context.setApplicationContext(mycontext);
	}
}
