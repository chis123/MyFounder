package com.founder.e5.sys.org;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;

public class OrgExchange {

	public class RestoreOrgBean extends Org{
		static final long serialVersionUID =1;

		public RestoreOrgBean()
		{
			super();
			// TODO Auto-generated constructor stub
		}
		private List roleList=null;
		private List userList=null;
		private List orgList=null;
		
		/**
		 * @return Returns the orgList.
		 */
		public List getOrgList() {
			if(orgList==null)
			{
				orgList=new ArrayList();
			}
			return orgList;
		}
		/**
		 * @param orgList The orgList to set.
		 */
		public void setOrgList(List orgList) {
			
			this.orgList = orgList;
		}
		/**
		 * @return Returns the roleList.
		 */
		public List getRoleList() {
			if(roleList==null)
			{
				roleList=new ArrayList();
			}
			return roleList;
		}
		/**
		 * @param roleList The roleList to set.
		 */
		public void setRoleList(List roleList) {
			this.roleList = roleList;
		}
		/**
		 * @return Returns the userList.
		 */
		public List getUserList() {
			if(userList==null)
			{
				userList=new ArrayList();
			}
			return userList;
		}
		/**
		 * @param userList The userList to set.
		 */
		public void setUserList(List userList) {
			this.userList = userList;
		}
		public Org getOrg()
		{ 
			Org org = new Org();
			try
			{
				Class orgClass = org.getClass();
				Method [] methods = orgClass.getDeclaredMethods();
			    for (int i = 0; i < methods.length; i ++)
			    {	
			    	String setName = methods[i].getName();
			    	String indexName = setName.substring(0,3);
			    	if(indexName.equals("set"))
			    	{
			    		String getName = "get"+setName.substring(3);
			    		Method getMothod = orgClass.getDeclaredMethod(getName,null);
			    		Object obj = (Object)getMothod.invoke(this,null);
			    		methods[i].invoke(org,new Object[] {obj});
			    	}
			    }
			}catch(Exception ex)
			{
				ex.getMessage();
			}
			return org;
			
		}
	}

//以上为内引类	
	
	public class RestoreRoleBean extends Role {
		static final long serialVersionUID =1;
		public RestoreRoleBean()
		{
			super();
			// TODO Auto-generated constructor stub
		}
		private List userCodeList=null;
		/**
		 * @return Returns the userCodeList.
		 */
		public List getUserCodeList()
		{
			if(userCodeList==null)
			{
				userCodeList = new ArrayList();
			}
			return userCodeList;
		}
		/**
		 * @param userCodeList The userCodeList to set.
		 */
		public void setUserCodeList(List userCodeList) {
			this.userCodeList = userCodeList;
		}

	}
	
