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
 * @created 11-七月-2005 10:32:01
 */
class OrgTypeReaderImpl implements OrgTypeReader {

    //存储从类型ID到类型名的映射
	private static Map idMap;
	//存储从类型名到类型ID的映射
	private static Map nameMap;
	//组织类型的数组
	private static OrgType[] orgTypes;

	public OrgTypeReaderImpl(){

	}

	static
	{
//王朝阳把初始化部分内容放到initOrgTypeInfo中
//2006-4-10		
	}
	
	/**
	 * 通过组织类型ID得到相应的组织类型名
	 * @param typeID   组织机构类型ID
	 * @return 组织机构类型名
	 */
	public String getTypeName(String typeID){
		initOrgTypeInfo();		
	    if(idMap.get(typeID) != null)
	        return (String)idMap.get(typeID);
		return null;
	}

	/**
	 * 通过组织类型名到相应的组织类型ID
	 * @param typeName    组织机构类型名
	 * @return 组织机构类型ID
	 */
	public String getTypeID(String typeName){
		initOrgTypeInfo();		
	    if(nameMap.get(typeName) != null)
	        return (String)nameMap.get(typeName);
		return null;
	}

	/**
	 * 获得组织机构类型的数组
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