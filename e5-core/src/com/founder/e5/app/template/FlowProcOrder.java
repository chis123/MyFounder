package com.founder.e5.app.template;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Node;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.DocType;
import com.founder.e5.dom.DocTypeManager;
import com.founder.e5.flow.Flow;
import com.founder.e5.flow.FlowManager;
import com.founder.e5.flow.FlowNode;
import com.founder.e5.flow.Proc;
import com.founder.e5.flow.ProcFlow;
import com.founder.e5.flow.ProcGroup;
import com.founder.e5.flow.ProcGroupManager;
import com.founder.e5.flow.ProcManager;
import com.founder.e5.flow.ProcOrder;
import com.founder.e5.flow.ProcOrderManager;
import com.founder.e5.flow.ProcUnflow;

/**
 * �������򡢲������ģ�嵼�롢����
 * @created 2009-2-18
 * @author Gong Lijie
 * @version 1.0
 */
public class FlowProcOrder {
	Log log = Context.getLog("e5.sys");

	DocTypeManager docTypeManager = (DocTypeManager)Context.getBean(DocTypeManager.class);
	FlowManager flowManager = (FlowManager)Context.getBean(FlowManager.class);
	ProcManager procManager = (ProcManager)Context.getBean(ProcManager.class);
	ProcOrderManager procOrderManager = (ProcOrderManager)Context.getBean(ProcOrderManager.class);
	ProcGroupManager procGroupManager = (ProcGroupManager)Context.getBean(ProcGroupManager.class);

