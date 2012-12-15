package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * 分类扩展属性的读取器
 * 注意分类扩展属性没有缓存，每次调用get方法会查询数据库
 * @created 21-7-2005 16:26:47
 * @author Gong Lijie
 * @version 1.0
 */
class CatExtReaderImpl implements CatExtReader {

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#get(int, int, java.lang.String)
	 */
	public CatExt getExt(int catType, int catID, String extType)
	throws E5Exception
	{
		return CatHelper.getExtManager().getExt(catType, catID, extType);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSub(int, int, java.lang.String)
	 */
	public CatExt[] getSubExt(int catType, int catID, String extType) throws E5Exception
	{
		return CatHelper.getExtManager().getSubExt(catType, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#get(java.lang.String)
	 */
	public CatExtType getExtType(String extType) throws E5Exception
	{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
		{
			return cache.getExtType(extType);
		}
		
		return CatHelper.getExtManager().getExtType(extType);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#get(int)
	 */
	public CatExtType getExtType(int extType) throws E5Exception
	{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
		{
			return cache.getExtType(extType);
		}
		
		return CatHelper.getExtManager().getExtType(extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExtTypes()
	 */
	public CatExtType[] getExtTypes() throws E5Exception
	{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
		{
			return cache.getExtTypes();
		}
		
		return CatHelper.getExtManager().getExtTypes();
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExtTypes(int)
	 */
	public CatExtType[] getExtTypes(int catType) throws E5Exception
	{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
		{
			return cache.getExtTypes(catType);
		}
		
		return CatHelper.getExtManager().getExtTypes(catType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExt(int, int, int)
	 */
	public CatExt getExt(int catType, int catID, int extType) throws E5Exception
	{
		return CatHelper.getExtManager().getExt(catType, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExts(int, int)
	 */
	public CatExt[] getExts(int catType, int catID) throws E5Exception
	{
		return CatHelper.getExtManager().getExts(catType, catID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getAllExts(int, java.lang.String)
	 */
	public CatExt[] getAllExts(int catType, String extType) throws E5Exception
	{
		return CatHelper.getExtManager().getAllExts(catType, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getAllExts(int, int)
	 */
	public CatExt[] getAllExts(int catType, int extType) throws E5Exception
	{
		return CatHelper.getExtManager().getAllExts(catType, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSubExt(int, int, int)
	 */
	public CatExt[] getSubExt(int catType, int catID, int extType) throws E5Exception
	{
		return CatHelper.getExtManager().getSubExt(catType, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSubExt(int, int[], java.lang.String)
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, String extType) throws E5Exception
	{
		return CatHelper.getExtManager().getSubExt(catType, catIDs, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSubExt(int, int[], int)
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, int extType) throws E5Exception
	{
		return CatHelper.getExtManager().getSubExt(catType, catIDs, extType);
	}
}