	public OrgExchange() {
		super();
		// TODO Auto-generated constructor stub
	}
	private OrgManager orgMgr=null;
	private RoleManager roleMgr=null;
	private UserManager userMgr=null;
   private Log logger = Context.getLog("e5.sys");

	
	public String getOrgXml(int orgID)
	{
		StringBuffer xmlBf = new StringBuffer();
		xmlBf.append("<?xml version=\"1.0\"?>").append("\r\n");;
		xmlBf.append("<Node version=\"1.0\">").append("\r\n");;
		try
		{
			if(orgID>0)
			{
				Org org = orgMgr.get(orgID);
				xmlBf.append(getOrgXml(org));
			}
			else
			{
				xmlBf.append(getRootXml());
			}
		}
		catch(E5Exception ex)
		{
			logger.error("invoke [exportNode] happend Exception:",ex);
		
		}
		xmlBf.append("</Node>");
		return xmlBf.toString();
		
	}
//	获取整个结构角色用户的XML信息	
		private String getRootXml() throws E5Exception
		{
			StringBuffer bf = new StringBuffer();
			Org [] orgAr = orgMgr.getNextChildOrgs(0);
			if(orgAr!=null)
			{
				for(int i=0;i<orgAr.length;i++)
				{
					bf.append(getOrgXml(orgAr[i]));
				}
				
			}
			return bf.toString();
		
		}

		
//		获取某个结构XML信息	
		private String getOrgXml(Org org) throws E5Exception
		{
			StringBuffer bf = new StringBuffer();
			bf.append("<Organization DBID=\"").append(org.getOrgID()).append("\">").append("\r\n");
			bf.append("<Name>").append(org.getName()).append("</Name>").append("\r\n");
			bf.append("<Code>").append(org.getCode()).append("</Code>").append("\r\n");
			bf.append("<Type>").append(org.getType()).append("</Type>").append("\r\n");
			bf.append("<OrderID>").append(org.getOrderID()).append("</OrderID>").append("\r\n");
			bf.append("<Property1><![CDATA[").append(org.getProperty1()).append("]]></Property1>").append("\r\n");
			bf.append("<Property2><![CDATA[").append(org.getProperty2()).append("]]></Property2>").append("\r\n");
			bf.append("<Property3><![CDATA[").append(org.getProperty3()).append("]]></Property3>").append("\r\n");
			bf.append("<Property4><![CDATA[").append(org.getProperty4()).append("]]></Property4>").append("\r\n");
			bf.append("<Property5><![CDATA[").append(org.getProperty5()).append("]]></Property5>").append("\r\n");
			bf.append("<Property6><![CDATA[").append(org.getProperty6()).append("]]></Property6>").append("\r\n");
			bf.append("<Property7><![CDATA[").append(org.getProperty7()).append("]]></Property7>").append("\r\n");
			Role [] roleAr = orgMgr.getRolesByOrg(org.getOrgID());
			if(roleAr!=null)
			{
				bf.append("<Roles>");
				for(int i=0;i<roleAr.length;i++)
				{
					bf.append(getRoleXml(roleAr[i]));
				}
				bf.append("</Roles>").append("\r\n");
			}
			User [] userAr = userMgr.getUsersByOrg(org.getOrgID());
			if(userAr!=null)
			{
				bf.append("<Users>");
				for(int i=0;i<userAr.length;i++)
				{
					bf.append(getUserXml(userAr[i]));
				}
				bf.append("</Users>").append("\r\n");
			}
			
			Org [] orgAr = orgMgr.getNextChildOrgs(org.getOrgID());
			if(orgAr!=null)
			{
				for(int i=0;i<orgAr.length;i++)
				{
					bf.append(getOrgXml(orgAr[i]));
				}
				
			}
			bf.append("</Organization>").append("\r\n");
			return bf.toString();
		}

		private String getRoleXml(Role role) throws E5Exception
		{
			StringBuffer bf = new StringBuffer();
			bf.append("<Role DBID=\"").append(role.getRoleID()).append("\">").append("\r\n");
			bf.append("<RoleName>").append(role.getRoleName()).append("</RoleName>").append("\r\n");
			bf.append("<OrderID>").append(role.getOrderID()).append("</OrderID>").append("\r\n");
			User [] userAr =userMgr.getUsersByRole(role.getRoleID());
			if(userAr!=null)
			{
				bf.append("<GrantUsers>");
				for(int i=0;i<userAr.length;i++)
				{
					bf.append(getRoleUserXml(userAr[i],role.getRoleID()));
				}
				bf.append("</GrantUsers>").append("\r\n");
			}
			bf.append("</Role>").append("\r\n");
			return bf.toString();
		}
		private String getRoleUserXml(User user,int roleID) throws E5Exception
		{
			StringBuffer bf = new StringBuffer();
			UserRole userRole = roleMgr.getUserRole(user.getUserID(),roleID);
			bf.append("<GrantUser")
			.append(" refid=\"").append(user.getUserID()).append("\"")
			.append(" usercode=\"").append(user.getUserCode()).append("\"")
			.append(">").append("\r\n");
			bf.append(getRoleValid(userRole));
			bf.append("</GrantUser>\r\n");
			return bf.toString();
		}
		private String getRoleValid(UserRole userRole)
		{
			StringBuffer bf = new StringBuffer();
			bf.append("<TimeType>");
			bf.append(userRole.getTimeType());
			bf.append("</TimeType>");
			bf.append("<TimeValue>");
			bf.append(userRole.getTimeValue());
			bf.append("</TimeValue>");
			bf.append("<StartDate>");
			bf.append(userRole.getStartDate());
			bf.append("</StartDate>");
			bf.append("<StartTime>");
			bf.append(userRole.getStartTime());
			bf.append("</StartTime>");
			bf.append("<EndDate>");
			bf.append(userRole.getEndDate());
			bf.append("</EndDate>");
			bf.append("<EndTime>");
			bf.append(userRole.getEndTime());
			bf.append("</EndTime>");
			bf.append("<EndTime>");
			bf.append("</EndTime>\r\n");
			
			return bf.toString();
		}
		
