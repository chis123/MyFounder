package com.founder.e5.cat;

/**
 * @created 21-7-2005 16:28:05
 * @author Gong Lijie
 * @version 1.0
 */
public class Factory {
	private static CatReader catReader;
	private static CatManager catManager;
	
	private static CatExtManager catExtManager;
	private static CatExtReader catExtReader;
	
	public static CatReader buildCatReader(){
		if (catReader == null) catReader = new CatReaderImpl();
		return catReader;
	}

	public static CatManager buildCatManager(){
		if (catManager == null) catManager = new CatManagerImpl();
		return catManager;
	}

	public static CatExtManager buildCatExtManager(){
		if (catExtManager == null) catExtManager = new CatExtManagerImpl();
		return catExtManager;
	}
	/**
	 * ������չ���ԵĶ�ȡ��
	 * ע�������չ����û�л��棬���Ի��ǵ��ù�������ʵ����
	 */
	public static CatExtReader buildCatExtReader(){
		if (catExtReader == null) catExtReader = new CatExtManagerImpl();
		return catExtReader;
	}

}