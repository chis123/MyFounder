package com.founder.e5.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoFactory;

/**
 * ������ĵ���������
 * <Description>
 * ϵͳ���кܶ���ϵ���ģʽ�Ĺ����࣬������ͳһ����
 * ������Ҫ��e5-config.xml�н���ע��
	<factory-config>
		<bean id="DSManager"
				beanClass="com.founder.e5.context.DSManager"
				invokeClass="com.founder.e5.context.DSManagerImpl" 
				invokeMethod="getInstance"/>
	</factory-config>
 * ��ϵͳ���Զ�����ÿ����
 * 
 * ���·�ʽ����:
 * FactoryManager.find(DSManager.class);
 * FactoryManager.find("com.founder.e5.context.DSManager");
 * FactoryManager.findByID("DSManager");
 * 
 * ע�⣺ԭ����findϵ�з����Ƿ�public�ģ�����ʱʹ��Context��ľ�̬����
 * getBean��getBeanByID��2006-2-13 Context��getBeanϵ�и���Spring
 * ��Bean Factory���й������������FactoryManager�������Žӿ�
 * </Description>
 * @created on 2005-7-19
 * @author Gong Lijie
 * @version 1.0
 */
public class FactoryManager
{
	protected static Map factoryByID;
	protected static Map factoryByClass;

	/**
	 * ������ȡϵͳ�����е�Ԥ�����
	 * @param className
	 * @return
	 */
	public static Object find(String className){
		if (factoryByClass == null) return null;
		
		return factoryByClass.get(className);
	}

	/**
	 * ����ȡϵͳ�����е�Ԥ�����
	 * @param clazz
	 * @return
	 */
	public static Object find(Class clazz){
		return find(clazz.getName());
	}
	
	/**
	 * ��IDȡϵͳ�����е�Ԥ�����
	 * @param id
	 * @return
	 */
	public static Object findByID(String id){
		if (factoryByID == null) return null;
		
		return factoryByID.get(id);
	}
	
	public synchronized static void init() throws Exception {
		if (factoryByClass != null) return;
		
		ConfigReader reader = ConfigReader.getInstance();

		List factories = reader.getFactories();
		if ((factories == null) || (factories.size() == 0)) return;
		
		//��ʼ��
		factoryByID = new Hashtable(factories.size());
		factoryByClass = new Hashtable(factories.size());
		
		for (int i = 0; i < factories.size(); i++)
		{
			InfoFactory f = (InfoFactory) factories.get(i);
			if (f == null) continue;

			//Factory����ʵ����
			Class c = Class.forName(f.getInvokeClass());
			Object obj;
			//��û��ָ�����÷��������ʾʹ��new
			if (f.getInvokeMethod() == null || f.getInvokeMethod().length() == 0)
				obj = c.newInstance();
			else
			{
	            Method method = c.getMethod(f.getInvokeMethod(), null);
	            int mod = method.getModifiers();
	            if (!Modifier.isPublic(mod))
	            	method.setAccessible(true);
	            if (Modifier.isStatic(mod))
	                obj = method.invoke(null, null);
	            else
	                obj = method.invoke(c.newInstance(), null);
			}
			
			factoryByID.put(f.getId(), obj);
			factoryByClass.put(f.getBeanClass(), obj);
		}
	}
	/**
	 * @test
	 */
	public static void show() 
	{
		System.out.println("factoryByClass:" + factoryByClass);
		System.out.println("factoryByID:" + factoryByID);
	}
}