		private String getUserXml(User user) throws E5Exception
		{
			StringBuffer bf = new StringBuffer();
			bf.append("<User DBID=\"").append(user.getUserID()).append("\">").append("\r\n");
			bf.append("<UserName>").append(user.getUserName()).append("</UserName>").append("\r\n");
			bf.append("<UserCode>").append(user.getUserCode()).append("</UserCode>").append("\r\n");
			bf.append("<OrderID>").append(user.getOrderID()).append("</OrderID>").append("\r\n");
			bf.append("<Address><![CDATA[").append(user.getAddress()).append("]]></Address>").append("\r\n");
			bf.append("<BPNumber><![CDATA[").append(user.getBPNumber()).append("]]></BPNumber>").append("\r\n");
			bf.append("<HandSetNumber><![CDATA[").append(user.getHandSetNumber()).append("]]></HandSetNumber>").append("\r\n");
			bf.append("<EmailAddress><![CDATA[").append(user.getEmailAddress()).append("]]></EmailAddress>").append("\r\n");
			bf.append("<PostCode><![CDATA[").append(user.getPostCode()).append("]]></PostCode>").append("\r\n");
			bf.append("<TelHomeNumber><![CDATA[").append(user.getTelHomeNumber()).append("]]></TelHomeNumber>").append("\r\n");
			bf.append("<TelOffNumber><![CDATA[").append(user.getTelOffNumber()).append("]]></TelOffNumber>").append("\r\n");
			bf.append("</User>").append("\r\n");
			return bf.toString();
		}
	
	public String saveNode(InputStream in,int orgID)
	{
		StringBuffer bf=new StringBuffer();
		DOMParser parser = new DOMParser();
		try
		{
			parser.parse(new InputSource(in));
			
			Document doc = parser.getDocument();
			Element root = doc.getDocumentElement();
			//2) 取得所有分类节点
			NodeList orglist = root.getChildNodes();
			RestoreOrgBean org = new RestoreOrgBean();
			org.setOrgID(orgID);
			for(int i=0;i<orglist.getLength();i++)
			{
				Node node = orglist.item(i);
				String name = node.getNodeName();
				if(name.equals("Organization"))
				{
					NodeList childList = node.getChildNodes();
					setNodes(org,childList);
				}
			}			
			bf.append(saveNode(org));
			bf.append(saveRoleUserNode(org.getOrgList()));
			//2009-3-27 Gong Lijie 有可能用户在角色之后加载，所以需最后才设置角色用户
			bf.append(saveUsersOfRoleNode(org.getOrgList()));
		}
		catch(Exception ex)
		{
			logger.error("invoke saveNode happen Exception:",ex);
			bf.append("import organization happen exception ,please view the logs");
			
		}
		return bf.toString();
	}
	private void setNodes(RestoreOrgBean rorgBean,NodeList treelist) throws Exception
	{

		if(treelist==null)
		{
			return ;
		}
		RestoreOrgBean org = new RestoreOrgBean();
		org.setCode("");
		rorgBean.getOrgList().add(org);
		
		for(int i=0;i<treelist.getLength();i++)
		{
			Node node = treelist.item(i);
			String name = node.getNodeName();
			if(name.equals("#text"))
			{
				continue;
			}
			String value = null;
			//取完所有当前属性 break
			if(name.equals("Roles"))
			{
				NodeList roleList = ((Element)node).getElementsByTagName("Role");
				setRoles(org.getRoleList(),roleList);
			}
			if(name.equals("Users"))
			{
				NodeList userList = ((Element)node).getElementsByTagName("User");
				setUsers(org.getUserList(),userList);
			}

			//恢复机构节点				
			if(name.equals("Organization"))
			{
				NodeList childList = node.getChildNodes();
				setNodes(org,childList);
			}
			else
			{
				try
				{
					Text tagText = (Text)node.getFirstChild();
					if(tagText!=null)
					{
						value = tagText.getNodeValue();
						setOrgValue(org,name,value);
					}
				}
				catch(Exception ex){}
			}

		}		
	}
	
