package com.founder.e5.flow;
import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @deprecated 2006-5-17
 * 已经被合并在FlowCache中
 * 
 * @created 04-8-2005 15:32:14
 * @author Gong Lijie
 * @version 1.0
 */
public class ProcCache implements Cache 
{
	Icon[] iconArr = null;
	Operation[] opArr = null;
	ProcFlow[] jumpArr = null;
	ProcUnflow[] unflowArr = null;
	/**
	 * 缓存类主要用在Reader实现类中<br>
	 * 在引用时，注意不要使用构造方法创建缓存类的实例<br>
	 * 请使用如下形式：<br>
	 * <pre>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * </pre>
	 * 其中XXXCache代表具体的缓存类
	 */
	public ProcCache()
	{
	}
	/**
	 * 缓存刷新
	 */
	public void refresh() throws E5Exception{
		ProcManager manager = FlowHelper.getProcManager();
		
		jumpArr = manager.getJumps(0);
		unflowArr = manager.getUnflows(0);
		opArr = manager.getOperations(0);
		iconArr = manager.getIcons();
	}

	/**
	 * 缓存重置
	 */
	public void reset(){

	}

	ProcFlow[] getJumps() {
		return jumpArr;
	}

	Operation[] getOperations() {
		return opArr;
	}

	Icon[] getIcons() {
		return iconArr;
	}

	ProcUnflow[] getUnflows() {
		return unflowArr;
	}
}