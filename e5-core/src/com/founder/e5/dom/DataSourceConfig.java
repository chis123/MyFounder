package com.founder.e5.dom;

/**
 * ����ĳģ����Ҫ�ĵ�������Դ�������������֡���������赥������Դ�����¼�����ǣ�(��DOM_RELATIONS��,2)
 * 
 * @version 1.0
 * @created 11-����-2005 15:40:53
 */
public class DataSourceConfig {

    /**
     * ���ơ�Ψһ������������ҵ����Ӧ������ԴID
     */
    private String dsName;

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
     * ��������Դ����
     * @return ����Դ����
     */
    public String getDsName ( ) {
        return dsName;
    }

    /**
     * ��������Դ����
     * @param name ����Դ����
     */
    public void setDsName ( String name ) {
        this.dsName = name;
    }

    private int dsID;

    public DataSourceConfig() {

    }

    public DataSourceConfig(String name, int dsid) {
        super ( );
        this.dsID = dsid;
        this.dsName = name;
    }

}