	/* -----------------------------------------------------
	 * �������򡢲�����ĵ���
	 * -----------------------------------------------------*/
	/**
	 * ������������ģ��
	 * @param appID ��ϵͳID
	 * @return
	 * @throws Exception
	 */
	public String exportProcOrders(int appID){
		try {
			DocType[] docTypes = docTypeManager.getTypes(appID);
			if (docTypes == null || docTypes.length == 0) return "";

			StringBuffer result = new StringBuffer(3000);
			result.append("<PROCORDERS>");
			for (int i = 0; i < docTypes.length; i++) {
				result.append(getByDocType(docTypes[i], false));
			}
			result.append("</PROCORDERS>");
			
			return result.toString();
		} catch (Exception e) {
			log.error("Exception for exporting process orders.", e);
			return "";
		}
	}
	/**
	 * ����������ģ��
	 * @param appID ��ϵͳID
	 * @return
	 * @throws Exception
	 */
	public String exportProcGroups(int appID){
		
		try {
			DocType[] docTypes = docTypeManager.getTypes(appID);
			if (docTypes == null || docTypes.length == 0) return "";

			StringBuffer result = new StringBuffer(3000);
			result.append("<PROCGROUPS>");
			for (int i = 0; i < docTypes.length; i++) {
				result.append(getByDocType(docTypes[i], true));
			}
			result.append("</PROCGROUPS>");
			
			return result.toString();
		} catch (Exception e) {
			log.error("Exception for exporting process groups.", e);
			return "";
		}
	}
	//ȡһ���ĵ����͵Ĳ�������/������
	private String getByDocType(DocType docType, boolean isGroup)  throws Exception
	{
		//���ĵ����͵����з����̲���һ����ȡ����������ÿ�β�ѯ���ݿ�
		ProcUnflow[] unflowProcs = procManager.getUnflows(docType.getDocTypeID());
		
		StringBuffer seg = new StringBuffer(2000);
		//ÿ�����̵Ĳ�������
		Flow[] flows = flowManager.getFlows(docType.getDocTypeID());
		if (flows != null ) {
			for (int i = 0; i < flows.length; i++) {
				seg.append(getByFlow(flows[i], unflowProcs, isGroup));
			}
		}
		//�����̲�������/������
		String unflowSeg = isGroup ? getUnflowGroups(docType.getDocTypeID(), unflowProcs)
					: getUnflowOrders(docType.getDocTypeID(), unflowProcs);
		if (!StringUtils.isBlank(unflowSeg)) {
			seg.append("<UNFLOW>").append(unflowSeg).append("</UNFLOW>");
		}
		if (seg.length() == 0) return "";
		
		StringBuffer result = new StringBuffer(2000);
		result.append("<DOCTYPE NAME=\"").append(docType.getDocTypeName()).append("\">");
		result.append(seg);
		result.append("</DOCTYPE>");
		
		return result.toString();
	}
	//ȡһ�����̵Ĳ�������/������
	private String getByFlow(Flow flow, ProcUnflow[] unflowProcs, boolean isGroup) throws Exception
	{
		FlowNode[] flowNodes = flowManager.getFlowNodes(flow.getID());
		if (flowNodes == null || flowNodes.length == 0) return "";
		
		StringBuffer seg = new StringBuffer(400);
		for (int i = 0; i < flowNodes.length; i++) {
			if (!isGroup)
				seg.append(getOrderByFlowNode(flow.getDocTypeID(), flowNodes[i], unflowProcs));
			else
				seg.append(getGroupByFlowNode(flow.getDocTypeID(), flowNodes[i], unflowProcs));
		}
		if (seg.length() == 0) return "";
		
		StringBuffer result = new StringBuffer(1000);
		result.append("<FLOW NAME=\"").append(flow.getName()).append("\">");
		result.append(seg);
		result.append("</FLOW>");
		
		return result.toString();
	}
	//ȡһ�����̽ڵ��µĲ�������
	private String getOrderByFlowNode(int docTypeID, FlowNode flowNode, ProcUnflow[] unflowProcs) throws Exception
	{
		ProcOrder[] orders = procOrderManager.getProcOrders(docTypeID, flowNode.getID());
		if (orders == null || orders.length == 0) return "";

		//�����̽ڵ���������̲���һ����ȡ����������ÿ�β�ѯ���ݿ�
		ProcFlow[] flowProcs = procManager.getProcs(flowNode.getID());
		
		StringBuffer result = new StringBuffer(400);
		result.append("<NODE NAME=\"").append(flowNode.getName()).append("\">");
		for (int i = 0; i < orders.length; i++) {
			result.append(getOneProc(orders[i].getProcID(), flowProcs, unflowProcs));
		}
		result.append("</NODE>");
		
		return result.toString();
	}
	//ȡһ�����̽ڵ��µĲ�����
	private String getGroupByFlowNode(int docTypeID, FlowNode flowNode, ProcUnflow[] unflowProcs) throws Exception
	{
		ProcGroup[] groups = procGroupManager.getProcGroups(docTypeID, flowNode.getID());
		if (groups == null || groups.length == 0) return "";
		
		//�����̽ڵ���������̲���һ����ȡ����������ÿ�β�ѯ���ݿ�
		ProcFlow[] flowProcs = procManager.getProcs(flowNode.getID());
		
		StringBuffer result = new StringBuffer(400);
		result.append("<NODE NAME=\"").append(flowNode.getName()).append("\">");
		for (int i = 0; i < groups.length; i++) {
			result.append("<PROCGROUP>");
			List procList = groups[i].getList();
			for (int j = 0; j < procList.size(); j++) {
				result.append(getOneProc(((ProcOrder)(procList.get(j))).getProcID(), flowProcs, unflowProcs));
			}
			result.append("</PROCGROUP>");
		}
		result.append("</NODE>");
		
		return result.toString();
	}
	//ȡһ���ĵ����͵ķ����̲�������
	private String getUnflowOrders(int docTypeID, ProcUnflow[] unflowProcs) throws Exception
	{
		ProcOrder[] orders = procOrderManager.getProcOrders(docTypeID, 0);
		if (orders == null || orders.length == 0) return "";
		
		StringBuffer result = new StringBuffer(200);
		for (int i = 0; i < orders.length; i++) {
			result.append(getOneProc(orders[i].getProcID(), null, unflowProcs));
		}
		return result.toString();
	}
	//ȡһ���ĵ����͵ķ����̲�����
	private String getUnflowGroups(int docTypeID, ProcUnflow[] unflowProcs) throws Exception
	{
		ProcGroup[] groups = procGroupManager.getProcGroups(docTypeID, 0);
		if (groups == null || groups.length == 0) return "";
		
		StringBuffer result = new StringBuffer(400);
		for (int i = 0; i < groups.length; i++) {
			result.append("<PROCGROUP>");
			List procList = groups[i].getList();
			for (int j = 0; j < procList.size(); j++) {
				result.append(getOneProc(((ProcOrder)(procList.get(j))).getProcID(), null, unflowProcs));
			}
			result.append("</PROCGROUP>");
		}
		return result.toString();
	}
	private String getOneProc(int procID, ProcFlow[] flowProcs, ProcUnflow[] unflowProcs) {
		Proc proc = getProc(procID, flowProcs, unflowProcs);
		if (proc == null) return "";
		
		StringBuffer result = new StringBuffer(50);
		result.append("<PROC NAME=\"").append(proc.getProcName()).append("\"");
		if (proc.getProcType() == Proc.PROC_UNFLOW) 
			result.append(" TYPE=\"UNFLOW\"/>");
		else
			result.append("/>");
		return result.toString();
	}
	private Proc getProc(int procID, ProcFlow[] flowProcs, ProcUnflow[] unflowProcs) {
		if (flowProcs != null) {
			for (int i = 0; i < flowProcs.length; i++)
				if (flowProcs[i].getProcID() == procID) return flowProcs[i];
		}
		if (unflowProcs != null) {
			for (int i = 0; i < unflowProcs.length; i++)
				if (unflowProcs[i].getProcID() == procID) return unflowProcs[i];
		}
		return null;
	}
	
