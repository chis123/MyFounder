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
 * @created 11-七月-2005 15:23:41
 */
public class FolderView {
	public static final boolean showKeepDay;
	/**
	 * 从WEB-INF/classes/e5-config.xml中读取是否配置文件夹保存天数
	 * 一次性读取。
	 * 配置文件若有改动，需重启应用服务器。
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
     * 对应文档库ID
     */
    private int docLibID;

    /**
     * 对应文档类型ID
     */
    private int docTypeID;

    /**
     * 文件夹/视图ID
     */
    private int FVID;

    /**
     * 文件夹视图名称
     */
    private String FVName;

    private int keepDay;

    /**
     * 父ID
     */
    private int parentID;

    /**
     * 根ID
     */
    private int rootID;

    private int treeLevel;

    private int treeOrder;
    
    /**
     * 该FolderView在整个文档库的文件夹树上的排行，用在缓存中，工作平台使用
     */
    private int orderTogether;
    
    /**
     * 该FolderView的父数组，即从文档库到其父的fvid的数组，用在缓存中，工作平台使用
     */
    private int[] parents;
    
    /**
     * 缺省构造函数
     */
    public FolderView() {

    }

    /**
     * 返回缺省布局ID
     * @return 缺省布局ID
     */
    public int getDefaultLayoutID() {
        return defaultLayoutID;
    }

    /**
     * 返回文档库ID
     * @return 文档库ID
     */
    public int getDocLibID() {
        return docLibID;
    }

    /**
     * 返回文档类型ID
     * @return 文档类型ID
     */
    public int getDocTypeID() {
        return docTypeID;
    }

    /**
     * 返回ID
     * @return ID
     */
    public int getFVID() {
        return FVID;
    }

    /**
     * 返回名称
     * @return 名称
     */
    public String getFVName() {
        return FVName;
    }

    /**
     * 返回保留天数
     * @return 保留天数
     */
    public int getKeepDay() {
        return keepDay;
    }

    /**
     * 返回父节点ID
     * @return 父节点ID
     */
    public int getParentID() {
        return parentID;
    }

    /**
     * 返回根文件夹ID
     * @return 根文件夹ID
     */
    public int getRootID() {
        return rootID;
    }

    /**
     * 返回所处树中的层次
     * @return 所处树中的层次
     */
    public int getTreeLevel() {
        return treeLevel;
    }

    /**
     * 返回所处同级节点中的次序
     * @return 所处同级节点中的次序
     */
    public int getTreeOrder() {
        return treeOrder;
    }

    /**
     * 设置缺省布局ID
     * @param defaultLayoutID 缺省布局ID
     */
    public void setDefaultLayoutID(int defaultLayoutID) {
        this.defaultLayoutID = defaultLayoutID;
    }

    /**
     * 设置文档库ID
     * @param docLibID 文档库ID
     */
    public void setDocLibID(int docLibID) {
        this.docLibID = docLibID;
    }

    /**
     * 设置文档类型ID
     * @param doctypeID 文档类型ID
     */
    public void setDocTypeID(int doctypeID) {
        this.docTypeID = doctypeID;
    }

    public void setFVID(int fvid) {
        FVID = fvid;
    }

    /**
     * 设置名称
     * @param name 名称
     */
    public void setFVName(String name) {
        FVName = name;
    }

    /**
     * 设置保留天数
     * @param keepDay 保留天数
     */
    public void setKeepDay(int keepDay) {
        this.keepDay = keepDay;
    }

    /**
     * 设置父节点ID
     * @param parentID 父节点ID
     */
    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    /**
     * 设置根节点ID
     * @param rootID 根节点ID
     */
    public void setRootID(int rootID) {
        this.rootID = rootID;
    }

    /**
     * 设置所处树中的层次
     * @param treeLevel 所处树中的层次
     */
    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    /**
     * 设置所处同层次的次序
     * @param treeOrder 所处同层次的次序
     */
    public void setTreeOrder(int treeOrder) {
        this.treeOrder = treeOrder;
    }

    /**
     * 返回当前对象在整棵树的顺序
     * @return 代表在整棵树中的顺序
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
     * 返回祖先节点的id
     * @return 代表祖先节点的id数组，按照层次排序
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
     * 判断是否文件夹
     * @return 是则true，否则为false
     */
    public boolean isFolder(){        
        String name = this.getClass().getName();
        return name.equalsIgnoreCase("com.founder.e5.dom.Folder");
    }

}