	private void setRoles(List rolelist,NodeList treelist) throws Exception
	{
		if(treelist==null)
		{
			return ;
		}
		for(int i=0;i<treelist.getLength();i++)
		{
			Element e = (Element)treelist.item(i);
			NodeList orgProperty = e.getChildNodes();
			//3) 设置分类属性
			RestoreRoleBean role = new RestoreRoleBean();
			for(int j=0;j<orgProperty.getLength();j++)
			{
				Node node = orgProperty.item(j);
				String name = node.getNodeName();
				if(name.equals("#text"))
				{
					continue;
				}
			
				if(name.equals("GrantUsers"))
				{
					NodeList userList = ((Element)node).getElementsByTagName("GrantUser");
					setGrantUsers(role.getUserCodeList(),userList);
				}
				else
				{
					try
					{
						Text tagText = (Text)node.getFirstChild();
						if(tagText!=null)
						{
							String value = tagText.getNodeValue();
						//取完所有当前属性 break
							if(name.equals("RoleName"))
							{
								role.setRoleName(value);
							}
						}
					}
					catch(Exception ex){}
				}
			}
			rolelist.add(role);
		}
	}
	
	private void setGrantUsers(List userlist,NodeList treelist) throws Exception
	{
		for(int i=0;i<treelist.getLength();i++)
		{
			Element e = (Element)treelist.item(i);
			String usercode=e.getAttribute("usercode");
			UserRole userRole = new UserRole();
			userRole.setUserCode(usercode);
			NodeList roleProperty = e.getChildNodes();
			for(int j=0;j<roleProperty.getLength();j++)
			{
				Node node = roleProperty.item(j);
				String name = node.getNodeName();
				if(name.equals("#text"))
				{
					continue;
				}
				try
				{
					Text tagText = (Text)node.getFirstChild();
					if(tagText!=null)
					{
						String value = tagText.getNodeValue();
						setUserRoleValue(userRole,name,value);
					}
				}
				catch(Exception ex){}
			}
			
			userlist.add(userRole);
		}

	}
	
