package com.founder.e5.flow;
import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @deprecated 2006-5-17
 * �Ѿ����ϲ���FlowCache��
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
	 * ��������Ҫ����Readerʵ������<br>
	 * ������ʱ��ע�ⲻҪʹ�ù��췽�������������ʵ��<br>
	 * ��ʹ��������ʽ��<br>
	 * <pre>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * </pre>
	 * ����XXXCache�������Ļ�����
	 */
	public ProcCache()
	{
	}
	/**
	 * ����ˢ��
	 */
	public void refresh() throws E5Exception{
		ProcManager manager = FlowHelper.getProcManager();
		
		jumpArr = manager.getJumps(0);
		unflowArr = manager.getUnflows(0);
		opArr = manager.getOperations(0);
		iconArr = manager.getIcons();
	}

	/**
	 * ��������
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