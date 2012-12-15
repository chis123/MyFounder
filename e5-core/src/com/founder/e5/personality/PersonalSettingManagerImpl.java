package com.founder.e5.personality;

import gnu.trove.TIntArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.config.ConfigReader;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBContext;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.util.CloseHelper;
import com.founder.e5.dom.FolderReader;
import com.founder.e5.dom.FolderView;
import com.founder.e5.listpage.ListPage;
import com.founder.e5.listpage.ListPageManager;
import com.founder.e5.permission.FVPermission;
import com.founder.e5.permission.FVPermissionReader;

/**
 * PersonalSettingManager实现。<br>
 * <br>
 * 注意：该类所有返回数组类型的方法，若返回null则表示相应的记录不存在
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 15:34:52
 */
public class PersonalSettingManagerImpl implements PersonalSettingManager {

	private static final String TABLENAME = "FSYS_PERSONSETTING";

	static {
		HashMap propMapping = new HashMap();
		propMapping.put( "CONFIGITEM", "item" );
		propMapping.put( "CONFIGVALUE", "value" );

		DBContext.registerMapping(
				TABLENAME,
				PersonalSetting.class,
				propMapping );
	}

	private PersonalSetting query( Map queryParams ) throws E5Exception {
		DBSession ss = Context.getDBSession();
		try {
			List list = ss.query( TABLENAME, queryParams );
			if ( list.size() > 0 )
				return ( PersonalSetting ) list.get( 0 );
		} catch ( Exception e ) {
			throw new E5Exception( e );
		} finally {
			CloseHelper.closeQuietly( ss );
		}
		return null;
	}

	private void delete( Map queryParams ) throws E5Exception {
		StringBuffer sb = new StringBuffer();
		sb.append( "delete from " ).append( TABLENAME ).append( " where " );

		for ( Iterator i = queryParams.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			sb.append( me.getKey() ).append( "=?" );

			if ( i.hasNext() )
				sb.append( " and " );
		}

		DBSession ss = Context.getDBSession();
		try {
			ss.executeUpdate( sb.toString(), queryParams.values().toArray() );
		} catch ( Exception e ) {
			throw new E5Exception( e );
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}

	private PersonalSetting query( int userID, int roleID, String item )
			throws E5Exception {
		HashMap map = new HashMap();
		map.put( "USERID", new Integer( userID ) );
		map.put( "ROLEID", new Integer( roleID ) );
		map.put( "CONFIGITEM", item );
		return query( map );
	}

	private PersonalSetting query( int userID, int roleID, String item,
			String ext1 ) throws E5Exception {
		HashMap map = new HashMap();
		map.put( "USERID", new Integer( userID ) );
		map.put( "ROLEID", new Integer( roleID ) );
		map.put( "CONFIGITEM", item );
		map.put( "EXT1", ext1 );
		return query( map );
	}

	private void delete( int userID ) throws E5Exception {
		HashMap map = new HashMap();
		map.put( "USERID", new Integer( userID ) );
		delete( map );
	}

	private void delete( int userID, int roleID ) throws E5Exception {
		HashMap map = new HashMap();
		map.put( "USERID", new Integer( userID ) );
		map.put( "ROLEID", new Integer( roleID ) );
		delete( map );
	}

	private void delete( int userID, int roleID, String item, String ext1 )
			throws E5Exception {
		HashMap map = new HashMap();
		map.put( "USERID", new Integer( userID ) );
		map.put( "ROLEID", new Integer( roleID ) );
		map.put( "CONFIGITEM", item );
		map.put( "EXT1", ext1 );
		delete( map );
	}

	private void insert( PersonalSetting bean ) throws E5Exception {
		DBSession ss = Context.getDBSession();
		try {
			ss.store( TABLENAME, bean );
		} catch ( Exception e ) {
			throw new E5Exception( e );
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}

	/**
	 * 更新数据库中给定的那些bean属性
	 * 
	 * @param bean
	 * @param dirtyFields 以逗号分隔的bean属性列表
	 * @throws E5Exception
	 */
	private void update( PersonalSetting bean, String dirtyFields )
			throws E5Exception {
		String[] dirty = dirtyFields.split( "," );
		DBSession ss = Context.getDBSession();
		try {
			ss.update( TABLENAME, bean, dirty );
		} catch ( Exception e ) {
			throw new E5Exception( e );
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}

	// --------------------------------------------------------------------------
	// 资源树根定制（存的时候不过滤，读的时候才过滤）
	// ext1代表showParent，ext2代表showRoot

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.personality.PersonalSettingReader#getResourceTreeRoot(int,
	 *      int)
	 */
	public int[] getResourceTreeRoot( int userID, int roleID )
			throws E5Exception {

		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.RESOURCETREEROOT );
		if ( bean == null )
			return null;

		if ( bean.getValue() != null ) {
			int[] folderIDs = StringUtils.getIntArray( bean.getValue() );
			if ( folderIDs != null )
				return filter( folderIDs, roleID );

		}

		return ArrayUtils.EMPTY_INT_ARRAY;
	}

	/**
	 * 过滤文件夹ID，须满足条件：1、存在 2、角色有权限 3、父节点尚未加入
	 * 
	 * @param folderIDs
	 * @param roleID
	 * @return
	 * @throws E5Exception
	 */
	private int[] filter( int[] folderIDs, int roleID ) throws E5Exception {
		TIntArrayList result1 = new TIntArrayList();
		TIntArrayList result2 = new TIntArrayList();
		int[] permed = getPermedFolders( roleID );

		// 第一遍过滤
		for ( int i = 0; i < folderIDs.length; i++ ) {
			int folderID = folderIDs[i];

			// 条件1
			if ( folderReader.get( folderID ) == null )
				continue; // 不存在

			// 条件2
			if ( permed != null && !ArrayUtils.contains( permed, folderID ) )
				continue; // 没权限

			result1.add( folderID );
		}

		folderIDs = result1.toNativeArray();

		// 第二遍过滤
		for ( int i = 0; i < folderIDs.length; i++ ) {
			int folderID = folderIDs[i];
			FolderView fv = folderReader.get( folderID );
			int[] parents = fv.getParents();

			if ( parents != null ) {
				if ( com.founder.e5.commons.ArrayUtils.intersect(
						folderIDs,
						parents ).length > 0 )
					continue;
			}

			result2.add( folderID );
		}

		return result2.toNativeArray();
	}

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.personality.PersonalSettingManager#setResourceTreeRoot(int,
	 *      int, int[])
	 */
	public void setResourceTreeRoot( int userID, int roleID, int[] folderIDs )
			throws E5Exception {
		setResourceTreeRoot( userID, roleID, StringUtils.join( folderIDs, "," ) );
	}

