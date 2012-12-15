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
	 * 分类扩展属性的读取器
	 * 注意分类扩展属性没有缓存，所以还是调用管理器的实现类
	 */
	public static CatExtReader buildCatExtReader(){
		if (catExtReader == null) catExtReader = new CatExtManagerImpl();
		return catExtReader;
	}

}