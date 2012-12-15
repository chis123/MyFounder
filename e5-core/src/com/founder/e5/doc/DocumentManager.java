/*
 * Created on 2005-6-21 16:55:07
 *
 */
package com.founder.e5.doc;

import com.founder.e5.context.E5Exception;

/**
 * �ĵ�ʵ��Ĺ���ӿڣ������ĵ�ʵ������ɾ�Ĳ顣
 * <br>����e5context
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-21 16:55:07
 */
public interface DocumentManager {
    
    /**
     * ָ���ĵ���ID��������Documentʵ����
     * 
     * ���裺
     * 1�������ĵ���ID����ĵ�����ID
     * 2�������ĵ�����ID����ĵ������ֶ�
     * 3��new һ��Document�������ĵ���ID���ĵ�����ID���ĵ������ֶ�
     * 4�������ĵ�ID����ֵ
     * 5������Documentʵ��
     * 
     * @param docLibID �ĵ���ID
     * @return ��Documentʵ��
     * @throws E5Exception
     */
    public Document newDocument( int docLibID ) throws E5Exception;

    /**
     * ��newDocument(int)��ͬ������Document��ID�ɲ������롣
     * @param docLibID
     * @param docID
     * @return
     * @throws E5Exception
     */
    public Document newDocument( int docLibID, long docID ) throws E5Exception;
    
	/**
	 * ���������ĵ��ĳ��ĵ���ID����������ԣ������Զ������ԣ�������Documentʵ����
	 * 
	 * @param refDoc �ο��ĵ�ʵ��
	 * @param docLibID ���ĵ������ĵ����ID
	 * @return Documentʵ��
	 * @throws E5Exception
	 */
	public Document newDocument( Document refDoc, int docLibID )
			throws E5Exception;
    
	/**
	 * ���������ĵ��ĳ��ĵ���ID����������ԣ������Զ������ԣ�������Documentʵ����
     * ��newDocument(Document, int)��ͬ������Document��ID�ɲ������롣
	 * 
	 * @param refDoc �ο��ĵ�ʵ��
	 * @param docLibID ���ĵ������ĵ����ID
	 * @return Documentʵ��
	 * @throws E5Exception
	 */
	public Document newDocument( Document refDoc, int docLibID, long docID )
			throws E5Exception;

	/**
     * ����id�����ĵ�ʵ��
     * 
     * �����ݿ���ȡ������Document��״̬Ϊ�־�̬��status=STATUS_PERSISTENT��;
     * �û����ô��ڳ־�̬��Document��set(...)��������������ʱ��Documentʵ��ά��һ�����±��
     * ͬʱ��¼���¹����ֶΣ��Ժ����DocumentManager.save(Document)ʱֻ������Щʵ�ʸ���
     * �����ֶΣ�֮��λ���±�Ǻ͸����ֶ���Ϣ��
     * �൱��get(docLibID, docID, true)
     * 
     * @param docLibID �ĵ���ID
     * @param docID �ĵ�ID
     * @return �ĵ�ʵ��
     * @throws E5Exception
     */
    public Document get(int docLibID, long docID) throws E5Exception;
    
    /**
     * ����id�����ĵ�ʵ��
     * 
     * �����ݿ���ȡ������Document��״̬Ϊ�־�̬��status=STATUS_PERSISTENT��;
     * �û����ô��ڳ־�̬��Document��set(...)��������������ʱ��Documentʵ��ά��һ�����±��
     * ͬʱ��¼���¹����ֶΣ��Ժ����DocumentManager.save(Document)ʱֻ������Щʵ�ʸ���
     * �����ֶΣ�֮��λ���±�Ǻ͸����ֶ���Ϣ��
     * 
     * @param doclibID �ĵ���ID
     * @param docID �ĵ�ID
     * @param lazyLoadLob �Ƿ��ӳټ���Lob�����ֶ�
     * @return �ĵ�ʵ��
     * @throws E5Exception
     */
    public Document get(int docLibID, long docID, boolean lazyLoadLob)
            throws E5Exception;
    
    /**
	 * ���ݶ��id�������ĵ�ʵ��<br>
	 * ��������
	 * 
	 * @see #get(int, long)
	 * @param docLibID �ĵ���ID
	 * @param docIDs �ĵ�ID
	 * @return �ĵ�ʵ������
	 * @throws E5Exception
	 */
    public Document[] get( int docLibID, long[] docIDs ) throws E5Exception;
    
