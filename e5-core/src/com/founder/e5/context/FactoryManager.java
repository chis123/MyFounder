package com.founder.e5.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoFactory;

/**
 * 工厂类的单例管理器
 * <Description>
 * 系统中有很多符合单例模式的工厂类，在这里统一管理
 * 首先需要在e5-config.xml中进行注册
	<factory-config>
		<bean id="DSManager"
				beanClass="com.founder.e5.context.DSManager"
				invokeClass="com.founder.e5.context.DSManagerImpl" 
				invokeMethod="getInstance"/>
	</factory-config>
 * 则系统会自动加载每个类
 * 
 * 如下方式调用:
 * FactoryManager.find(DSManager.class);
 * FactoryManager.find("com.founder.e5.context.DSManager");
 * FactoryManager.findByID("DSManager");
 * 
 * 注意：原来的find系列方法是非public的，调用时使用Context类的静态方法
 * getBean和getBeanByID。2006-2-13 Context的getBean系列改用Spring
 * 的Bean Factory进行管理，所以这里的FactoryManager单独开放接口
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
	 * 按类名取系统管理中的预存对象
	 * @param className
	 * @return
	 */
	public static Object find(String className){
		if (factoryByClass == null) return null;
		
		return factoryByClass.get(className);
	}

	/**
	 * 按类取系统管理中的预存对象
	 * @param clazz
	 * @return
	 */
	public static Object find(Class clazz){
		return find(clazz.getName());
	}
	
	/**
	 * 按ID取系统管理中的预存对象
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
		
		//初始化
		factoryByID = new Hashtable(factories.size());
		factoryByClass = new Hashtable(factories.size());
		
		for (int i = 0; i < factories.size(); i++)
		{
			InfoFactory f = (InfoFactory) factories.get(i);
			if (f == null) continue;

			//Factory对象实例化
			Class c = Class.forName(f.getInvokeClass());
			Object obj;
			//若没有指定调用方法，则表示使用new
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