	private void setUsers(List userlist,NodeList treelist) throws Exception
	{
		if(treelist==null)
		{
			return ;
		}
		for(int i=0;i<treelist.getLength();i++)
		{
			Element e = (Element)treelist.item(i);
			NodeList orgProperty = e.getChildNodes();
			//3) 设置分类属性
			User user = new User();
			for(int j=0;j<orgProperty.getLength();j++)
			{
				Node node = orgProperty.item(j);
				String name = node.getNodeName();
				if(name.equals("#text"))
				{
					continue;
				}
				try
				{
					Text tagText = (Text)node.getFirstChild();
					if(tagText!=null)
					{
						String value = tagText.getNodeValue();
					//取完所有当前属性 break
						value = tagText.getNodeValue();
						try
						{
							setUserValue(user,name,value);
						}
						catch(Exception ex){}
					}
				}
				catch(Exception ex){}
				
			}
			userlist.add(user);
		}

	}
	private String saveNode(RestoreOrgBean preNode)throws Exception
	{
		StringBuffer bf=new StringBuffer();
		List orgList = preNode.getOrgList();
		for(int i=0;i<orgList.size();i++)
		{
			RestoreOrgBean rorgbean =(RestoreOrgBean)orgList.get(i);
			rorgbean.setParentID(preNode.getOrgID());
			try
			{
				bf.append("Add new organization:").append(rorgbean.getName());
				bf.append("<br>");
				Org newOrg = rorgbean.getOrg();
				orgMgr.create(newOrg);
				
				rorgbean.setOrgID(newOrg.getOrgID());
				bf.append(saveNode(rorgbean));
			}
			catch(E5Exception ex)
			{
				logger.error("[saveNode]",ex);
				bf.append("Failed! Exception:").append(ex.getMessage());
			}

		}
		return bf.toString();		
	}
	private String saveRoleUserNode(List orgList)throws Exception
	{
		StringBuffer bf=new StringBuffer();
		for(int i=0;i<orgList.size();i++)
		{
//恢复角色内容			
			RestoreOrgBean rorgbean =(RestoreOrgBean)orgList.get(i);
			if(!isValidOrgID(rorgbean.getOrgID()))
			{
				bf.append("[add user or Role]Failed!No org:").append(rorgbean.getName()).append(".<br>");
				continue;
			}
			List roleList = rorgbean.getRoleList();
			bf.append(saveRole(roleList,rorgbean.getOrgID()));
			
//恢复用户内容			
			List userList = rorgbean.getUserList();
			bf.append(saveUser(userList,rorgbean.getOrgID()));
//恢复用户角色关系
//			bf.append(saveRoleUser(roleList,rorgbean.getOrgID()));
//恢复子机构			
			bf.append(saveRoleUserNode(rorgbean.getOrgList()));
		}
		return bf.toString();
	}
	//单独加载角色用户。因为有可能用户在角色之后加载，所以需最后才设置角色用户
	private String saveUsersOfRoleNode(List orgList)throws Exception
	{
		StringBuffer bf=new StringBuffer();
		for(int i=0;i<orgList.size();i++)
		{
			RestoreOrgBean rorgbean =(RestoreOrgBean)orgList.get(i);
			if(!isValidOrgID(rorgbean.getOrgID()))
			{
				bf.append("[add users of role]Failed!No org:").append(rorgbean.getName()).append(". <br>");
				continue;
			}
			List roleList = rorgbean.getRoleList();
			//加载角色用户关系
			bf.append(saveRoleUser(roleList,rorgbean.getOrgID()));
			//递归子层
			bf.append(saveUsersOfRoleNode(rorgbean.getOrgList()));
		}
		return bf.toString();
	}
	
	private String saveRole(List roleList,int orgid)throws Exception
	{
		StringBuffer bf=new StringBuffer();
		for(int j=0;j<roleList.size();j++)
		{
			Role role= (Role)roleList.get(j);
			try
			{
				bf.append("Add role:").append(role.getRoleName());
				int roleid = roleMgr.create(orgid,role.getRoleName());
				role.setRoleID(roleid);
			}
			catch(E5Exception ex)
			{
				logger.error("[saveRoleUserNode]",ex);
				bf.append("Failed! Exception:").append(ex.getMessage());
			}
			bf.append("<br>");
			
		}
		return bf.toString();
		
	}
	
	private String saveUser(List userList,int orgid)throws Exception
	{
		StringBuffer bf=new StringBuffer();
		for(int j=0;j<userList.size();j++)
		{
			User user= (User)userList.get(j);
			try
			{
				bf.append("Add user:").append(user.getUserName());
				user.setOrgID(orgid);
				userMgr.create(user);
			}
			catch(E5Exception ex)
			{
				logger.error("[saveRoleUserNode]",ex);
				bf.append("Failed! Exception:").append(ex.getMessage());
			}
			bf.append("<br>");
			
		}
		return bf.toString();
		
	}
	
	private String saveRoleUser(List roleList,int orgid)throws Exception
	{
		StringBuffer bf=new StringBuffer();
		for(int j=0;j<roleList.size();j++)
		{
			RestoreRoleBean role= (RestoreRoleBean)roleList.get(j);
			List usercodeList = role.getUserCodeList();
			if(!isValidRoleID(role.getRoleID()))
			{
				bf.append(" Failed to add user.No role found!<br>");
				continue;
			}
			if(usercodeList==null)
			{
				continue;
			}
			for(int k=0;k<usercodeList.size();k++)
			{
				UserRole userRole = (UserRole)usercodeList.get(k);
				try
				{
					bf.append("Grant Role [").append(role.getRoleName())
						.append("] to User:").append(userRole.getUserCode());

					User user = userMgr.getUserByCode(userRole.getUserCode());
					if(user!=null)
					{
						userRole.setRoleID(role.getRoleID());
						userRole.setRoleName(role.getRoleName());
						userRole.setUserID(user.getUserID());
						roleMgr.grantRole(user.getUserID(),userRole);
					}
					else {
						bf.append("--User not found!");
					}
				}
				catch(E5Exception ex)
				{
					logger.error("[saveRoleUserNode]",ex);
					bf.append("Failed! Exception:" + ex.getLocalizedMessage());
				}
				bf.append("<br>");
			}
		}
		return bf.toString();
	}
	
	
	private boolean isValidOrgID(int orgid) throws E5Exception
	{
		Org org = orgMgr.get(orgid);
		if (org == null) {
			return false;
		}
		return true;
	}

