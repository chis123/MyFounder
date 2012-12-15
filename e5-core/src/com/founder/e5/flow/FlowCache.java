package com.founder.e5.flow;
import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * 流程缓存
 * @created 04-8-2005 14:15:59
 * @author Gong Lijie
 * @version 1.0
 */
public class FlowCache implements Cache 
{
	private Flow[] flowArr = null;
	private FlowNode[] nodeArr = null;
	private ProcOrder[] orders = null;
	private Icon[] iconArr = null;
	private Operation[] opArr = null;
	private ProcFlow[] procArr = null;
	private ProcUnflow[] unflowArr = null;
	private ProcGroup[] groups = null; //操作组 2008-05-15 Gong Lijie

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
	public FlowCache()
	{
		
	}
	/**
	 * 缓存刷新
	 * @throws E5Exception
	 */
	public void refresh() throws E5Exception {
		FlowManager manager = FlowHelper.getFlowManager();
		flowArr = manager.getFlows(0);
		nodeArr = manager.getFlowNodes(0);		
		orders = FlowHelper.getProcOrderManager().getAll();
		
		ProcManager procManager = FlowHelper.getProcManager();
		
		procArr = procManager.getProcs(0);
		unflowArr = procManager.getUnflows(0);
		opArr = procManager.getOperations(0);
		iconArr = procManager.getIcons();
		
		try {
			ProcGroupManager groupManager = FlowHelper.getProcGroupManager();
			groups = groupManager.getAllGroups();
		} catch (Exception e) {
		}
	}

	/**
	 * 缓存重置
	 */
	public void reset(){

	}
	
	Flow[] getFlows()
	{
		return flowArr;
	}
	
	FlowNode[] getNodes()
	{
		return nodeArr;
	}
	ProcOrder[] getOrders()
	{
		return orders;
	}
	ProcFlow[] getProcs() {
		return procArr;
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
	
	ProcGroup[] getGroups(){
		return groups;
	}
}