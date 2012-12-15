package com.founder.e5.sys.org;

import java.io.Serializable;

/**
 * ͳһ��ȱʡ����Ŀ¼�࣬�����Ի���(type=0)���Խ�ɫ(type=1)�����û�(type=2)���е����á�<br/>
 * 
 * Ŀǰ�û�ȱʡ����Ŀ¼��ʹ������UserFolder����û��ʹ��DefaultFolder��<br/>
 * @created 2007-6-19
 * @author Gong Lijie
 * @version 1.0
 */
public class DefaultFolder implements Serializable {
	//ȱʡĿ¼���������ͳ���
	public static final int DEFAULTFOLDER_ORG = 0;//����
	public static final int DEFAULTFOLDER_ROLE = 1;//��ɫ
	public static final int DEFAULTFOLDER_USER = 2;//�û�

	private static final long serialVersionUID = 1519311244761137392L;
	private int hashCode = Integer.MIN_VALUE;
	
	private int id;			//ID:����ID�����ɫID�����û�ID
	private int idType;		//���ͣ�0��������1����ɫ��2���û�
	private int docTypeID; 	//�ĵ�����ID
	private int libID;		//��ID
	private int folderID;	//�ļ���ID
	
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
