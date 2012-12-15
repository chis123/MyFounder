package com.founder.e5.sys.org;

import java.io.Serializable;

/**
 * 统一的缺省工作目录类，包括对机构(type=0)、对角色(type=1)、对用户(type=2)进行的设置。<br/>
 * 
 * 目前用户缺省工作目录的使用类是UserFolder，而没有使用DefaultFolder。<br/>
 * @created 2007-6-19
 * @author Gong Lijie
 * @version 1.0
 */
public class DefaultFolder implements Serializable {
	//缺省目录的设置类型常量
	public static final int DEFAULTFOLDER_ORG = 0;//机构
	public static final int DEFAULTFOLDER_ROLE = 1;//角色
	public static final int DEFAULTFOLDER_USER = 2;//用户

	private static final long serialVersionUID = 1519311244761137392L;
	private int hashCode = Integer.MIN_VALUE;
	
	private int id;			//ID:机构ID、或角色ID、或用户ID
	private int idType;		//类型：0－机构；1－角色；2－用户
	private int docTypeID; 	//文档类型ID
	private int libID;		//库ID
	private int folderID;	//文件夹ID
	
	public DefaultFolder() {
		super();
		
	}
	public DefaultFolder(int id, int type, int docTypeID, int libID, int folderID) {
		super();
		
		this.id = id;
		this.idType = type;
		this.docTypeID = docTypeID;
		this.libID = libID;
		this.folderID = folderID;
	}
	
	public int getDocTypeID() {
		return docTypeID;
	}
	public void setDocTypeID(int docTypeID) {
		this.docTypeID = docTypeID;
	}
	public int getFolderID() {
		return folderID;
	}
	public void setFolderID(int folderID) {
		this.folderID = folderID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLibID() {
		return libID;
	}
	public void setLibID(int libID) {
		this.libID = libID;
	}
	public int getIdType() {
		return idType;
	}
	public void setIdType(int type) {
		this.idType = type;
	}
	
    public boolean equals (Object obj) 
    {
        if(obj == null) return false;
        if(! (obj instanceof DefaultFolder))
            return false;
        else
        {
        	DefaultFolder defaultFolder = (DefaultFolder)obj;
            if(defaultFolder.getId() == this.id 
            		&& defaultFolder.getIdType() == this.idType
            		&& defaultFolder.getDocTypeID() == this.docTypeID)
                return true;
        }
        return false;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer(100);
			sb.append(id).append(':').append(idType).append(':').append(docTypeID);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}