	/**
	 * @throws E5Exception
	 * @see com.founder.e5.personality.PersonalSettingManager#setResourceTreeRoot(int,
	 *      int, java.lang.String)
	 */
	public void setResourceTreeRoot( int userID, int roleID, String folderIDs )
			throws E5Exception {
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.RESOURCETREEROOT );
		if ( bean == null ) {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( ConfigItem.RESOURCETREEROOT );
			bean.setValue( folderIDs );

			insert( bean );
		} else {
			bean.setValue( folderIDs );
			update( bean, "value" );
		}
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingReader#showTreeParent(int,
	 *      int)
	 */
	public boolean showTreeParent( int userID, int roleID ) throws E5Exception {
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.RESOURCETREEROOT );
		String value = bean.getExt1();
		return "1".equals( value );
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#setShowTreeParent(int,
	 *      int, boolean)
	 */
	public void setShowTreeParent( int userID, int roleID, boolean enabled )
			throws E5Exception {
		String ext1 = ( enabled ? "1" : "0" );
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.RESOURCETREEROOT );
		if ( bean == null ) {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( ConfigItem.RESOURCETREEROOT );
			bean.setExt1( ext1 );

			insert( bean );
		} else {
			update( bean, "ext1" );
		}
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingReader#showTreeRoot(int,
	 *      int)
	 */
	public boolean showTreeRoot( int userID, int roleID ) throws E5Exception {
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.RESOURCETREEROOT );
		String value = bean.getExt2();
		return "1".equals( value );
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#setShowTreeRoot(int,
	 *      int, boolean)
	 */
	public void setShowTreeRoot( int userID, int roleID, boolean enabled )
			throws E5Exception {
		String ext2 = ( enabled ? "1" : "0" );
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.RESOURCETREEROOT );
		if ( bean == null ) {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( ConfigItem.RESOURCETREEROOT );
			bean.setExt2( ext2 );

			insert( bean );
		} else {
			update( bean, "ext2" );
		}
	}

	// -------------------------------------------------------------------------
	// 图标和文字选项定制

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#setIconAndTextOption(int,
	 *      int, int)
	 */
	public void setIconAndTextOption( int userID, int roleID, int configValue )
			throws E5Exception {
		String value = String.valueOf( configValue );
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.ICON_TEXT_OPTION );
		if ( bean == null ) {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( ConfigItem.ICON_TEXT_OPTION );
			bean.setValue( value );

			insert( bean );
		} else {
			bean.setValue( value );
			update( bean, "value" );
		}
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingReader#getIconAndTextOption(int,
	 *      int)
	 */
	public int getIconAndTextOption( int userID, int roleID )
	throws E5Exception 
	{
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.ICON_TEXT_OPTION );

		String strValue;
		if ( bean != null ) strValue = bean.getValue();
		else// 若没有做配置，则使用缺省配置 2006-12-30 Gong Lijie
			strValue = ConfigReader.getInstance().getCustomize().getDefaultButtonStyle();
		int value = getInt(strValue, ConfigItem.TOPICON_DOWNTEXT);
		
		// 检查结果合法性
		switch ( value ) {
			case ConfigItem.ONLY_TEXT :
			case ConfigItem.ONLY_ICON :
			case ConfigItem.LEFTICON_RIGHTTEXT :
			case ConfigItem.TOPICON_DOWNTEXT :
				return value;
			default :
				return ConfigItem.TOPICON_DOWNTEXT;
		}
	}
	private int getInt(String str, int defaultValue){
		try{
			return Integer.parseInt(str);
		}catch (Exception e){
			return defaultValue;
		}
	}
	// ----------------------------------------------------------------------

	// 用来检查角色对文件夹是否有权限
	private FVPermissionReader fvpermReader;

	public void setFvpermReader( FVPermissionReader fvpermReader ) {
		this.fvpermReader = fvpermReader;
	}

	/**
	 * 取得角色有*读*和*处理*权限的文件夹ID
	 */
	private int[] getPermedFolders( int roleID ) throws E5Exception {
		int[] perms = { FVPermission.PERMISSION_READ,
				FVPermission.PERMISSION_PROCESS };
		return fvpermReader.getFoldersOfRole( roleID, perms );
	}

	// 用来根据文件夹ID获得文件夹对象
	private FolderReader folderReader;

	public void setFolderReader( FolderReader folderReader ) {
		this.folderReader = folderReader;
	}

	private ListPageManager listManager;

	public void setListManager( ListPageManager listManager ) {
		this.listManager = listManager;
	}

	// ----------------------------------------------------------------------

	/**
	 * @see com.founder.e5.personality.PersonalSettingReader#getListPages(int,
	 *      int, int)
	 */
	public int[] getListPages( int userID, int roleID, int docTypeID )
			throws E5Exception {
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.LISTPAGE_CHOSEN );
		if ( bean == null )
			return null;

		String value = bean.getValue();
		int[] listID1 = StringUtils.getIntArray( value ); // 用户选用的所有列表方式
		if ( listID1 == null )
			listID1 = ArrayUtils.EMPTY_INT_ARRAY;

		if ( docTypeID == 0 )
			return listID1;

		TIntArrayList temp = new TIntArrayList();
		ListPage[] lists = listManager.get( docTypeID );
		if ( lists != null ) {
			for ( int i = 0; i < lists.length; i++ ) {
				temp.add( lists[i].getListID() );
			}
		}
		int[] listID2 = temp.toNativeArray(); // 某个文档类型下的所有列表方式

		// 交集：某个文档类型下用户选用的列表方式
		return com.founder.e5.commons.ArrayUtils.intersect( listID1, listID2 );
	}

	/**
	 * 更新选用的列表方式时，检查是否去掉了某些列表方式，连带删除针对这些列表方式定制的表记录
	 * 
	 * @see com.founder.e5.personality.PersonalSettingManager#setListPages(int,
	 *      int, java.lang.String)
	 */
	public void setListPages( int userID, int roleID, String listIDs )
			throws E5Exception {
		PersonalSetting bean = query(
				userID,
				roleID,
				ConfigItem.LISTPAGE_CHOSEN );
		if ( bean != null ) {
			String oldListIDs = bean.getValue();
			String[] oldIDs = oldListIDs.split( "," );
			String[] newIDs = listIDs.split( "," );
			String[] deleted = com.founder.e5.commons.ArrayUtils.minus(
					oldIDs,
					newIDs );
			for ( int i = 0; i < deleted.length; i++ ) {
				String listID = deleted[i];
				delete(
						userID,
						roleID,
						ConfigItem.LISTPAGE_CFG + "_" + listID,
						listID );
			}

			bean.setValue( listIDs );
			update( bean, "value" );
		} else {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( ConfigItem.LISTPAGE_CHOSEN );
			bean.setValue( listIDs );

			insert( bean );
		}
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingReader#getListPageCfg(int,
	 *      int, int)
	 */
	public String[] getListPageCfg( int userID, int roleID, int listPageID )
			throws E5Exception {
		PersonalSetting bean = query( userID, roleID, ConfigItem.LISTPAGE_CFG
				+ "_" + listPageID, String.valueOf( listPageID ) );
		if ( bean == null )
			return null;
		String[] result = new String[ 2 ];
		result[0] = bean.getExt2();
		result[1] = bean.getValue();
		return result;
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#setListPageCfg(int,
	 *      int, int, java.lang.String, String)
	 */
	public void setListPageCfg( int userID, int roleID, int listPageID,
			String listXml, String templateSlice ) throws E5Exception {
		String item = ConfigItem.LISTPAGE_CFG + "_" + listPageID;
		PersonalSetting bean = query(
				userID,
				roleID,
				item,
				String.valueOf( listPageID ) );
		if ( bean == null ) {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( item );
			bean.setExt1( String.valueOf( listPageID ) );
			bean.setValue( templateSlice );
			bean.setExt2( listXml );

			insert( bean );
		} else {
			bean.setValue( templateSlice );
			bean.setExt2( listXml );

			update( bean, "value,ext2" );
		}
	}

	/**
	 * @param userID
	 * @param roleID
	 * @param docTypeID
	 * @param flowNodeID
	 * @return
	 * @throws E5Exception
	 */
	private PersonalSetting queryToolbarCfg( int userID, int roleID,
			int docTypeID, int fvID, int flowNodeID ) throws E5Exception {
		HashMap map = new HashMap();
		map.put( "USERID", new Integer( userID ) );
		map.put( "ROLEID", new Integer( roleID ) );
		map.put( "CONFIGITEM", getToolbarItem(docTypeID, fvID, flowNodeID));
		return query( map );
	}
	private String getToolbarItem(int docTypeID, int fvID, int flowNodeID)
	{
		return (new StringBuffer(ConfigItem.TOOLBAR)
				.append("_").append(docTypeID)
				.append("_").append(fvID)
				.append("_").append(flowNodeID)
				).toString();
	}
	public int[] getToolbarCfg( int userID, int roleID, int docTypeID,
			int fvID, int flowNodeID ) throws E5Exception {
		PersonalSetting bean = queryToolbarCfg(
				userID,
				roleID,
				docTypeID,
				fvID,
				flowNodeID );
		if ( bean != null ) {
			int[] result = StringUtils.getIntArray( bean.getValue() );
			if ( result != null )
				return result;
			else
				return ArrayUtils.EMPTY_INT_ARRAY;
		}
		return null;
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#setToolbarCfg(int,
	 *      int, int, int, int, String)
	 */
	public void setToolbarCfg( int userID, int roleID, int docTypeID, int fvID,
			int flowNodeID, String procIDs ) throws E5Exception {
		PersonalSetting bean = queryToolbarCfg(
				userID,
				roleID,
				docTypeID,
				fvID,
				flowNodeID );
		if ( bean == null ) {
			bean = new PersonalSetting();
			bean.setUserID( userID );
			bean.setRoleID( roleID );
			bean.setItem( getToolbarItem(docTypeID, fvID, flowNodeID) );
			bean.setExt1( String.valueOf( docTypeID ) );
			bean.setExt2( String.valueOf( fvID ) );
			bean.setExt3( String.valueOf( flowNodeID ) );
			bean.setValue( procIDs );

			insert( bean );
		} else {
			bean.setValue( procIDs );
			update( bean, "value" );
		}
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#deleteAllSetting(int,
	 *      int)
	 */
	public void deleteAllSetting( int userID, int roleID ) throws E5Exception {
		delete( userID, roleID );
	}

	/**
	 * @see com.founder.e5.personality.PersonalSettingManager#deleteAllSetting(int)
	 */
	public void deleteAllSetting( int userID ) throws E5Exception {
		delete( userID );
	}

}
