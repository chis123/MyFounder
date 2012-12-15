package com.founder.e5.cat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * 分类缓存
 * @created 2005-7-21
 * @author Gong Lijie
 * @updated wanghc 增加分类和分类扩展属性的缓存 2006-5-22
 * @version 1.0
 */
public class CatCache implements Cache
{
	private CatType[] types = null;

	private CatExtType[] extTypes = null;

	/**
	 * 分类hashmap缓存，key=CatType,value=Category[]
	 */
	private HashMap catMap = new HashMap();

	/**
	 * 分类扩展属性缓存，key=CatType,value=HashMap key=CatExtType,value=CatExt[]
	 */
	private HashMap catExtMap = new HashMap();

	/**
	 * 缓存类主要用在Reader实现类中<br>
	 * 在引用时，注意不要使用构造方法创建缓存类的实例<br>
	 * 请使用如下形式：<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * 其中XXXCache代表具体的缓存类
	 */
	public CatCache()
	{

	}

	public void refresh() throws E5Exception
	{

		CatManager catManager = CatHelper.getCatManager();
		CatExtManager extManager = CatHelper.getExtManager();

		// 分类类型和扩展类型数组缓存
		types = catManager.getTypes();
		extTypes = extManager.getExtTypes();

		// 清除分类和分类扩展取值原有得缓存
		catMap.clear();
		catExtMap.clear();

		for (int typeIndex = 0; types != null && typeIndex < types.length; typeIndex++)
		{
			// 更新分类缓存
			catMap.put(types[typeIndex], catManager.getCats(types[typeIndex]
					.getCatType()));

			// 更新分类扩展属性缓存
			HashMap tExtMap = new HashMap();
			catExtMap.put(types[typeIndex], tExtMap);
			CatExtType[] tExtTypes = extManager.getExtTypes(types[typeIndex]
					.getCatType());

			for (int extIndex = 0; tExtTypes != null
					&& extIndex < tExtTypes.length; extIndex++)
			{
				if (tExtTypes[extIndex].getCatType() == types[typeIndex]
						.getCatType())
				{

					tExtMap.put(tExtTypes[extIndex], extManager.getAllExts(
							types[typeIndex].getCatType(), tExtTypes[extIndex]
									.getType()));
				}
			}
		}

	}

	public void reset()
	{

	}

	CatType[] getCatTypes()
	{
		return types;
	}

	CatType getCatType(int catType)
	{
		if (types == null)
			return null;
		for (int i = 0; i < types.length; i++)
			if (types[i].getCatType() == catType)
				return types[i];
		return null;
	}

	CatType getCatType(String name)
	{
		if (types == null)
			return null;
		for (int i = 0; i < types.length; i++)
			if (types[i].getName().equals(name))
				return types[i];
		return null;
	}

	CatExtType getExtType(int extType)
	{
		if (extTypes == null)
			return null;

		for (int i = 0; i < extTypes.length; i++)
			if (extTypes[i].getType() == extType)
				return extTypes[i];
		return null;
	}

	CatExtType getExtType(String extType)
	{
		if (extTypes == null)
			return null;

		for (int i = 0; i < extTypes.length; i++)
			if (extTypes[i].getTypeName().equals(extType))
				return extTypes[i];
		return null;
	}

	CatExtType[] getExtTypes() throws E5Exception
	{
		return extTypes;
	}

	CatExtType[] getExtTypes(int catType) throws E5Exception
	{
		if (extTypes == null)
			return null;

		List list = new ArrayList(extTypes.length);
		for (int i = 0; i < extTypes.length; i++)
		{
			if ((extTypes[i].getCatType() == 0)
					|| (extTypes[i].getCatType() == catType))
				list.add(extTypes[i]);
		}
		if (list.size() == 0)
			return null;

		return (CatExtType[]) list.toArray(new CatExtType[0]);
	}

	/*-==========从缓存中取得分类=====================================---*/
	Category getCatByName(int catType, String catName)
	{
		// 取分类
		CatType type = getCatType(catType);

		Category cats[] = (Category[]) catMap.get(type);
		if (cats == null) return null;

		Category cat = null;
		for (int i = 0; i < cats.length; i++)
		{
			if (catName.equals(cats[i].getCatName()))
			{
				cat = cats[i];
				break;
			}
		}
		return cat;
	}

