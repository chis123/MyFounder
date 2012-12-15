package com.founder.e5.cat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * ���໺��
 * @created 2005-7-21
 * @author Gong Lijie
 * @updated wanghc ���ӷ���ͷ�����չ���ԵĻ��� 2006-5-22
 * @version 1.0
 */
public class CatCache implements Cache
{
	private CatType[] types = null;

	private CatExtType[] extTypes = null;

	/**
	 * ����hashmap���棬key=CatType,value=Category[]
	 */
	private HashMap catMap = new HashMap();

	/**
	 * ������չ���Ի��棬key=CatType,value=HashMap key=CatExtType,value=CatExt[]
	 */
	private HashMap catExtMap = new HashMap();

	/**
	 * ��������Ҫ����Readerʵ������<br>
	 * ������ʱ��ע�ⲻҪʹ�ù��췽�������������ʵ��<br>
	 * ��ʹ��������ʽ��<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * ����XXXCache�������Ļ�����
	 */
	public CatCache()
	{

	}

	public void refresh() throws E5Exception
	{

		CatManager catManager = CatHelper.getCatManager();
		CatExtManager extManager = CatHelper.getExtManager();

		// �������ͺ���չ�������黺��
		types = catManager.getTypes();
		extTypes = extManager.getExtTypes();

		// �������ͷ�����չȡֵԭ�еû���
		catMap.clear();
		catExtMap.clear();

		for (int typeIndex = 0; types != null && typeIndex < types.length; typeIndex++)
		{
			// ���·��໺��
			catMap.put(types[typeIndex], catManager.getCats(types[typeIndex]
					.getCatType()));

			// ���·�����չ���Ի���
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

	/*-==========�ӻ�����ȡ�÷���=====================================---*/
	Category getCatByName(int catType, String catName)
	{
		// ȡ����
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
		// ȡ����
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

		// ȡ��չ
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
			// ͨ���������Ʋ�ѯ�����������ڵ㱾��
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
		// ȡ��չ����
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
	 * ���ֲ��ҷ�(CatExt�ǰ���catID�����)
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