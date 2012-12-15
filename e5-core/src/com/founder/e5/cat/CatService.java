package com.founder.e5.cat;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.*;

/**
 * 分类工具类
 * @author wanghc
 * @created 2006-2-28
 * @version 1.0
 */
public class CatService
{
	/**
	 * 分类管理接口
	 */
	private CatManager catManager = null;
	
	/**
	 * 回车换行
	 */
	public static final String rn = "\r\n";
	
	/**
	 * 属性名称和属性得显示顺序
	 */
	static final String fieldNames[] = {"catType","catID","parentID","catName","cascadeName","catLevel","displayOrder","deleted",
			"userName","lastModified","memo","ref","refType","refTable","refID","catCode","linkTable","linkType","linkID",
			"pubLevel","published","sameGroup"};
	
	private CatService()
	{
		catManager = (CatManager)Context.getBean("CatManager");
	}

	private static CatService instance = null;
	
	static public CatService getIntance()
	{
		if(instance == null)
			instance = new CatService();
		
		return instance;
	}
	/**
	 * 导出全部分类类型的分类
	 * @return
	 */
	public String exportXML()
	throws Exception
	{
		StringBuffer root = new StringBuffer();
		root.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(rn);

		root.append("<CATEGORYS>").append(rn);

		//取出所有的分类类型
		CatType[] catTypes = catManager.getTypes();
		if (catTypes != null)
		{
			//每个分类类型逐个导出
			for (int i = 0; i < catTypes.length; i++)
			{
				root.append("<CATTYPE name=\"").append(StringUtils.encode(catTypes[i].getName())).append("\">").append(rn);
				
				exportOneType(root, catTypes[i].getCatType(), 0, true);
				
				root.append("</CATTYPE>").append(rn);
			}
		}
		root.append("</CATEGORYS>").append(rn);		
		return root.toString();
	}
	/**
	 * 将分类导出xml格式
	 * 
	 * @param catType    分类类型
	 * @param catID      分类ID
	 * @param children   是否包含子孙结点
	 * @param deleted    是否包含删除结点
	 * @return xml内容
	 */
	public String exportXML(int catType, int catID, boolean children)
		throws Exception
	{
		StringBuffer root = new StringBuffer();
		root.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(rn);
		
		root.append("<CATEGORYS>").append(rn);

		exportOneType(root, catType, catID, children);
		
		root.append("</CATEGORYS>").append(rn);		
		
		return root.toString();
	}
	/**
	 * 按某个分类类型导出分类
	 * @param root
	 * @param catType
	 * @param catID
	 * @param children
	 * @return
	 * @throws Exception
	 */
	private String exportOneType(StringBuffer root, int catType, int catID, boolean children)
	throws Exception
	{
		//catManager.getChildrenCats()
		String tab = tab();
		
		//导出分类类型下的全部分类
		if(catID<=0)
		{
			Category[] cats= catManager.getSubCats(catType,0);
			for(int i=0;cats!=null&&i<cats.length;i++)
			{
				exportCat(cats[i],children,root,tab);
			}
		}
		//导出一个分类
		else
		{
			Category cat = catManager.getCat(catType,catID);
					
			exportCat(cat, children, root,tab);
		}
		return root.toString();
	}
	
	/*导出一个分类*/
	private void exportCat(Category cat,boolean children, StringBuffer root, String tab) throws Exception, E5Exception
	{
		root.append("<CATEGORY>").append(rn);	
		appendCat(cat,root,tab);
		
		//2.取得所有子分类		
		if(children)
		{
			Category[] all = catManager.getChildrenCats(cat.getCatType(),cat.getCatID(),null);
			if(all!=null)
			{
				appendSubCat(cat.getCatID(),all,root,tab);
			}
		}		
		root.append("</CATEGORY>").append(rn);
	}
	/**
	 * 添加一个分类到 root
	 * @param p
	 * @param root
	 * @param tab
	 */
	private void appendCat(Category p,StringBuffer root,String tab)
		throws Exception
	{
		for(int i=0;i<fieldNames.length;i++)
		{
			String fn = fieldNames[i];
			String v  = null;
			try
			{
				v = BeanUtils.getProperty(p,fieldNames[i]);
			}catch(Exception ex){}
			
			if(v == null) v = "";
			
			//对取值进行编码
			//v = HtmlEncoder.encode(v);			
			//catName、memo、cascadeName 中可能包含特殊字符 将他们放到CDATA中。
			if(fn.equals("catName") || fn.equals("memo") || fn.equals("cascadeName"))
				v = "<![CDATA[" + v + "]]>";

			root.append(tab).append("<").append(fn).append(">")
			.append(v)
			.append("</").append(fn).append(">")
			.append(rn);
		}
	}
	
