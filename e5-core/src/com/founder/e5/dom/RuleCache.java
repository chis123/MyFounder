package com.founder.e5.dom;

import java.util.List;
import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.dom.util.TIntIntListHashMap;

/**
 * @deprecated 2006-5-17
 * �ѱ��ϲ���DocTypeCache��
 * 
 * depends on context.jar
 * 
 * @version 1.0
 * @created 11-����-2005 15:56:15
 */
public class RuleCache implements Cache {

    private TIntObjectHashMap ruleID_Rule_map = new TIntObjectHashMap();
    private TIntIntListHashMap typeID_RuleID_map = new TIntIntListHashMap();

    /**
     * ��ȡָ��id�Ĺ���
     * 
     * @param ruleID
     * @return ���û���򷵻�null
     */
    public Rule getRule(int ruleID) {
        return (Rule) this.ruleID_Rule_map.get(ruleID);
    }

    /**
     * ��ȡָ���ĵ������µ����й���
     * 
     * @param doctypeID
     * @return ���û�У��򷵻ؿ�����
     */
    public Rule[] getRules(int doctypeID) {

        TIntArrayList l = this.typeID_RuleID_map.getIntList(doctypeID);
        if (null == l)
            return DomUtils.EMPTY_RULE_ARRAY;

        final int size = l.size();
        Rule[] rules = new Rule[size];
        for (int i = 0; i < size; i++) {
            rules[i] = this.getRule(l.getQuick(i));
        }

        return rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#refresh()
     */
    public void refresh() throws E5Exception {

        Rule[] ruleList = this.getAllRules();
        this.ruleID_Rule_map.clear();
        this.typeID_RuleID_map.clear();
        for (int i = 0; i < ruleList.length; i++) {
            Rule r = ruleList[i];
            this.ruleID_Rule_map.put(r.getRuleID(), r);
            this.typeID_RuleID_map.put(r.getDocTypeID(), r.getRuleID());
        }

    }

    private Rule[] getAllRules() throws E5Exception {
    	RuleManager ruleManager = (RuleManager)Context.getBean(RuleManager.class);
    	return ruleManager.getByDoctypeID(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#reset()
     */
    public void reset() {
        this.ruleID_Rule_map.clear();
        this.typeID_RuleID_map.clear();
    }

}