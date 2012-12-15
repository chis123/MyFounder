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
 * ���๤����
 * @author wanghc
 * @created 2006-2-28
 * @version 1.0
 */
public class CatService
{
	/**
	 * �������ӿ�
	 */
	private CatManager catManager = null;
	
	/**
	 * �س�����
	 */
	public static final String rn = "\r\n";
	
	/**
	 * �������ƺ����Ե���ʾ˳��
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
	 * ����ȫ���������͵ķ���
	 * @return
	 */
	public String exportXML()
	throws Exception
	{
		StringBuffer root = new StringBuffer();
		root.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(rn);

		root.append("<CATEGORYS>").append(rn);

		//ȡ�����еķ�������
		CatType[] catTypes = catManager.getTypes();
		if (catTypes != null)
		{
			//ÿ�����������������
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
	 * �����ർ��xml��ʽ
	 * 
	 * @param catType    ��������
	 * @param catID      ����ID
	 * @param children   �Ƿ����������
	 * @param deleted    �Ƿ����ɾ�����
	 * @return xml����
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
	 * ��ĳ���������͵�������
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
		
		//�������������µ�ȫ������
		if(catID<=0)
		{
			Category[] cats= catManager.getSubCats(catType,0);
			for(int i=0;cats!=null&&i<cats.length;i++)
			{
				exportCat(cats[i],children,root,tab);
			}
		}
		//����һ������
		else
		{
			Category cat = catManager.getCat(catType,catID);
					
			exportCat(cat, children, root,tab);
		}
		return root.toString();
	}
	
	/*����һ������*/
	private void exportCat(Category cat,boolean children, StringBuffer root, String tab) throws Exception, E5Exception
	{
		root.append("<CATEGORY>").append(rn);	
		appendCat(cat,root,tab);
		
		//2.ȡ�������ӷ���		
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
	 * ���һ�����ൽ root
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
			
			//��ȡֵ���б���
			//v = HtmlEncoder.encode(v);			
			//catName��memo��cascadeName �п��ܰ��������ַ� �����Ƿŵ�CDATA�С�
			if(fn.equals("catName") || fn.equals("memo") || fn.equals("cascadeName"))
				v = "<![CDATA[" + v + "]]>";

			root.append(tab).append("<").append(fn).append(">")
			.append(v)
			.append("</").append(fn).append(">")
			.append(rn);
		}
	}
	
	/**
	 * ����ӵ�root
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
			//���������			
			if(parentID == all[i].getParentID())
			{
				root.append(tab).append("<CATEGORY>").append(rn);
				//����Լ�				
				appendCat(all[i],root,tab+tab());				
				//�����
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
	 * �������������õ�һ��XML���ڵ�
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
	 * �������
	 * @param catID ������ID
	 * @param catType ������catType
	 * @param is �����ļ���
	 */
	public void importCategory(int catID,int catType,InputStream is)
	throws Exception
	{		
		Element root = getRoot(is);
		
		importChild(catID,catType,root);
	}
	/**
	 * ������ࡣ��һ���Ե������������͵ķ��ࡣ
	 * ��ָ���������ͣ���XML��CATTYPE��ȡ������������
	 * ���������Ͳ����ڣ����ȴ�����
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
	 * ��һ���������Ϳ�ʼ���롣
	 * @param root
	 * @throws Exception
	 */
	private void importCatType(Element root)
	throws Exception
	{
		//��ȡCATTYPE�ڵ��name���ԣ���ʾ������������
		String catTypeName = root.attribute("name").getStringValue();
		//������ȡ�÷�������
		CatType catType = catManager.getType(catTypeName);
		//���������Ͳ����ڣ����ȴ�����������
		if (catType == null)
			catType = createCatType(catTypeName);
		//�õ���������ID
		int catTypeID = catType.getCatType();
		//��������
		importChild(0,catTypeID,root);
	}
	/**
	 * ����һ���µķ�������
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
	 * �����ӷ���
	 * @param parentID �������ID��0��ʾ�Ӹ���ʼ
	 * @param catType ��������ID
	 * @param root ������Ľڵ�
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
	 * �������������Ƿ��з���
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