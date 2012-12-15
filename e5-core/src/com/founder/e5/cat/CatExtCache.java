package com.founder.e5.cat;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @deprecated 2006-5-17
 * 已经被合并在CatCache中
 * 
 * 分类扩展属性缓存
 * 目前只缓存扩展属性的类型
 * 具体的扩展属性不做缓存
 * @created 21-7-2005 16:27:14
 * @author Gong Lijie
 * @version 1.0
 */
public class CatExtCache implements Cache {
	private CatExtType[] extTypes = null;

	public void refresh() throws E5Exception{
		CatExtManager manager = CatHelper.getExtManager();
		extTypes = manager.getExtTypes();
	}

	public void reset(){
	}

	public CatExtType getExtType(int extType)
	{
		if (extTypes == null) return null;
		
		for (int i = 0; i < extTypes.length; i++)
			if (extTypes[i].getType() == extType)
				return extTypes[i];
		return null;
	}
	
	public CatExtType getExtType(String extType) throws E5Exception
	{
		if (extTypes == null) return null;
		
		for (int i = 0; i < extTypes.length; i++)
			if (extTypes[i].getTypeName().equals(extType))
				return extTypes[i];
		return null;
	}

	public CatExtType[] getExtTypes() throws E5Exception
	{
		return extTypes;
	}

	public CatExtType[] getExtTypes(int catType) throws E5Exception
	{
		if (extTypes == null) return null;
		
		List list = new ArrayList(extTypes.length);
		for (int i = 0; i < extTypes.length; i++)
		{
			if ((extTypes[i].getCatType() == 0) 
					|| (extTypes[i].getCatType() == catType))
				list.add(extTypes[i]);
		}
		if (list.size() == 0) return null;
		
		return (CatExtType[])list.toArray(new CatExtType[0]);
	}
}