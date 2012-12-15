package com.founder.e5.sys.org;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.founder.e5.sys.org.OrgTypeReader;
import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoOrg;

/**
 * @version 1.0
 * @created 11-����-2005 10:32:01
 */
class OrgTypeReaderImpl implements OrgTypeReader {

    //�洢������ID����������ӳ��
	private static Map idMap;
	//�洢��������������ID��ӳ��
	private static Map nameMap;
	//��֯���͵�����
	private static OrgType[] orgTypes;

	public OrgTypeReaderImpl(){

	}

	static
	{
//�������ѳ�ʼ���������ݷŵ�initOrgTypeInfo��
//2006-4-10		
	}
	
	/**
	 * ͨ����֯����ID�õ���Ӧ����֯������
	 * @param typeID   ��֯��������ID
	 * @return ��֯����������
	 */
	public String getTypeName(String typeID){
		initOrgTypeInfo();		
	    if(idMap.get(typeID) != null)
	        return (String)idMap.get(typeID);
		return null;
	}

	/**
	 * ͨ����֯����������Ӧ����֯����ID
	 * @param typeName    ��֯����������
	 * @return ��֯��������ID
	 */
	public String getTypeID(String typeName){
		initOrgTypeInfo();		
	    if(nameMap.get(typeName) != null)
	        return (String)nameMap.get(typeName);
		return null;
	}

	/**
	 * �����֯�������͵�����
	 */
	public OrgType[] get(){
		initOrgTypeInfo();
		return orgTypes;
	}
	
	private void initOrgTypeInfo()
	{
		
		if(orgTypes==null)
		{
		    idMap = new HashMap();
		    nameMap = new HashMap();
		    ConfigReader reader = ConfigReader.getInstance();
		    List orgs = reader.getOrgs();
		    List orgtypes = new ArrayList();
		    for(int i = 0; i < orgs.size(); i++)
		    {
		        InfoOrg org = (InfoOrg)orgs.get(i);
		        idMap.put(org.getId(), org.getName());
		        nameMap.put(org.getName(), org.getId());
		        OrgType orgType = new OrgType();
		        orgType.setId(org.getId());
		        orgType.setName(org.getName());
		        orgtypes.add(orgType);
		    }
		    orgTypes = new OrgType[orgtypes.size()];
		    orgtypes.toArray(orgTypes);
			
		}
	}

}