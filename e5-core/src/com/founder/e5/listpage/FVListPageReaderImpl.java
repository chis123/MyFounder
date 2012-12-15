package com.founder.e5.listpage;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.CacheReader;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.FolderViewCache;

/**
 * 文件夹列表方式读取接口的实现类
 * @created 2009-8-25
 * @author Gong Lijie
 * @version 1.0
 */
public class FVListPageReaderImpl implements FVListPageReader {
	
	public ListPage[] get(int fvID) throws E5Exception {
		FVListPage[] all = getAll();
		if (all == null) return null;

		for (int i = 0; i < all.length; i++) {
			if (all[i].getFvID() == fvID)
				return all[i].getPageList();
		}
		return null;
	}
	
	public int[] getIDs(int fvID) throws E5Exception {
		FVListPage[] all = getAll();
		if (all == null) return null;
		
		for (int i = 0; i < all.length; i++) {
			if (all[i].getFvID() == fvID)
				return StringUtils.getIntArray(all[i].getPages());
		}
		return null;
	}

	public FVListPage[] getAll() throws E5Exception {
		FolderViewCache cache = (FolderViewCache)CacheReader.find(FolderViewCache.class);
		return cache.getFVListPages();
	}
}