	Category getCat(int catType, int catID, String extType)
	{
		// 取分类
		Category cat = null;
		CatType type = getCatType(catType);

		Category cats[] = (Category[]) catMap.get(type);
		if (cats == null)
			return null;

		boolean hasExt = extType != null;

		for (int i = 0; i < cats.length; i++)
		{
			if (cats[i].getCatID() == catID)
			{
				cat = cats[i];
				break;
			}
		}

		// 取扩展
		if (hasExt)
		{

			CatExt[] catExts = getExts(type, extType);
			if (catExts != null)
			{
				CatExt ext = findExt(catExts, catID, 0, catExts.length);
				if (ext != null)
				{
					cat = (Category) cat.clone();
					cat.setCatName(ext.getExtName());
					cat.setCascadeName(ext.getCascadeName());
				}
			}
		}
		return cat;
	}

	Category getCat(String catType, int catID, String extType)
	{
		CatType type = getCatType(catType);
		return getCat(type.getCatType(), catID, extType);
	}

	Category[] getCats(int catType, String extType)
	{
		Category[] cats = null;

		CatType type = getCatType(catType);
		cats = (Category[]) catMap.get(type);

		setCatExt(extType, type, cats);
		return cats;
	}

	Category[] getCats(String catType, String extType)
	{
		CatType type = getCatType(catType);
		return getCats(type.getCatType(), extType);
	}

	Category[] getSubCats(int catType, int catID, String extType)
	{
		CatType type = getCatType(catType);

		Category[] allCats = (Category[]) catMap.get(type);
		if (allCats == null) return null;
		
		ArrayList list = new ArrayList();
		for (int i = 0; i < allCats.length; i++)
		{
			if (allCats[i].getParentID() == catID)
				list.add(allCats[i]);
		}

		Category[] cats = (Category[]) list.toArray(new Category[0]);

		setCatExt(extType, type, cats);

		return cats;
	}

	Category[] getSubCats(String catType, int catID, String extType)
	{
		CatType type = getCatType(catType);
		return getSubCats(type.getCatType(), catID, extType);
	}

	Category[] getChildrenCats(int catType, int catID, String extType)
	{
		CatType type = getCatType(catType);
		Category[] allCats = (Category[]) catMap.get(type);
		if (catID <= 0) return allCats;
		
		Category parent = getCat(catType, catID, null);
		String cascadeID = parent.getCascadeID()+Category.separator;
		
		ArrayList list = new ArrayList();
		for (int i = 0; i < allCats.length; i++)
		{
			// 通过级联名称查询（不包括父节点本身）
			if (allCats[i].getCascadeID().startsWith(cascadeID))
				list.add(allCats[i]);
		}

		Category[] cats = (Category[]) list.toArray(new Category[0]);

		setCatExt(extType, type, cats);

		return cats;
	}

	Category[] getChildrenCats(int catType, int catID, int extType)
	{
		CatExtType type = getExtType(extType);
		return getChildrenCats(catType, catID, type.getTypeName());
	}

	/*-================PRIVATE METHOD=====================---*/
	private void setCatExt(String extType, CatType type, Category[] cats)
	{
		// 取扩展属性
		if (cats != null && extType != null)
		{
			CatExt[] exts = getExts(type, extType);
			if (exts == null)
				return;

			for (int i = 0; i < cats.length; i++)
			{
				cats[i] = (Category) cats[i].clone();
				CatExt catExt = findExt(exts, cats[i].getCatID(), 0,
						exts.length);
				if (catExt != null)
				{
					cats[i].setCascadeName(catExt.getCascadeName());
					cats[i].setCatName(catExt.getExtName());
				}
			}
		}
	}

	private CatExt[] getExts(CatType type, String extType)
	{
		CatExtType tExtType = getExtType(extType);
		HashMap tExtMap = (HashMap) catExtMap.get(type);
		CatExt[] catExts = (CatExt[]) tExtMap.get(tExtType);
		return catExts;
	}

	/**
	 * 二分查找法(CatExt是按照catID排序的)
	 * 
	 * @param source
	 * @param catID
	 * @return
	 */
	private CatExt findExt(CatExt[] source, int catID, int from, int to)
	{
		int index = (to - from) / 2 + from;
		if (source[index].getCatID() == catID)
			return source[index];
		else if (index == 0 || index == to - 1)
			return null;
		else if (source[index].getCatID() > catID)
			return findExt(source, catID, from, index);
		else
			return findExt(source, catID, index, to);
	}
}