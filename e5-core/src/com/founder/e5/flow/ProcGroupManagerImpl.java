package com.founder.e5.flow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;

public class ProcGroupManagerImpl implements ProcGroupManager {

	private final String SQL_GROUP_SELECT = "select * from E5FLOW_PROCGROUP where DOCTYPEID=? and FLOWNODEID=? order by GROUPNO,PROCORDER";
	private final String SQL_GROUP_DELETE = "delete from E5FLOW_PROCGROUP where DOCTYPEID=? and FLOWNODEID=?";
	private final String SQL_GROUP_INSERT = "insert into E5FLOW_PROCGROUP(DOCTYPEID,FLOWNODEID,PROCID,GROUPNO,PROCORDER) values(?,?,?,?,?)";

	private final String SQL_ALL_SELECT = "select * from E5FLOW_PROCGROUP order by DOCTYPEID,FLOWNODEID,GROUPNO,PROCORDER";
	
	public void reset(int docTypeID, int flowNodeID, ProcGroup[] groups)
	throws E5Exception 
	{
		DBSession session = Context.getDBSession();
		try
		{
			session.beginTransaction();
			//先清除原来的操作组
			session.executeUpdate(SQL_GROUP_DELETE, new Object[]{new Integer(docTypeID), new Integer(flowNodeID)} );
			
			//增加组，顺序就是数组的顺序
			for (int i = 0; i < groups.length; i++)
			{
				if (groups[i] == null) continue;
				List list = groups[i].getList();
				
				//一个一个增加组内的操作，顺序就是列表的顺序
				for (int j = 0; j < list.size(); j++) {
					ProcOrder proc = (ProcOrder)list.get(j);

					session.executeUpdate(SQL_GROUP_INSERT, new Object[]{new Integer(docTypeID), 
							new Integer(flowNodeID), new Integer(proc.getProcID()),
							//组号：自动取数组顺序，从0开始；操作顺序：自动取列表内的顺序，从0开始
							new Integer(i), new Integer(j),
							} );
				}
			}
			session.commitTransaction();
		}
		catch (Exception e)
		{
			try{session.rollbackTransaction();}catch(Exception ex){}
			throw new E5Exception("[FLOW.PROCGROUP]Error when save proc groups!", e);
		}
		finally
		{
			session.closeQuietly();
		}
	}

	public ProcGroup[] getProcGroups(int docTypeID, int flowNodeID)
			throws E5Exception {
		DBSession sess = null;

		List list = new ArrayList();
		try
		{
			sess = Context.getDBSession();
			IResultSet rs = sess.executeQuery( SQL_GROUP_SELECT, new Object[]{new Integer(docTypeID), new Integer(flowNodeID)} );
			int oldGroup = -1;
			ProcGroup group = null;
			while ( rs.next() )
			{
				ProcOrder proc = getProc(rs);

				int groupID = rs.getInt("GROUPNO");

				//组号不同，则新建一个组
				if (groupID != oldGroup)
				{
					group = new ProcGroup();
					group.setGroupNo(groupID); //加上组序号
					list.add(group);//加入列表
					
					oldGroup = groupID;
				}
				group.addProc(proc);
			}
		}
		catch ( SQLException e )
		{
			throw new E5Exception("[FLOW.PROCGROUP]Error when get proc groups!", e);
		}
		finally
		{
			if (sess != null) sess.closeQuietly();
		}

		return (ProcGroup[])list.toArray(new ProcGroup[0]);
	}
	//一个操作对象
	private ProcOrder getProc(IResultSet rs) throws SQLException
	{
		ProcOrder proc = new ProcOrder();
		proc.setDocTypeID(rs.getInt("DOCTYPEID"));
		proc.setFlowNodeID(rs.getInt("FLOWNODEID"));
		proc.setOrder(rs.getInt("PROCORDER"));
		proc.setProcID(rs.getInt("PROCID"));
		
		return proc;
	}
	public ProcGroup[] getAllGroups() throws E5Exception 
	{
		DBSession sess = null;
		
		List list = new ArrayList();
		try
		{
			sess = Context.getDBSession();
			IResultSet rs = sess.executeQuery( SQL_ALL_SELECT);
			int oldGroup = -1, oldDocTypeID = -1, oldFlowNodeID = -1;
			ProcGroup group = null;
			while ( rs.next() )
			{
				ProcOrder proc = getProc(rs);
		
				int groupID = rs.getInt("GROUPNO");
				int docTypeID = rs.getInt("DOCTYPEID");
				int flowNodeID = rs.getInt("FLOWNODEID");
		
				//组号不同，则新建一个组
				if (docTypeID != oldDocTypeID || flowNodeID != oldFlowNodeID || groupID != oldGroup)
				{
					group = new ProcGroup();
					group.setDocTypeID(docTypeID);
					group.setFlowNodeID(flowNodeID);
					group.setGroupNo(groupID); //加上组序号
					
					list.add(group);//加入列表
					
					oldGroup = groupID;
					oldDocTypeID = docTypeID;
					oldFlowNodeID = flowNodeID;
				}
				group.addProc(proc);
			}
		}
		catch ( SQLException e )
		{
			throw new E5Exception("[FLOW.PROCGROUP]Error when get all proc groups!", e);
		}
		finally
		{
			if (sess != null) sess.closeQuietly();
		}
		
		return (ProcGroup[])list.toArray(new ProcGroup[0]);
	}
}
