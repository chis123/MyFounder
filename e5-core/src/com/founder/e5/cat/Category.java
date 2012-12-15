package com.founder.e5.cat;

import java.util.Date;

/**
 * �������,ʵ����Cloneable�ӿڣ����Ե���ʵ����clone����clone�������
 * 
 * @created 21-7-2005 16:28:23
 * @author Gong Lijie
 * @version 1.0
 */
public class Category implements Cloneable
{
	/**
	 * ���༶�����ƺͼ���IDʹ�õķָ���
	 */
	public static final char separator = '~';
	/**
	 * �Ƿ�ʹ�ü������Ƶı�־��
	 * ϵͳ�в���Ҫʹ��ʱ�������ڷ����޸ĵȲ���ʱ��ʡ����ʱ��
	 */
	public static boolean useCascadeName = true;
	
	private int catType;		//��������ID
	private int catID;			//����ID
	private int parentID;		//������ID
	private String catName;		//������
	
	private String cascadeName;	//�����������ƣ���¼�Ӹ����ൽ�����������·�������ݸ������Զ������ʱȷ��
	private String cascadeID;	//��������ID����¼�Ӹ����ൽ�������ID·�������ݸ������Զ������ʱȷ��
	private int catLevel;		//�������ڵĲ�Σ����ݸ������Զ������ʱȷ��
	
	private int displayOrder;	//������ͬ��ķ����е�����ͬ������������ӷ�������
	private boolean deleted;	//�Ƿ��ѱ�ɾ��
	private String userName;	//����޸ĵ��û���
	private Date lastModified;	//����޸�ʱ��
	private String memo;		//˵��
	
	//���÷���
	private boolean ref;		//�Ƿ����÷���
	private int refType;		//�������÷��࣬���õķ�������ID
	private String refTable;	//�������÷��࣬���õķ������Ͷ�Ӧ�ķ����
	private int refID;			//�������÷��࣬���õķ���ID

	//������
	private String catCode;		//�����룬ҵ���ϵı�����߾����Ӧ
	
	//��Ӧ�Ĺ�����
	private String linkTable;	//�����Ӧ�Ĺ������뷢����أ�ȷ�����÷���ʱ���ĸ���������ж�ȡ
	
	//��ط������ͺͷ���ID
	private int linkType;		//�������ط�������
	private int linkID;			//�������ط���ID
	
	private int pubLevel;		//������ܼ�
	private boolean published;//�����Ƿ�ɷ���
	
	private int sameGroup;		//�����飬ͬ��ķ�����Ա�ͬ�����ӣ���ʱ���ṩ�ù��ܣ�	
	
	/**
	 * ���ӽڵ�����
	 */
	private int childCount = 0;
	
