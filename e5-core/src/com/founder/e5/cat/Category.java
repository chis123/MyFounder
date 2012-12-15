package com.founder.e5.cat;

import java.util.Date;

/**
 * 分类对象,实现了Cloneable接口，可以调用实例的clone方法clone分类对象
 * 
 * @created 21-7-2005 16:28:23
 * @author Gong Lijie
 * @version 1.0
 */
public class Category implements Cloneable
{
	/**
	 * 分类级联名称和级联ID使用的分隔符
	 */
	public static final char separator = '~';
	/**
	 * 是否使用级联名称的标志。
	 * 系统中不需要使用时，可以在分类修改等操作时节省不少时间
	 */
	public static boolean useCascadeName = true;
	
	private int catType;		//分类类型ID
	private int catID;			//分类ID
	private int parentID;		//父分类ID
	private String catName;		//分类名
	
	private String cascadeName;	//级联分类名称，记录从根分类到它自身的名称路径。根据父分类自动在添加时确定
	private String cascadeID;	//级联分类ID，记录从根分类到它自身的ID路径。根据父分类自动在添加时确定
	private int catLevel;		//分类所在的层次，根据父分类自动在添加时确定
	
	private int displayOrder;	//分类在同层的分类中的排序（同父分类的所有子分类排序）
	private boolean deleted;	//是否已被删除
	private String userName;	//最后修改的用户名
	private Date lastModified;	//最后修改时间
	private String memo;		//说明
	
	//引用分类
	private boolean ref;		//是否引用分类
	private int refType;		//若是引用分类，引用的分类类型ID
	private String refTable;	//若是引用分类，引用的分类类型对应的分类表
	private int refID;			//若是引用分类，引用的分类ID

	//分类码
	private String catCode;		//分类码，业务上的编码或者旧码对应
	
	//对应的关联表
	private String linkTable;	//分类对应的关联表，与发布相关，确定看该分类时从哪个关联表进行读取
	
	//相关分类类型和分类ID
	private int linkType;		//分类的相关分类类型
	private int linkID;			//分类的相关分类ID
	
	private int pubLevel;		//分类的密级
	private boolean published;//分类是否可发布
	
	private int sameGroup;		//分类组，同组的分类可以被同步增加（暂时不提供该功能）	
	
	/**
	 * 孩子节点数量
	 */
	private int childCount = 0;
	
	/**
	 * 属性对应标记
	 * 可用这些标记指定分类把哪些属性的修改传递到所有子分类
	 */
	public static final int TRANS_CATCODE 	= 0;
	public static final int TRANS_LINKTABLE = 1;
	public static final int TRANS_LINKTYPE 	= 2;
	public static final int TRANS_LINKID 	= 3;
	public static final int TRANS_PUBLISH 	= 4;
	public static final int TRANS_PUBLEVEL 	= 5;
	
	public static final String FIELD_CATCODE 	= "ENTRY_CODE";
	public static final String FIELD_LINKTABLE 	= "ENTRY_LINKTABLE";
	public static final String FIELD_LINKTYPE 	= "ENTRY_LINKTYPE";
	public static final String FIELD_LINKID 	= "ENTRY_LINKID";
	public static final String FIELD_PUBLISH 	= "ENTRY_PUBLISH";
	public static final String FIELD_PUBLEVEL 	= "ENTRY_PUB_LEVEL";

	public static String DEFAULT_TABLENAME = "CATEGORY_OTHER";

