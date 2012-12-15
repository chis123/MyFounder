package com.founder.e5.dom;

import java.io.InputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @version 1.0
 * @created 11-����-2005 15:23:41
 */
public class FolderView {
	public static final boolean showKeepDay;
	/**
	 * ��WEB-INF/classes/e5-config.xml�ж�ȡ�Ƿ������ļ��б�������
	 * һ���Զ�ȡ��
	 * �����ļ����иĶ���������Ӧ�÷�������
	 */
	static{
		String configFile = "e5-config.xml";
		
        InputStream in = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(configFile);
	    if (in == null) showKeepDay = false;
	    else {
			String show = null;
		    try {
		    	show = getAttributeValue((new SAXReader()).read(in));
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			showKeepDay = "true".equals(show);
	    }
	}
	private static String getAttributeValue(Document doc)
	{
		Node format = doc.selectSingleNode("//folder-config");
		if (format == null) return "";

		Attribute child = (Attribute)format.selectSingleNode("@showKeepDay");
		if (child != null) return child.getValue();
		else return "";
	}

    private int defaultLayoutID;

    /**
     * ��Ӧ�ĵ���ID
     */
    private int docLibID;

    /**
     * ��Ӧ�ĵ�����ID
     */
    private int docTypeID;

    /**
     * �ļ���/��ͼID
     */
    private int FVID;

    /**
     * �ļ�����ͼ����
     */
    private String FVName;

    private int keepDay;

    /**
     * ��ID
     */
    private int parentID;

    /**
     * ��ID
     */
    private int rootID;

    private int treeLevel;

    private int treeOrder;
    
    /**
     * ��FolderView�������ĵ�����ļ������ϵ����У����ڻ����У�����ƽ̨ʹ��
     */
    private int orderTogether;
    
    /**
     * ��FolderView�ĸ����飬�����ĵ��⵽�丸��fvid�����飬���ڻ����У�����ƽ̨ʹ��
     */
    private int[] parents;
    
    /**
     * ȱʡ���캯��
     */
    public FolderView() {

    }

    /**
     * ����ȱʡ����ID
     * @return ȱʡ����ID
     */
    public int getDefaultLayoutID() {
        return defaultLayoutID;
    }

    /**
     * �����ĵ���ID
     * @return �ĵ���ID
     */
    public int getDocLibID() {
        return docLibID;
    }

    /**
     * �����ĵ�����ID
     * @return �ĵ�����ID
     */
    public int getDocTypeID() {
        return docTypeID;
    }

    /**
     * ����ID
     * @return ID
     */
    public int getFVID() {
        return FVID;
    }

    /**
     * ��������
     * @return ����
     */
    public String getFVName() {
        return FVName;
    }

    /**
     * ���ر�������
     * @return ��������
     */
    public int getKeepDay() {
        return keepDay;
    }

    /**
     * ���ظ��ڵ�ID
     * @return ���ڵ�ID
     */
    public int getParentID() {
        return parentID;
    }

    /**
     * ���ظ��ļ���ID
     * @return ���ļ���ID
     */
    public int getRootID() {
        return rootID;
    }

    /**
     * �����������еĲ��
     * @return �������еĲ��
     */
    public int getTreeLevel() {
        return treeLevel;
    }

    /**
     * ��������ͬ���ڵ��еĴ���
     * @return ����ͬ���ڵ��еĴ���
     */
    public int getTreeOrder() {
        return treeOrder;
    }

    /**
     * ����ȱʡ����ID
     * @param defaultLayoutID ȱʡ����ID
     */
    public void setDefaultLayoutID(int defaultLayoutID) {
        this.defaultLayoutID = defaultLayoutID;
    }

    /**
     * �����ĵ���ID
     * @param docLibID �ĵ���ID
     */
    public void setDocLibID(int docLibID) {
        this.docLibID = docLibID;
    }

    /**
     * �����ĵ�����ID
     * @param doctypeID �ĵ�����ID
     */
    public void setDocTypeID(int doctypeID) {
        this.docTypeID = doctypeID;
    }

    public void setFVID(int fvid) {
        FVID = fvid;
    }

    /**
     * ��������
     * @param name ����
     */
    public void setFVName(String name) {
        FVName = name;
    }

    /**
     * ���ñ�������
     * @param keepDay ��������
     */
    public void setKeepDay(int keepDay) {
        this.keepDay = keepDay;
    }

    /**
     * ���ø��ڵ�ID
     * @param parentID ���ڵ�ID
     */
    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    /**
     * ���ø��ڵ�ID
     * @param rootID ���ڵ�ID
     */
    public void setRootID(int rootID) {
        this.rootID = rootID;
    }

    /**
     * �����������еĲ��
     * @param treeLevel �������еĲ��
     */
    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    /**
     * ��������ͬ��εĴ���
     * @param treeOrder ����ͬ��εĴ���
     */
    public void setTreeOrder(int treeOrder) {
        this.treeOrder = treeOrder;
    }

    /**
     * ���ص�ǰ��������������˳��
     * @return �������������е�˳��
     */
    public int getOrderTogether() {
        return orderTogether;
    }

    /**
     * @param orderTogether The orderTogether to set.
     */
    public void setOrderTogether(int orderTogether) {
        this.orderTogether = orderTogether;
    }

    /**
     * �������Ƚڵ��id
     * @return �������Ƚڵ��id���飬���ղ������
     */
    public int[] getParents() {
        return parents;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
        
    }

    /**
     * @param parents The parents to set.
     */
    public void setParents(int[] parents) {
        this.parents = parents;
    }
    
    /**
     * �ж��Ƿ��ļ���
     * @return ����true������Ϊfalse
     */
    public boolean isFolder(){        
        String name = this.getClass().getName();
        return name.equalsIgnoreCase("com.founder.e5.dom.Folder");
    }

}