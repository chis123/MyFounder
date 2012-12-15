package com.founder.e5.flow;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.E5Exception;

public class ProcGroupReaderImpl implements ProcGroupReader {
	public ProcGroupReaderImpl() {

	}
	public ProcGroup[] getProcGroups(int docTypeID, int flowNodeID)
			throws E5Exception {
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache == null)
			return null;
		ProcGroup[] all = cache.getGroups();
		
		List list = new ArrayList();
		
		for (int i = 0; i < all.length; i++) {
			if (all[i].getDocTypeID() == docTypeID && all[i].getFlowNodeID() == flowNodeID)
				list.add(all[i]);
			else if (all[i].getDocTypeID() > docTypeID || all[i].getFlowNodeID() > flowNodeID)
				break;
		}
		return (ProcGroup[])list.toArray(new ProcGroup[0]);
	}
}
