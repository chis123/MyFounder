package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.CacheReader;
import com.founder.e5.context.Context;
import com.founder.e5.flow.ProcManager;
import com.founder.e5.flow.ProcReader;

/**
 * @created on 2005-7-18
 * @author Gong Lijie
 * @version 1.0
 */
public class PermissionHelper
{
	/**
	 * ��Ȩ�ޱ������ �� �� �����ϳ�һ��Ȩ�ޱ�ǣ��Ա��Ƚ�
	 * @param maskArray Ȩ�ޱ�ǵ����飬��{1,2,4}��ʾ��0λ/1λ/2λ����Ȩ��
	 * @return
	 */
	static int getMask(int[] maskArray){
		int mask = 0;
		for (int i = 0; i < maskArray.length; i++)
			mask = mask | maskArray[i];
		return mask;
	}
	
	/**
	 * �ж��Ƿ�ӵ��ĳ����Ȩ��
	 * @param p int Ȩ��
	 * @param mask Ȩ��mask ע��mask=0ʱʼ��Ϊfalse
	 * @return
	 */
	static boolean hasPermission(int p, int mask){
		if ((mask > 0 ) && (p & mask) == mask)
			return true;
		else
			return false;
	}
	/**
	 * �ж��Ƿ�ӵ��ĳ����Ȩ��
	 * @param p Ȩ�޶���
	 * @param mask
	 * @return
	 */
	static boolean hasPermission(Permission p, int[] mask){
		return hasPermission(p.getPermission(), mask);
	}
	static boolean hasPermission(int p, int[] mask){
		for (int i = 0; i < mask.length; i++)
			if (hasPermission(p, mask[i])) return true;
		return false;
	}
	/**
	 * ��һ��Ȩ��������ȡ��ӵ��ĳЩȨ�޵Ľ�ɫ
	 * @param pArr
	 * @param maskArray
	 * @return
	 */
	static int[] getRoleByMask(Permission[] pArr, int[] maskArray){
		//�Ƚ�Ȩ�ޣ��õ����ʵĽ�ɫ
		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i], maskArray))
				list.add(new Integer(pArr[i].getRoleID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] roleArr = new int[size];
		for (int i = 0; i < size; i++)
			roleArr[i] = ((Integer)list.get(i)).intValue();
		return roleArr;
	}
	/**
	 * ��һ�������Ķ��������У��м�ĳһλȥ��
	 * ������32λ��
	 * @param num int ԭʼ����
	 * @param b int Ҫȥ����λ����0��ʼ
	 * @return int
	 */
	public static int getBitDelete(int num, int b)
	{
		int nInteger = 0x7FFFFFFF;
		int nIntegerBits = 31;

		int nMaskRight = nInteger >> (nIntegerBits - b); //Ҫ�õ����bλʱʹ�õ�Mask
		int nMaskLeft = nInteger << b; //Ҫ�õ���λʱʹ�õ�Mask

		int nRight = num & nMaskRight; // �õ������bλ
		int nLeft = (num >> 1) & nMaskLeft; // ���ƺ����룬Ҳ���ǰ����bλȥ��

		int nResult = nRight | nLeft;
		return nResult;
	}
	
	/**
	 * ȡһ������num�ĵ�bλ
	 * @param num
	 * @param b
	 * @return
	 */
	public static int getBit(int num, int b)
	{
		return (num >> b) & 1;
	}
	
	static ProcManager getProcManager()
	{
		return (ProcManager)Context.getBean(ProcManager.class);
	}
	static ProcReader getProcReader()
	{
		return (ProcReader)Context.getBean(ProcReader.class);
	}
	/**
	 * �ڲ���������ȡReader�ӿ�
	 * ͳһ����Context.getBean����ʽ
	 * ��Ҫcontext����e5-config.xml��֧��
	 * @return
	 */
	static PermissionReader getReader()
	{
		PermissionReader reader = (PermissionReader)Context.getBean(PermissionReader.class);
		if (reader == null)
			reader = Factory.buildPermissionReader();
		return reader;
	}

	static PermissionManager getManager()
	{
		PermissionManager manager = (PermissionManager)Context.getBean(PermissionManager.class);
		if (manager == null)
			manager = Factory.buildPermissionManager();
		return manager;
	}
	static FVPermissionManager getFVPermissionManager()
	{
		FVPermissionManager manager = (FVPermissionManager)Context.getBean(FVPermissionManager.class);
		if (manager == null)
			manager = Factory.buildFVPermissionManager();
		return manager;
	}
	//Cache
	static PermissionCache getCache(){
		return (PermissionCache)CacheReader.find(PermissionCache.class);
	}
}
