package com.founder.e5.dom;

/**
 * �ĵ����������̱�Ķ�Ӧ��ϵ��
 * 
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-����-2005 14:00:31
 */
public class DocLibAdditionals {

    private int id;

    private int libTypes;

    private String libServer;

    private String libDB;

    private String libTable;

    private int docLibID;

    public DocLibAdditionals() {

    }

    /**
     * �����ĵ���ID
     * @return �ĵ���ID
     */
    public int getDocLibID() {
        return docLibID;
    }

    /**
     * �����ĵ���ID
     * @param docLibID �ĵ���ID
     */
    public void setDocLibID(int docLibID) {
        this.docLibID = docLibID;
    }

    /**
     * �����ĵ����Ӧ���̱�ID
     * @return �ĵ����Ӧ���̱�ID
     */
    public int getId() {
        return id;
    }

    /**
     * ������ˮ��
     * @param id ��ˮ��
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * �����ĵ������ݿ�����
     * @return �ĵ������ݿ�����
     */
    public String getLibDB() {
        return libDB;
    }

    /**
     * �����ĵ������ݿ�����
     * @param libDB �ĵ������ݿ����� 
     */
    public void setLibDB(String libDB) {
        this.libDB = libDB;
    }

    /**
     * �����ĵ������������
     * @return �ĵ������������
     */
    public String getLibServer() {
        return libServer;
    }

    /**
     * �����ĵ������������
     * @param libServer �ĵ������������
     */
    public void setLibServer(String libServer) {
        this.libServer = libServer;
    }

    /**
     * �����ĵ����Ӧ������
     * @return �ĵ����Ӧ������
     */
    public String getLibTable() {
        return libTable;
    }

    /**
     * �����ĵ����Ӧ������
     * @param libTable �ĵ����Ӧ������
     */
    public void setLibTable(String libTable) {
        this.libTable = libTable;
    }

    /**
     * �����ĵ�������
     * @return �ĵ�������
     */
    public int getLibTypes() {
        return libTypes;
    }

    /**
     * �����ĵ�������
     * @param libTypes �ĵ�������
     */
    public void setLibTypes(int libTypes) {
        this.libTypes = libTypes;
    }
}