	/**
	 * ���Զ�Ӧ���
	 * ������Щ���ָ���������Щ���Ե��޸Ĵ��ݵ������ӷ���
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
	 * ȡ�÷�����
	 * @return ������ 
	 */
	public int getSameGroup()
	{
		return sameGroup;
	}
	/**
	 * ���÷�����
	 * @param isSame ������
	 */
	public void setSameGroup(int isSame)
	{
		this.sameGroup = isSame;
	}
	/**
	 * ȡ�÷���ļ�������,�������Ƹ��ݷ������ڵĲ��(level)�ͷ������Ƽ��㡣<br>
	 * �磺<br>
	 *  root<br>
	 *  + A<br>
	 *  &nbsp&nbsp;+ B<br>
	 *  &nbsp&nbsp;&nbsp&nbsp;+ C<br>
	 *  ��ôA�ļ�������Ϊ��root~A<br>
	 *  B�ļ�������Ϊ��root~A~B ��������.<br>
	 *  ~�ָ�������<code>Category.separator</code>ָ����<br>
	 *  
	 *  ȡ�÷���ʱ��ָ��extType(��չ��������)�����ڸ�ֵ,���༶�����ƽ����滻Ϊ��չ����(����)�ļ������Ʒ��� 
	 * @return ���༶������
	 */
	public String getCascadeName()
	{
		return cascadeName;
	}
	/**
	 * 	��������������ü�������
	 * @param cascadeName The cascadeName to set.
	 */
	void setCascadeName(String cascadeName)
	{
		this.cascadeName = cascadeName;
	}
	/**
	 * ȡ�÷�����
	 * @return ������
	 */
	public String getCatCode()
	{
		return catCode;
	}
	/**
	 * ���÷�����
	 * @param catCode ������
	 */
	public void setCatCode(String catCode)
	{
		this.catCode = catCode;
	}
	/**
	 * ȡ�÷���ID
	 * @return ����ID
	 */
	public int getCatID()
	{
		return catID;
	}
	/**
	 * ���÷���ID
	 * @param catID ����ID
	 */
	public void setCatID(int catID)
	{
		this.catID = catID;
	}
	/**
	 * ȡ�÷�����,��δ�0��ʼ
	 * 
	 * @return ������
	 */
	public int getCatLevel()
	{
		return catLevel;
	}
	/**
	 * ��������������÷�����
	 * @param catLevel ���÷�����.
	 */
	void setCatLevel(int catLevel)
	{
		this.catLevel = catLevel;
	}
	/**
	 * ȡ�÷�������<br>
	 * ȡ�÷���ʱ���ָ��extType(������չ����)�����ڸ�ֵ,���÷�����չ���Ե�ȡֵ�滻��������Ʒ���.
	 * @return Returns the catName.
	 */
	public String getCatName()
	{
		return catName;
	}
	/**
	 * ���÷�������
	 * @param catName ���������
	 */
	public void setCatName(String catName)
	{
		this.catName = catName;
	}
	/**
	 * ȡ�÷���ķ�������ID
	 * @return ��������ID
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * ���÷�������ID
	 * @param catType ��������ID
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
	 * ȡ�÷������ֵܽڵ��е���ʾ˳��
	 * @return ��ʾ˳��
	 */
	public int getDisplayOrder()
	{
		return displayOrder;
	}
	/**
	 * ���÷������ֵܽڵ��е���ʾ˳��
	 * @param displayOrder ��ʾ˳��
	 */
	public void setDisplayOrder(int displayOrder)
	{
		this.displayOrder = displayOrder;
	}
	/**
	 * �����Ƿ���ɾ��
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
	 * �����Ƿ��ѷ���
	 * @return true/false
	 */
	public boolean isPublished()
	{
		return published;
	}
	/**
	 * ���÷��෢��
	 * @param isPublished �Ƿ񷢲�
	 */
	public void setPublished(boolean isPublished)
	{
		this.published = isPublished;
	}
	/**
	 * �Ƿ����÷���
	 * @return �Ƿ����÷���
	 */
	public boolean isRef()
	{
		return ref;
	}
	/**
	 * �����Ƿ����÷���
	 * @param isRef �Ƿ����÷���
	 */
	public void setRef(boolean isRef)
	{
		this.ref = isRef;
	}
	/**
	 * ȡ�÷�������޸ĵ�ʱ��
	 * @return ����޸ĵ�ʱ��
	 */
	public Date getLastModified()
	{
		return lastModified;
	}
	/**
	 * ���÷�������޸�ʱ��
	 * @param lastModified �޸�ʱ��
	 */
	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}
	/**
	 * ȡ����ط���ID
	 * @return ��ط���ID
	 */
	public int getLinkID()
	{
		return linkID;
	}
	/**
	 * ������ط���
	 * @param linkID ��ط���ID
	 */
	public void setLinkID(int linkID)
	{
		this.linkID = linkID;
	}
	
	/**
	 * ȡ�÷��������
	 * @return ���������
	 */
	public String getLinkTable()
	{
		return linkTable;
	}
	/**
	 * ���÷��������
	 * @param linkTable ���������
	 */
	public void setLinkTable(String linkTable)
	{
		this.linkTable = linkTable;
	}
	/**
	 * ȡ����ط����������ID
	 * @return ��������ID
	 */
	public int getLinkType()
	{
		return linkType;
	}
	/**
	 * ������ط����������
	 * @param linkType ��������ID
	 */
	public void setLinkType(int linkType)
	{
		this.linkType = linkType;
	}
	/**
	 * ���ط���˵��
	 * @return ����˵��
	 */
	public String getMemo()
	{
		return memo;
	}
	/**
	 * ���÷���˵��
	 * @param memo ����˵��
	 */
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	/**
	 * ȡ�ø�����ID
	 * @return ����ID
	 */
	public int getParentID()
	{
		return parentID;
	}
	/**
	 * ���ø�����ID
	 * @param parentID ������ID
	 */
	public void setParentID(int parentID)
	{
		this.parentID = parentID;
	}
	/**
	 * ȡ�÷����ܼ�
	 * @return �����ܼ�
	 */
	public int getPubLevel()
	{
		return pubLevel;
	}
	/**
	 * ���÷����ܼ�
	 * @param pubLevel �����ܼ�
	 */
	public void setPubLevel(int pubLevel)
	{
		this.pubLevel = pubLevel;
	}
	/**
	 * ȡ�����÷���ķ���ID
	 * @return ���÷���ķ���ID
	 */
	public int getRefID()
	{
		return refID;
	}
	/**
	 * �������÷���ķ���ID
	 * @param refID ���÷���ID
	 */
	public void setRefID(int refID)
	{
		this.refID = refID;
	}
	/**
	 * ȡ�����÷����Ӧ�ķ����
	 * @return Returns the refTable.
	 */
	public String getRefTable()
	{
		return refTable;
	}
	/**
	 * �������÷����Ӧ�ķ����
	 * @param refTable �����
	 */
	public void setRefTable(String refTable)
	{
		this.refTable = refTable;
	}
	/**
	 * ȡ�����÷���ķ�������
	 * @return Returns the refType.
	 */
	public int getRefType()
	{
		return refType;
	}
	/**
	 * �������÷���ķ�������
	 * @param refType ��������ID
	 */
	public void setRefType(int refType)
	{
		this.refType = refType;
	}
	/**
	 * ȡ������޸ķ�����Ϣ���û�����
	 * @return �û�����
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * ��������޸ķ�����Ϣ���û�����
	 * @param userName �û�����
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	/**
	 * ȡ�÷��༶��ID
	 * ����ID�ͷ��༶���������ƣ�����ָ����չ����ʱ�����޸ļ���ID
	 * 
	 * @return ���༶��ID
	 * 
	 */
	public String getCascadeID() {
		return cascadeID;
	}
	//��������������ü���ID
	void setCascadeID(String cascadeID) {
		this.cascadeID = cascadeID;
	}
	/**
	 * ȡ�÷����ӽڵ����
	 * @return �ӽڵ����
	 */
	public int getChildCount()
	{
		return childCount;
	}
	//�������ⲿ�޸�
	void setChildCount(int childCount)
	{
		this.childCount = childCount;
	}
	
	/**
	 * Clone�������
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