	private boolean isValidRoleID(int roleid) throws E5Exception
	{
		Role role=roleMgr.get(roleid);
		if(role==null)
		{
			return false;
		}
		return true;
	}
	
	private void setOrgValue(Org org,String name,String value)
	{
		if (value == null || value.equals("null")) return;

		String method = "set" + name;
		try
		{
			Method setValue = getMethodByName(Class.forName("com.founder.e5.sys.org.Org"), method);
			if (setValue == null) return;
			
	        Object[] args = null;
			if(name.equalsIgnoreCase("orderID") || name.equalsIgnoreCase("type") ||name.equalsIgnoreCase("parentID"))
			{
				args = new Object[]{new Integer(value)};
			}
			else
			{
				args = new Object[]{value};
			}
	        setValue.invoke(org,args);
		}
		catch(Exception ex)
		{
			logger.error("invoke setOrgValue happen exception:" + method, ex);
		}
	}
	
	private void setUserRoleValue(UserRole userRole,String name,String value)
	{
		if (value.equals("null")) return;
		
		String method = "set" + name;
		try
		{
			Method setValue = getMethodByName(
					Class.forName("com.founder.e5.sys.org.UserRole"), method);
			if (setValue == null)
				return;

			Object[] args = null;
			if (name.equalsIgnoreCase("timeType")
					|| name.equalsIgnoreCase("timeValue")
					|| name.equalsIgnoreCase("userID")
					|| name.equalsIgnoreCase("roleID"))
			{
				args = new Object[]{ new Integer(value) };
			} 
			else
				args = new Object[]{ value };

			setValue.invoke(userRole, args);
		} catch (Exception ex)
		{
			logger.error("Error when set userRole value!", ex);
		}
		
	}
	private void setUserValue(User user,String name,String value)
	{
		if (value.equals("null"))
			return;

		String method = "set" + name;
		try
		{
			Method setValue = getMethodByName(
					Class.forName("com.founder.e5.sys.org.User"), method);
			if (setValue == null)
				return;

			Object[] args = null;
			if (name.equalsIgnoreCase("orderID") 
					|| name.equalsIgnoreCase("orgID")
					|| name.equalsIgnoreCase("userID"))
			{
				args = new Object[] { new Integer(value) };
			} 
			else
				args = new Object[] { value };
			
			setValue.invoke(user, args);
		} catch (Exception ex)
		{
			logger.error("Error when set user value!", ex);
		}
	}
	
	
    public Method getMethodByName(Class obj, String methodName)
    {
        Method retVal = null;
        Method[] methods = obj.getDeclaredMethods();
        for (int i = 0; i < methods.length; i ++)
        {
            if (methods[i].getName().equals(methodName))
            {
                retVal = methods[i];
                break;
            }
        }
        return retVal;
    }
	
	/**
	 * @param logger The logger to set.
	 */
	public void setLogger(Log logger) {
		this.logger = logger;
	}

	/**
	 * @param orgMgr The orgMgr to set.
	 */
	public void setOrgMgr(OrgManager orgMgr) {
		this.orgMgr = orgMgr;
	}

	/**
	 * @param roleMgr The roleMgr to set.
	 */
	public void setRoleMgr(RoleManager roleMgr) {
		this.roleMgr = roleMgr;
	}

	/**
	 * @param userMgr The userMgr to set.
	 */
	public void setUserMgr(UserManager userMgr) {
		this.userMgr = userMgr;
	}

}
