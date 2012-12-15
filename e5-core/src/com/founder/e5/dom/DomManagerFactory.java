package com.founder.e5.dom;

/**
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 15:48:45
 */
public class DomManagerFactory extends DomReaderFactory {

    private static final DocTypeManager docTypeMgr = new DocTypeManagerImpl();

    private static final DocLibManager docLibMgr = new DocLibManagerImpl();

    private static final FolderManager folderMgr = new FolderManagerImpl();

    private static final RuleManager ruleMgr = new RuleManagerImpl();

    private static final FilterManager filterMgr = new FilterManagerImpl();

    private static final ViewManager viewMgr = new ViewManagerImpl();

    private static final DataSourceConfigManager dataSourceConfigMgr = new DataSourceConfigManagerImpl();

    public static DocTypeManager buildDocTypeManager() {
        return docTypeMgr;

    }

    public static DocLibManager buildDocLibManager() {
        return docLibMgr;
    }

    public static FolderManager buildFolderManager() {
        return folderMgr;
    }

    public static ViewManager buildViewManager() {
        return viewMgr;
    }

    public static RuleManager buildRuleManager() {
        return ruleMgr;
    }

    public static FilterManager buildFilterManager() {
        return filterMgr;
    }

    public static DataSourceConfigManager buildDataSourceConfigManager() {
        return dataSourceConfigMgr;
    }

}