	/**
	 * 添加子到root
	 * @param parentID
	 * @param all
	 * @param root
	 * @param tab
	 * @throws Exception
	 */
	private void appendSubCat(int parentID,Category all[],StringBuffer root,String tab)
		throws Exception
	{
		//tab = tab + tab();
		
		for(int i=0;i<all.length;i++)
		{
			//如果是子类			
			if(parentID == all[i].getParentID())
			{
				root.append(tab).append("<CATEGORY>").append(rn);
				//添加自己				
				appendCat(all[i],root,tab+tab());				
				//添加子
				appendSubCat(all[i].getCatID(),all,root,tab+tab());
				root.append(tab).append("</CATEGORY>").append(rn);
			}
		}
	}
	
	private String tab()
	{
		return "	";
	}	

	/**
	 * 解析输入流，得到一个XML根节点
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private Element getRoot(InputStream is)
	throws Exception
	{
		SAXReader reader  = new SAXReader();
		Document document = reader.read(is);
		return document.getRootElement();
	}
	/**
	 * 导入分类
	 * @param catID 父分类ID
	 * @param catType 父分类catType
	 * @param is 分类文件流
	 */
	public void importCategory(int catID,int catType,InputStream is)
	throws Exception
	{		
		Element root = getRoot(is);
		
		importChild(catID,catType,root);
	}
	/**
	 * 导入分类。可一次性导入多个分类类型的分类。
	 * 不指定分类类型，从XML的CATTYPE中取分类类型名。
	 * 若分类类型不存在，则先创建。
	 * @param is
	 */
	public void importCategory(InputStream is)
	throws Exception
	{
		Element root = getRoot(is);

		Iterator it = root.elementIterator("CATTYPE");
		while (it.hasNext())
		{
			Element e = (Element)it.next();
			
			importCatType(e);
		}		
	}
	/**
	 * 从一个分类类型开始导入。
	 * @param root
	 * @throws Exception
	 */
	private void importCatType(Element root)
	throws Exception
	{
		//读取CATTYPE节点的name属性，表示分类类型名称
		String catTypeName = root.attribute("name").getStringValue();
		//按名称取得分类类型
		CatType catType = catManager.getType(catTypeName);
		//若分类类型不存在，则先创建分类类型
		if (catType == null)
			catType = createCatType(catTypeName);
		//得到分类类型ID
		int catTypeID = catType.getCatType();
		//正常导入
		importChild(0,catTypeID,root);
	}
	/**
	 * 创建一个新的分类类型
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private CatType createCatType(String name)
	throws Exception
	{
		CatType type = new CatType();
		type.setName(name);
		type.setTableName("");
		catManager.createType(type);
		
		return type;
	}
	/**
	 * 导入子分类
	 * @param parentID 父分类的ID，0表示从根开始
	 * @param catType 分类类型ID
	 * @param root 父分类的节点
	 * @throws Exception
	 */
	private void importChild(int parentID,int catType,Element root)
		throws Exception
	{
		Iterator it = root.elementIterator("CATEGORY");
		while (it.hasNext())
		{
			Element e = (Element)it.next();
			Category cat = new Category();
			
			for(int i=0;i<CatService.fieldNames.length;i++)
			{
				Element value = (Element)e.element(CatService.fieldNames[i]);
				if(value!=null)
				{
					try{			
						BeanUtils.copyProperty(cat,CatService.fieldNames[i],value.getText());
					}catch(Exception ex){}
				}
			}
			
			cat.setParentID(parentID);
			cat.setCatType(catType);
			//save
			catManager.createCat(cat);
			
			//add child
			importChild(cat.getCatID(),catType,e);
		}		
	}
	
	/**
	 * 检查分类类型下是否有分类
	 * @param catType
	 * @return
	 */
	public boolean hasCategory(int catType)
		throws E5Exception
	{
		Category cat[] = catManager.getSubCats(catType,0);
		if(cat == null || cat.length == 0)
			return false;
		else
			return true;
	}
}