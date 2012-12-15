package com.founder.e5.dom;

/**
 * �ĵ���bean
 * 
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-����-2005 14:00:25
 */
public class DocLib {

    private int docLibID;

    private String docLibName;

    private int docTypeID;

    private String docLibTable;

    private int keepDay;

    private String description;

    private String attachDevName;

    private int folderID;

    /**
     * ���ĵ����Ӧ������ԴID
     */
    private int dsID;

    /**
     * �Ƿ�־ÿ�
     */
    private int isPersistent;

    
    /**
     * ȱʡ���캯��
     */
    public DocLib() {

    }
    
    /**
     * ���ظ����豸��
     * @return �����豸��
     */
    public String getAttachDevName ( ) {
        return attachDevName;
    }

    /**
     * @param attachDevName
     *            The attachDevName to set.
     */
    void setAttachDevName ( String attachDevName ) {
        this.attachDevName = attachDevName;
    }

    /**
     * �����ĵ���������Ϣ
     * @return �ĵ���������Ϣ
     */
    public String getDescription ( ) {
        return description;
    }

    /**
     * �����ĵ���������Ϣ
     * @param description ������Ϣ
     */
    public void setDescription ( String description ) {
        this.description = description;
    }

    /**
     * �����ĵ���ID
     * @return �ĵ���ID
     */
    public int getDocLibID ( ) {
        return docLibID;
    }

    /**
     * @param docLibID
     *            The docLibID to set.
     */
    public void setDocLibID ( int docLibID ) {
        this.docLibID = docLibID;
    }

    /**
     * �����ĵ�������
     * @return �ĵ�������
     */
    public String getDocLibName ( ) {
        return docLibName;
    }

    /**
     * �����ĵ�������
     * @param docLibName �ĵ�������
     */
    public void setDocLibName ( String docLibName ) {
        this.docLibName = docLibName;
    }

    /**
     * �����ĵ������
     * @return �ĵ������
     */
    public String getDocLibTable ( ) {
        return docLibTable;
    }

    /**
     * @param docLibTable
     *            The docLibTable to set.
     */
    void setDocLibTable ( String docLibTable ) {
        this.docLibTable = docLibTable;
    }

    /**
     * �����ĵ�����ID
     * @return �ĵ�����ID
     */
    public int getDocTypeID ( ) {
        return docTypeID;
    }

    /**
     * @param docTypeID
     *            The docTypeID to set.
     */
    public void setDocTypeID ( int docTypeID ) {
        this.docTypeID = docTypeID;
    }

    /**
     * ��������ԴID
     * @return ����ԴID
     */
    public int getDsID ( ) {
        return dsID;
    }

    /**
     * ��������ԴID
     * @param dsID ����ԴID
     */
    public void setDsID ( int dsID ) {
        this.dsID = dsID;
    }

    /**
     * �����ĵ����Ӧ�ĸ��ļ���ID
     * @return �ļ���ID
     */
    public int getFolderID ( ) {
        return folderID;
    }

    /**
     * @param folderID
     *            The folderID to set.
     */
    void setFolderID ( int folderID ) {
        this.folderID = folderID;
    }

    /**
     * �����Ƿ�־ÿ�
     * @return 1�����ǣ�0������
     */
    public int getIsPersistent ( ) {
        return isPersistent;
    }

    /**
     * @param isPersistent
     *            The isPersistent to set.
     */
    void setIsPersistent ( int isPersistent ) {
        this.isPersistent = isPersistent;
    }

    /**
     * ���ر�������
     * @return ��������
     */
    public int getKeepDay ( ) {
        return keepDay;
    }

    /**
     * @param keepDay
     *            The keepDay to set.
     */
    public void setKeepDay ( int keepDay ) {
        this.keepDay = keepDay;
    }
}