	/* -----------------------------------------------------
	 * �������򡢲�����ĵ���
	 * -----------------------------------------------------*/
	/**
	 * �����������ģ��
	 * @param doc
	 * @return
	 */
	public boolean importProcOrders(Node doc)
	{
		List nodeList = doc.selectNodes("//PROCORDERS/DOCTYPE");
		if(nodeList == null) return true;
		
		return importAll(nodeList, false);
	}
	public boolean importProcGroups(Node doc)
	{
		List nodeList = doc.selectNodes("//PROCGROUPS/DOCTYPE");
		if(nodeList == null) return true;
		
		return importAll(nodeList, true);
	}
	
	private boolean importAll(List nodeList, boolean isGroup)
	{
		//���ĵ������������
		boolean result = true;
		for (int i = 0; i < nodeList.size(); i++) {
			Node docNode = (Node)nodeList.get(i);
			String docTypeName = getAttributeValue(docNode, "@NAME");
			try {
				DocType docType = docTypeManager.get(docTypeName);
				if (docType == null) {
					log.error("[FlowTemplate.importProcOrder]Cannot find the doctype name:" + docTypeName);
					result = false;
					continue;
				}
				setByDocType(docType, docNode, isGroup);
			} catch (E5Exception ex) {
				log.error("[FlowTemplate.importProcOrder]", ex);
				result = false;
			}
		}
		return result;
	}
	
