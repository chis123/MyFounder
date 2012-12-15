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
	 * 把权限标记数组 用 或 操作合成一个权限标记，以备比较
	 * @param maskArray 权限标记的数组，如{1,2,4}表示第0位/1位/2位都有权限
	 * @return
	 */
	static int getMask(int[] maskArray){
		int mask = 0;
		for (int i = 0; i < maskArray.length; i++)
			mask = mask | maskArray[i];
		return mask;
	}
	
	/**
	 * 判断是否拥有某几种权限
	 * @param p int 权限
	 * @param mask 权限mask 注意mask=0时始终为false
	 * @return
	 */
	static boolean hasPermission(int p, int mask){
		if ((mask > 0 ) && (p & mask) == mask)
			return true;
		else
			return false;
	}
	/**
	 * 判断是否拥有某几种权限
	 * @param p 权限对象
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
	 * 从一个权限数组中取得拥有某些权限的角色
	 * @param pArr
	 * @param maskArray
	 * @return
	 */
	static int[] getRoleByMask(Permission[] pArr, int[] maskArray){
		//比较权限，得到合适的角色
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
	 * 把一个整数的二进制数中，中间某一位去掉
	 * 整数按32位计
	 * @param num int 原始整数
	 * @param b int 要去掉的位，从0开始
	 * @return int
	 */
	public static int getBitDelete(int num, int b)
	{
		int nInteger = 0x7FFFFFFF;
		int nIntegerBits = 31;

		int nMaskRight = nInteger >> (nIntegerBits - b); //要得到最低b位时使用的Mask
		int nMaskLeft = nInteger << b; //要得到高位时使用的Mask

		int nRight = num & nMaskRight; // 得到仅最低b位
		int nLeft = (num >> 1) & nMaskLeft; // 右移后，做与，也就是把最低b位去掉

		int nResult = nRight | nLeft;
		return nResult;
	}
	
	/**
	 * 取一个数字num的第b位
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
	 * 内部方法，获取Reader接口
	 * 统一改用Context.getBean的形式
	 * 需要context包和e5-config.xml的支持
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
