package com.founder.e5.dom;

/**
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 15:48:38
 */
public class DomReaderFactory {

    private static final DataSourceConfigReader configReader = new DataSourceConfigReaderImpl();

    private static final DocLibReader docLibReader = new DocLibReaderImpl();

    private static final DocTypeReader docTypeReader = new DocTypeReaderImpl(
            new ContextCacheManagerImpl());

    private static final FilterReader filterReaer = new FilterReaderImpl();

    private static final FolderReader folderReader = new FolderReaderImpl();

    private static final RuleReader ruleReader = new RuleReaderImpl();

    private static final ViewReader viewReader = new ViewReaderImpl();

    public static DataSourceConfigReader buildDataSourceConfigReader() {
        return configReader;
    }

    public static DocLibReader buildDocLibReader() {
        return docLibReader;
    }

    public static DocTypeReader buildDocTypeReader() {
        return docTypeReader;
    }

    public static FilterReader buildFilterReader() {
        return filterReaer ;
    }

    public static FolderReader buildFolderReader() {
        return folderReader ;
    }

    public static  RuleReader buildRuleReader() {
        return ruleReader;
    }

    public static ViewReader buildViewReader() {
        return viewReader;
    }

    public DomReaderFactory() {

    }

}