package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * 分类的读取器
 * 注意分类没有使用缓存
 * @created 21-7-2005 16:28:28
 * @author Gong Lijie
 * @version 1.0
 */
class CatReaderImpl implements CatReader {

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCat(java.lang.String, int, java.lang.String)
	 */
	public Category getCat(String catTypeName, int catID, String extType) throws E5Exception
	{
		return CatHelper.getCache().getCat(catTypeName, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCat(int, int, java.lang.String)
	 */
	public Category getCat(int catType, int catID, String extType) throws E5Exception
	{
		return CatHelper.getCache().getCat(catType, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCats(java.lang.String, java.lang.String)
	 */
	public Category[] getCats(String catTypeName, String extType) throws E5Exception
	{
		return CatHelper.getCache().getCats(catTypeName, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCats(int, java.lang.String)
	 */
	public Category[] getCats(int catType, String extType) throws E5Exception
	{
		return CatHelper.getCache().getCats(catType, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getSubCats(java.lang.String, int, java.lang.String)
	 */
	public Category[] getSubCats(String catTypeName, int catID, String extType) throws E5Exception
	{
		return CatHelper.getCache().getSubCats(catTypeName, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getSubCats(int, int, java.lang.String)
	 */
	public Category[] getSubCats(int catType, int catID, String extType) throws E5Exception
	{
		return CatHelper.getCache().getSubCats(catType, catID, extType);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatTypeReader#get(int)
	 */
	public CatType getType(int id) throws E5Exception{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
			return cache.getCatType(id);
		return CatHelper.getCatManager().getType(id);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatTypeReader#getCatTypeByName(java.lang.String)
	 */
	public CatType getType(String name) throws E5Exception{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
			return cache.getCatType(name);

		return CatHelper.getCatManager().getType(name);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getTypes()
	 */
	public CatType[] getTypes() throws E5Exception
	{
		CatCache cache = CatHelper.getCache();
		if (cache != null)
			return cache.getCatTypes();

		return CatHelper.getCatManager().getTypes();
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getChildrenCats(int, int, int)
	 */
	public Category[] getChildrenCats(int catType, int catID, int extType) throws E5Exception
	{
		return CatHelper.getCache().getChildrenCats(catType, catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getChildrenCats(int, int, java.lang.String)
	 */
	public Category[] getChildrenCats(int catType, int catID, String extType) throws E5Exception
	{
		return CatHelper.getCache().getChildrenCats(catType, catID, extType);
	}

	public Category getCat(String catTypeName, int catID) 
	throws E5Exception 
	{
		return getCat(catTypeName, catID, null);
	}

	public Category getCat(int catType, int catID) throws E5Exception 
	{
		return getCat(catType, catID, null);
	}

	public Category[] getCats(String catTypeName) throws E5Exception 
	{
		return getCats(catTypeName, null);
	}

	public Category[] getCats(int catType) throws E5Exception {
		return getCats(catType, null);
	}

	public Category[] getChildrenCats(int catType, int catID) 
	throws E5Exception 
	{
		return getChildrenCats(catType, catID, null);
	}

	public Category[] getSubCats(String catTypeName, int catID) 
	throws E5Exception 
	{
		return getSubCats(catTypeName, catID, null);
	}

	public Category[] getSubCats(int catType, int catID) 
	throws E5Exception 
	{
		return getSubCats(catType, catID, null);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCat(int[])
	 */
	public Category[] getCat(int catType,String catIDs) throws E5Exception
	{
		if(catIDs == null || "".equals(catIDs))
			return null;
		
		
		String[] catIDArray = catIDs.split(",");
		Category[] cats = new Category[catIDArray.length];
		for(int i=0;i<catIDArray.length;i++)
		{
			cats[i] = this.getCat(catType,Integer.parseInt(catIDArray[i]));			
		}		
		return cats;
	}

	public Category getCatByName(int catType, String catName) throws E5Exception
	{
		return CatHelper.getCache().getCatByName(catType, catName);
	}
}