	public String toString(){
		return (new StringBuffer()
				.append("[catType:").append(catType)
				.append(",catID:").append(catID)
				.append(",parentID:").append(parentID)
				.append(",catName:").append(catName)
				.append(",cascadeName:").append(cascadeName)
				.append(",catLevel:").append(catLevel)
				.append(",displayOrder:").append(displayOrder)
				.append(",isDeleted:").append(deleted)
				.append(",userName:").append(userName)
				.append(",lastModified:").append(lastModified)
				.append(",memo:").append(memo)
				.append(",isRef:").append(ref)
				.append(",refType:").append(refType)
				.append(",refTable:").append(refTable)
				.append(",refID:").append(refID)
				.append(",catCode:").append(catCode)
				.append(",linkTable:").append(linkTable)
				.append(",linkType:").append(linkType)
				.append(",linkID:").append(linkID)
				.append(",pubLevel:").append(pubLevel)
				.append(",isPublished:").append(published)
				.append(",sameGroup:").append(sameGroup)
				.append("]\n")
				).toString();
	}
	/**
	 * 取得分类组
	 * @return 分类组 
	 */
	public int getSameGroup()
	{
		return sameGroup;
	}
	/**
	 * 设置分类组
	 * @param isSame 分类组
	 */
	public void setSameGroup(int isSame)
	{
		this.sameGroup = isSame;
	}
	/**
	 * 取得分类的级联名称,级联名称根据分类所在的层次(level)和分类名称计算。<br>
	 * 如：<br>
	 *  root<br>
	 *  + A<br>
	 *  &nbsp&nbsp;+ B<br>
	 *  &nbsp&nbsp;&nbsp&nbsp;+ C<br>
	 *  那么A的级联名称为：root~A<br>
	 *  B的级联名称为：root~A~B 依此类推.<br>
	 *  ~分隔符是由<code>Category.separator</code>指定的<br>
	 *  
	 *  取得分类时若指定extType(扩展属性类型)并存在该值,分类级联名称将被替换为扩展属性(别名)的级联名称返回 
	 * @return 分类级联名称
	 */
	public String getCascadeName()
	{
		return cascadeName;
	}
	/**
	 * 	不允许从外面设置级联名称
	 * @param cascadeName The cascadeName to set.
	 */
	void setCascadeName(String cascadeName)
	{
		this.cascadeName = cascadeName;
	}
	/**
	 * 取得分类码
	 * @return 分类码
	 */
	public String getCatCode()
	{
		return catCode;
	}
	/**
	 * 设置分类码
	 * @param catCode 分类码
	 */
	public void setCatCode(String catCode)
	{
		this.catCode = catCode;
	}
	/**
	 * 取得分类ID
	 * @return 分类ID
	 */
	public int getCatID()
	{
		return catID;
	}
	/**
	 * 设置分类ID
	 * @param catID 分类ID
	 */
	public void setCatID(int catID)
	{
		this.catID = catID;
	}
	/**
	 * 取得分类层次,层次从0开始
	 * 
	 * @return 分类层次
	 */
	public int getCatLevel()
	{
		return catLevel;
	}
	/**
	 * 不允许从外面设置分类层次
	 * @param catLevel 设置分类层次.
	 */
	void setCatLevel(int catLevel)
	{
		this.catLevel = catLevel;
	}
	/**
	 * 取得分类名称<br>
	 * 取得分类时如果指定extType(分类扩展属性)并存在该值,将用分类扩展属性的取值替换分类的名称返回.
	 * @return Returns the catName.
	 */
	public String getCatName()
	{
		return catName;
	}
	/**
	 * 设置分类名称
	 * @param catName 分类的名称
	 */
	public void setCatName(String catName)
	{
		this.catName = catName;
	}
	/**
	 * 取得分类的分类类型ID
	 * @return 分类类型ID
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * 设置分类类型ID
	 * @param catType 分类类型ID
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
	 * 取得分类在兄弟节点中的显示顺序
	 * @return 显示顺序
	 */
	public int getDisplayOrder()
	{
		return displayOrder;
	}
	/**
	 * 设置分类在兄弟节点中的显示顺序
	 * @param displayOrder 显示顺序
	 */
	public void setDisplayOrder(int displayOrder)
	{
		this.displayOrder = displayOrder;
	}
	/**
	 * 分类是否已删除
	 * @return true/false
	 */
	public boolean isDeleted()
	{
		return deleted;
	}
	