	private boolean setByDocType(DocType docType, Node docNode, boolean isGroup) {
		boolean result = true;
		List nodeList = docNode.selectNodes("descendant::FLOW");
		try {
			//���ĵ����͵����з����̲���һ����ȡ����������ÿ�β�ѯ���ݿ�
			ProcUnflow[] unflowProcs = procManager.getUnflows(docType.getDocTypeID());

			//���̲���
			if (nodeList != null)
			{
				for (int i = 0; i < nodeList.size(); i++)
				{
					Node node = (Node)nodeList.get(i);
					String name = getAttributeValue(node, "@NAME");
					Flow flow = flowManager.getFlow(docType.getDocTypeID(), name);
					if (flow == null) {
						log.error("[FlowTemplate.importProcOrder]Cannot find the flow name:" + name);
						result = false;
						continue;
					}
					if (!setByFlow(flow, node, unflowProcs, isGroup))
						result = false;
				}
			}

			//�����̲����ļ���
			Node unflowNode = docNode.selectSingleNode("descendant::UNFLOW");
			if (unflowNode != null) {
				if (!isGroup)
					setUnflowOrders(docType.getDocTypeID(), unflowNode, unflowProcs);
				else
					setUnflowGroups(docType.getDocTypeID(), unflowNode, unflowProcs);
			}
		} catch(E5Exception ex){
			log.error("[FlowTemplate.ImportProcOrder]Error when import by docType.",ex);
			result = false;
		}
		return result;
	}
	//����һ�����̵Ĳ�������
	private boolean setByFlow(Flow flow, Node docNode, ProcUnflow[] unflowProcs, boolean isGroup) {
		List nodeList = docNode.selectNodes("descendant::NODE");
		if (nodeList == null || nodeList.size() == 0) return true;
		
		boolean result = true;
		try {
			for (int i = 0; i < nodeList.size(); i++)
			{
				Node node = (Node)nodeList.get(i);
				String name = getAttributeValue(node, "@NAME");
				FlowNode flowNode = flowManager.getFlowNode(flow.getID(), name);
				if (flowNode == null) {
					log.error("[FlowTemplate.importProcOrder]Cannot find the flownode name:" + name);
					result = false;
					continue;
				}
				if (isGroup) {
					if (!setGroupByFlowNode(flow.getDocTypeID(), flowNode, node, unflowProcs))
						result = false;
				}
				else{
					if (!setOrderByFlowNode(flow.getDocTypeID(), flowNode, node, unflowProcs))
						result = false;
				}
			}
		} catch(E5Exception ex){
			log.error("[FlowTemplate.ImportFlow]Error when import flow",ex);
			result = false;
		}
		return result;
	}
	//����һ�����̽ڵ�Ĳ�������
	private boolean setOrderByFlowNode(int docTypeID, FlowNode flowNode, Node docNode, ProcUnflow[] unflowProcs) {
		List nodeList = docNode.selectNodes("descendant::PROC");
		if (nodeList == null || nodeList.size() == 0) return true;

		boolean result = true;
		try {
			//�����̽ڵ���������̲���һ����ȡ����������ÿ�β�ѯ���ݿ�
			ProcFlow[] flowProcs = procManager.getProcs(flowNode.getID());

			List procList = new ArrayList(nodeList.size());
			for (int i = 0; i < nodeList.size(); i++)
			{
				//һ��PROC
				Node node = (Node)nodeList.get(i);
				String name = getAttributeValue(node, "@NAME");
				String type = getAttributeValue(node, "@TYPE");
				//ȡ��PROC���󣬼���LIST
				Proc proc = ("UNFLOW".equals(type)) ? getProc(name, null, unflowProcs)
						: getProc(name, flowProcs, null);
				if (proc != null) procList.add(proc);
			}
			//��û�������������������
			if (procList.size() == 0) return true;

			Proc[] procs = (Proc[])procList.toArray(new Proc[0]);
			//����
			procOrderManager.reset(docTypeID, flowNode.getFlowID(), flowNode.getID(), procs);
		} catch(E5Exception ex){
			log.error("[FlowTemplate.ImportOrder]Error when import flowNode order",ex);
			result = false;
		}
		return result;
	}
	//����һ�����̽ڵ�Ĳ�����
	private boolean setGroupByFlowNode(int docTypeID, FlowNode flowNode, Node docNode, ProcUnflow[] unflowProcs) {
		return setGroups(docTypeID, flowNode.getID(), docNode, unflowProcs);
	}
	//����һ��������
	private boolean setGroups(int docTypeID, int flowNodeID, Node docNode, ProcUnflow[] unflowProcs) {
		List nodeList = docNode.selectNodes("descendant::PROCGROUP");
		if (nodeList == null || nodeList.size() == 0) return true;

		boolean result = true;
		List list = new ArrayList(nodeList.size());
		try {
			//�����̽ڵ���������̲���һ����ȡ����������ÿ�β�ѯ���ݿ�
			ProcFlow[] flowProcs = (flowNodeID == 0) ? null : procManager.getProcs(flowNodeID);

			for (int i = 0; i < nodeList.size(); i++)
			{
				//һ��PROCGROUP
				Node node = (Node)nodeList.get(i);
				List sonList = node.selectNodes("descendant::PROC");
				if (sonList == null || sonList.size() == 0) continue;
				
				ProcGroup group = new ProcGroup();
				list.add(group);//�����б�
				
				for (int j = 0; j < sonList.size(); j++)
				{
					//һ��PROC
					Node procNode = (Node)sonList.get(j);
					String name = getAttributeValue(procNode, "@NAME");
					String type = getAttributeValue(procNode, "@TYPE");
					
					//ȡ��PROC����
					Proc proc = ("UNFLOW".equals(type) || flowNodeID == 0) ? getProc(name, null, unflowProcs)
							: getProc(name, flowProcs, null);
					if (proc == null) continue;
					group.addProc(getProcOrder(docTypeID, flowNodeID, j, proc.getProcID()));
				}
			}
			//��û�������������������
			if (list.size() == 0) return true;
			//����
			ProcGroup[] groups = (ProcGroup[])list.toArray(new ProcGroup[0]);
			procGroupManager.reset(docTypeID, flowNodeID, groups);
		} catch(E5Exception ex){
			log.error("[FlowTemplate.ImportOrder]Error when import flowNode order",ex);
			result = false;
		}
		return result;
	}
	//�����ĵ����͵ķ����̲�������
	private boolean setUnflowOrders(int docTypeID, Node docNode, ProcUnflow[] unflowProcs) {
		List nodeList = docNode.selectNodes("descendant::PROC");
		if (nodeList == null || nodeList.size() == 0) return true;

		boolean result = true;
		try {
			List procList = new ArrayList(nodeList.size());
			for (int i = 0; i < nodeList.size(); i++)
			{
				//һ��PROC
				Node node = (Node)nodeList.get(i);
				String name = getAttributeValue(node, "@NAME");
				//ȡ��PROC���󣬼���LIST
				Proc proc = getProc(name, null, unflowProcs);
				if (proc != null) procList.add(proc);
			}
			//��û�������������������
			if (procList.size() == 0) return true;

			Proc[] procs = (Proc[])procList.toArray(new Proc[0]);
			//����
			procOrderManager.reset(docTypeID, 0, 0, procs);
		} catch(E5Exception ex){
			log.error("[FlowTemplate.ImportOrder]Error when import flowNode order",ex);
			result = false;
		}
		return result;
	}
	//�����ĵ����͵ķ����̲�����
	private boolean setUnflowGroups(int docTypeID, Node docNode, ProcUnflow[] unflowProcs) {
		return setGroups(docTypeID, 0, docNode, unflowProcs);
	}
	private String getAttributeValue(Node node, String name)
	{
		Attribute child = (Attribute)node.selectSingleNode(name);
		if (child != null)
			return child.getValue();
		else return null;
		
	}
	private Proc getProc(String procName, ProcFlow[] flowProcs, ProcUnflow[] unflowProcs) {
		if (flowProcs != null) {
			for (int i = 0; i < flowProcs.length; i++)
				if (flowProcs[i].getProcName().equals(procName)) return flowProcs[i];
		}
		if (unflowProcs != null) {
			for (int i = 0; i < unflowProcs.length; i++)
				if (unflowProcs[i].getProcName().equals(procName)) return unflowProcs[i];
		}
		return null;
	}
	private ProcOrder getProcOrder(int docTypeID, int flowNodeID, int procOrder, int procID)
	{
		ProcOrder proc = new ProcOrder();
		proc.setDocTypeID(docTypeID);
		proc.setFlowNodeID(flowNodeID);
		proc.setOrder(procOrder);
		proc.setProcID(procID);
		
		return proc;
	}
}