    /**
	 * ����id�����ĵ�ʵ�壬��ֻȡָ�����ֶ�
	 * 
	 * @param docLibID �ĵ���ID
     * @param docID �ĵ���ID
     * @param columns �û������ֶ�����
     * @param lazyLoadLob �Ƿ��ӳټ���Lob�����ֶ�
	 * @return �ĵ�ʵ����ע�⣺����ֻ���������ֶε�ֵ��
	 * @throws E5Exception
	 */
	public Document get( int docLibID, long docID, String[] columns, boolean lazyLoadLob )
			throws E5Exception;
    
    /**
	 * ����id�����ĵ�ʵ�壬��ֻȡָ�����ֶ�<br>
	 * ע�⣺�˷����൱��get(int, long, String[], true)
	 * 
	 * @see #get(int, long, String[], boolean)
	 * @param docLibID �ĵ���ID
     * @param docID �ĵ���ID
     * @param columns �û������ֶ�����
	 * @return �ĵ�ʵ����ע�⣺����ֻ���������ֶε�ֵ��
	 * @throws E5Exception
	 */
	public Document get( int docLibID, long docID, String[] columns )
			throws E5Exception;
    
    /**
	 * ���ݶ��id�������ĵ�ʵ�壬��ֻȡָ�����ֶ�
	 * 
	 * @param docLibID �ĵ���ID
	 * @param docIDs �ĵ���ID
	 * @param columns �û������ֶ�����
	 * @return �ĵ�ʵ�����飨ע�⣺����ֻ���������ֶε�ֵ��
	 * @throws E5Exception
	 */
    public Document[] get( int docLibID, long[] docIDs, String[] columns )
			throws E5Exception;
    
    /**
     * �����ĵ�ʵ�塣<br>
     * ��Ϊ���ĵ�����ʱ̬�������ڿ��в���һ����¼����Ϊ�����ĵ����־�̬��������¿��м�¼<br>
     * <br>
     * ���裺<br>
     * 1���ж�Document.getStatus()����Ϊ��ʱ̬��STATUS_TRANSIENT����������¼�¼��
     * ��Ϊ�־�̬��STATUS_PERSISTENT������������м�¼<br>
     * 2���������м�¼ʱ����Document.isDirty()Ϊ����ֱ�ӷ��أ������Document.getDirtyColumns()
     * ��ȡ���޸Ĺ��ı��ֶΣ�������Щ�ֶΣ�Ȼ�����Document.dirtyColumns������Document.dirty=false<br>
     * <br>
     * ʵ�ʸ����ĵ�ʱ��Ҫ���ǹ����ĵ����������̼�¼������һ���ԣ���������
     * 
     * @param doc �������ĵ�ʵ��
     * @throws E5Exception
     */
    public void save(Document doc) throws E5Exception;
    
    
    /**
     * �ƶ�ָ���ĵ�����һ�ĵ��⡣<br>
     * �ĵ��ƿ�ʱ�ĵ�ID�Ϳ�ID�����䶯���������Զ������û�����ʱ�ĵ���״̬
     * <br>
     * <br>
     * �ĵ����봦�ڳ־�̬���ƶ�����ͬʱ�����ĵ���ǰ״̬��
     * ͬʱҪ�ƶ����������̼�¼��Ŀ���ĵ��⣬�����¹����ĵ���Ϣ��<br>
     * ע�⣺�ĵ��ƿ��ID�������仯!���̼�¼��IDҲ�����仯�������������ԭ��ID�ᱻ�滻Ϊ�¸�ID
     * 
     * @param doc �ĵ�ʵ��
     * @param newDocLibID Ŀ���ĵ���ID
     * @throws E5Exception
     * @return ���ĵ�
     */
    public Document moveTo(Document doc, int newDocLibID) throws E5Exception;
    
    /**
     * �ƶ�ָ���ĵ�����һ�ĵ��⡣<br>
     * <br>
     * <br>
     * ��ο�moveTo(Document, int)����
     * @param doc
     * @param newDocLibID
     * @param docID	ָ��ID
     * @return
     * @throws E5Exception
     */
    public Document moveTo(Document doc, int newDocLibID, long newDocID) throws E5Exception;
    /**
     * ���ĵ���ɾ���ĵ���¼������ɾ����
     * 
     * ɾ���ĵ�ʱҪͬʱɾ���ĵ���ص����̼�¼�����¹�����Ϣ������һ������
     * 
     * @param doc ��ɾ�ĵ�
     * @throws E5Exception
     */
    public void delete(Document doc) throws E5Exception;

    
    /**
     * ����IDɾ�����¼������ɾ����
     * 
     * ɾ���ĵ�ʱҪͬʱɾ���ĵ���ص����̼�¼�����¹�����Ϣ������һ������
     * 
     * @param docLibID �ĵ���ID
     * @param docID �ĵ�ID
     * @throws E5Exception
     */
    public void delete(int docLibID, long docID) throws E5Exception;

}