	/**
	 * @param isDeleted The isDeleted to set.
	 */
	void setDeleted(boolean isDeleted)
	{
		this.deleted = isDeleted;
	}
	/**
	 * 分类是否已发布
	 * @return true/false
	 */
	public boolean isPublished()
	{
		return published;
	}
	/**
	 * 设置分类发布
	 * @param isPublished 是否发布
	 */
	public void setPublished(boolean isPublished)
	{
		this.published = isPublished;
	}
	/**
	 * 是否引用分类
	 * @return 是否引用分类
	 */
	public boolean isRef()
	{
		return ref;
	}
	/**
	 * 设置是否引用分类
	 * @param isRef 是否引用分类
	 */
	public void setRef(boolean isRef)
	{
		this.ref = isRef;
	}
	/**
	 * 取得分类最后修改的时间
	 * @return 最后修改的时间
	 */
	public Date getLastModified()
	{
		return lastModified;
	}
	/**
	 * 设置分类最后修改时间
	 * @param lastModified 修改时间
	 */
	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}
	/**
	 * 取得相关分类ID
	 * @return 相关分类ID
	 */
	public int getLinkID()
	{
		return linkID;
	}
	/**
	 * 设置相关分类
	 * @param linkID 相关分类ID
	 */
	public void setLinkID(int linkID)
	{
		this.linkID = linkID;
	}
	
	/**
	 * 取得分类关联表
	 * @return 分类关联表
	 */
	public String getLinkTable()
	{
		return linkTable;
	}
	/**
	 * 设置分类关联表
	 * @param linkTable 分类关联表
	 */
	public void setLinkTable(String linkTable)
	{
		this.linkTable = linkTable;
	}
	/**
	 * 取得相关分类分类类型ID
	 * @return 分类类型ID
	 */
	public int getLinkType()
	{
		return linkType;
	}
	/**
	 * 设置相关分类分类类型
	 * @param linkType 分类类型ID
	 */
	public void setLinkType(int linkType)
	{
		this.linkType = linkType;
	}
	/**
	 * 返回分类说明
	 * @return 分类说明
	 */
	public String getMemo()
	{
		return memo;
	}
	/**
	 * 设置分类说明
	 * @param memo 分类说明
	 */
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	/**
	 * 取得父分类ID
	 * @return 分类ID
	 */
	public int getParentID()
	{
		return parentID;
	}
	/**
	 * 设置父分类ID
	 * @param parentID 父分类ID
	 */
	public void setParentID(int parentID)
	{
		this.parentID = parentID;
	}
	/**
	 * 取得分类密级
	 * @return 分类密级
	 */
	public int getPubLevel()
	{
		return pubLevel;
	}
	/**
	 * 设置分类密级
	 * @param pubLevel 分类密级
	 */
	public void setPubLevel(int pubLevel)
	{
		this.pubLevel = pubLevel;
	}
	/**
	 * 取得引用分类的分类ID
	 * @return 引用分类的分类ID
	 */
	public int getRefID()
	{
		return refID;
	}
	/**
	 * 设置引用分类的分类ID
	 * @param refID 引用分类ID
	 */
	public void setRefID(int refID)
	{
		this.refID = refID;
	}
	/**
	 * 取得引用分类对应的分类表
	 * @return Returns the refTable.
	 */
	public String getRefTable()
	{
		return refTable;
	}
	/**
	 * 设置引用分类对应的分类表
	 * @param refTable 分类表
	 */
	public void setRefTable(String refTable)
	{
		this.refTable = refTable;
	}
	/**
	 * 取得引用分类的分类类型
	 * @return Returns the refType.
	 */
	public int getRefType()
	{
		return refType;
	}
	/**
	 * 设置引用分类的分类类型
	 * @param refType 分类类型ID
	 */
	public void setRefType(int refType)
	{
		this.refType = refType;
	}
	/**
	 * 取得最后修改分类信息的用户名称
	 * @return 用户名称
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * 设置最后修改分类信息的用户名称
	 * @param userName 用户名称
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	/**
	 * 取得分类级联ID
	 * 级联ID和分类级联名称类似，但是指定扩展类型时不会修改级联ID
	 * 
	 * @return 分类级联ID
	 * 
	 */
	public String getCascadeID() {
		return cascadeID;
	}
	//不允许从外面设置级联ID
	void setCascadeID(String cascadeID) {
		this.cascadeID = cascadeID;
	}
	/**
	 * 取得分类子节点个数
	 * @return 子节点个数
	 */
	public int getChildCount()
	{
		return childCount;
	}
	//不允许外部修改
	void setChildCount(int childCount)
	{
		this.childCount = childCount;
	}
	
	/**
	 * Clone分类对象
	 */
	public Object clone()
	{
		Category tmp = null;
		
		try
		{
			tmp = (Category)super.clone();
			tmp.lastModified = (Date)tmp.lastModified.clone();
		}
		catch(Exception e)
		{			
		}
		return tmp;
	}
}