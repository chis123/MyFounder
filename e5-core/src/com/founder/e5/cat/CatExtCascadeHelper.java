package com.founder.e5.cat;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.founder.e5.context.E5Exception;

/**
 * ���������ڲ�������
 * @author wanghc
 * @created 2006-3-13
 * @version 1.0
 */

class CatExtCascadeHelper
{
	/**
	 * �½�����ʱ�����ü�������
	 * @param current
	 */
	static public void createExtCascade(CatExt current)
		throws E5Exception
	{		
		int catType= current.getCatType();		
		
		CatExt parent = CatHelper.getExtManager().getExt(catType,current.getParentID(),current.getExtType());
		
		if(parent !=null)
		{
			if(parent.getCascadeName()==null) parent.setCascadeName("");
			current.setCascadeName(parent.getCascadeName()+Category.separator+current.getExtName());
		}
		else
			current.setCascadeName(current.getExtName());
	}
	
	/**
	 * �޸ı���ʱ�����¼�������
	 * @param src
	 * @param dest
	 */
	static public void updateExtCascade(CatExt src,CatExt[] olds,Connection conn)
		throws E5Exception
	{
		boolean changed = false;		
		int catID       = src.getCatID();
		int catType     = src.getCatType();
		int extType     = src.getExtType();
		//��ʷ��������
		String oname = null;
		
		//1)�Ƿ��޸��˱���ȡֵ
		for(int i=0;i<olds.length;i++)
		{
			if(src.getCatID()==olds[i].getCatID() && src.getExtType()==olds[i].getExtType())
			{
				//˵��:
				//1.�޸ı���ȡֵʱ���ã�2.�ƶ��ڵ�ʱ����
				//�޸���ȡֵ���޸��˼�������
				//�ƶ��ڵ�ʱ��ͬ��update���ø÷�������ʱsrc��olds��cascadeName����������ֵ��ʱ
				//�������ǿ���Ϊnull,������Ҫ��������Ϊ""���бȽ�
				
				if(src.getExtName()==null) src.setExtName("");
				if(src.getCascadeName() == null) src.setCascadeName("");
				if(olds[i].getExtName()==null) olds[i].setExtName("");
				if(olds[i].getCascadeName() == null) olds[i].setCascadeName("");	
		
				if(!src.getExtName().equals(olds[i].getExtName())
						|| !src.getCascadeName().equals(olds[i].getCascadeName()))
				{
					changed = true;
					oname   = olds[i].getCascadeName();					
				}
				//δ�޸ģ�����src����updateʱ����
				else
					src.setCascadeName(olds[i].getCascadeName());
				
				break;
			}
		}		
		//@TODO My be load object from db.
		if(!changed)
			return;
		
		createExtCascade(src);
		
		//��ǰ��������
		String name = src.getCascadeName();
			
		//2)�޸ĸ�����
		Category subs[] = CatHelper.getCatManager().getChildrenCatsIncludeDeleted(catType,catID,extType);
		
		//û����
		if(subs==null)
			return;
		
		int catIDs[] = new int[subs.length];
		String cascadeName[] = new String[subs.length];
		
		//oname ==null ��ʾ ""
		if(oname == null)
			oname = "";
		
		int onlen = oname.length();
		
		//ѭ��������
		for(int i=0;i<subs.length;i++)
		{
			catIDs[i]      = subs[i].getCatID();			
			cascadeName[i] = name+subs[i].getCascadeName().substring(onlen,subs[i].getCascadeName().length());	
		}
		
		updateExtCascade(extType,catIDs,cascadeName,conn);
		
	}
	
	private static final String UPDATE_CASCADE_NAME_SQL = "UPDATE CATEGORY_EXT set EXT_CASCADE_NAME=? WHERE ENTRY_ID=? AND EXT_TYPE=?";
	
	/**
	 * �������·�������ļ������ơ�����ID,������Ϊ���úõĶ�Ӧ��ϵ����ȡֵ��
	 * @param extType 
	 * @param catID
	 * @param cascadeName
	 * @param conn
	 */
	private static void updateExtCascade(int extType,int catID[],String cascadeName[],Connection conn)
		throws E5Exception
	{		
		PreparedStatement pstmt = null;
		try
		{
			
			pstmt = conn.prepareStatement(UPDATE_CASCADE_NAME_SQL);
			
			for(int i=0;i<cascadeName.length;i++)
			{
				pstmt.setString(1,cascadeName[i]);			
				pstmt.setInt(2,catID[i]);
				pstmt.setInt(3,extType);				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		}
		catch(Exception e)
		{
			throw new E5Exception("[Update Cat Ext Cascade]"+e.getMessage());
		}
		finally
		{
			try{pstmt.close();}catch(Exception e){};
			pstmt=null;
		}
	}
	
	/**
	 * ���������ӽڵ㼶������,�ƶ�����ڵ�ʱ����
	 * @param catID
	 * @param catType
	 * @param conn
	 */
	static public void updateExtChildCascade(int catType,int catID,int destCatID,Connection conn)
		throws E5Exception
	{
		CatExtType[] types = CatHelper.getExtManager().getExtTypes(catType);
		
		if(types == null) return;
		
		CatExt[] exts = new CatExt[types.length];
		
		//ÿ����������update һ��
		for(int i=0;i<types.length;i++)
		{			
			CatExt ext = CatHelper.getExtManager().getExt(catType,catID,types[i].getType());
			ext.setParentID(destCatID);
			createExtCascade(ext);
			exts[i] = ext;			
		}
		
		CatHelper.getExtManager().saveExt(catType,catID,exts);		
	}
	
	private static final String INSERT_CATEXT_SQL = "insert into CATEGORY_EXT(ENTRY_ID,EXT_TYPE,PARENT_ID,WT_TYPE,EXT_NAME,EXT_CASCADE_NAME) values (?,?,?,?,?,?)";
	
	/**
	 * �½�����ʱ,�������з��������µķ���ı���
	 * ���ݷ������ڵĲ������~~~~
	 * @param type
	 * @param conn
	 */
	static public void createExtType(CatExtType type,Connection conn)
		throws E5Exception
	{		
		int catType = type.getCatType();
		int extType = type.getType();
		//@TODO
		//1) ȡ�����з������͵Ľڵ�
		Category cat[] = CatHelper.getCatManager().getChildrenCatsIncludeDeleted(catType,0,0);
		
		if(cat == null || cat.length == 0 ) return;
		
		//2) ���ݲ������~
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(INSERT_CATEXT_SQL);
			
			for(int i=0;i<cat.length;i++)
			{
				pstmt.setInt(1,cat[i].getCatID());
				pstmt.setInt(2,extType);
				pstmt.setInt(3,cat[i].getParentID());
				pstmt.setInt(4,catType);
				pstmt.setString(5,"");
				pstmt.setString(6,getCascadeName(cat[i].getCatLevel()));
				
				pstmt.addBatch();
			}
			
			//3) ���������¼	
			pstmt.executeBatch();
			
		}
		catch(Exception e)
		{
			throw new E5Exception(e);
		}
		finally
		{
			try{pstmt.close();}catch(Exception e){}
			pstmt = null;
		}		
			
	}
	
	private static String getCascadeName(int level)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<level;i++)
		{
			sb.append(Category.separator);
		}		
		return sb.toString();
	}
	
}
