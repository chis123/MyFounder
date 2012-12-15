package com.founder.e5.app.template;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.DocType;
import com.founder.e5.dom.DocTypeField;
import com.founder.e5.dom.DocTypeManager;
import com.founder.e5.dom.Filter;
import com.founder.e5.dom.FilterManager;
import com.founder.e5.dom.Rule;
import com.founder.e5.dom.RuleManager;
import com.founder.e5.dom.util.DomUtils;

/**
 * �ĵ�������ز��ֵĵ�������
 * @created 2007-11-19
 * @author Gong Lijie
 * @version 1.0
 */
class DomTemplateExport {

    String exportDocTypes(DocType[] types)  throws E5Exception
    {
        Element typesElement = DocumentHelper.createElement("DocumentTypes");
        Document document = DocumentHelper.createDocument(typesElement);
        // doctypes
        for( int i = 0; i < types.length; i++){
            DocType type = types[i];
            exportDocType(typesElement, type);
        }
        // filters
        Element filtersElement = typesElement.addElement("FILTERS");
        for( int i = 0; i < types.length; i++){
            DocType type = types[i];
            exportFilters(filtersElement, type.getDocTypeID(), type.getDocTypeName());          
        }
        // rules
        Element rulesElement = typesElement.addElement("RULES");
        for( int i = 0; i < types.length; i++){
            DocType type = types[i];
            exportRules(rulesElement, type.getDocTypeID(), type.getDocTypeName());          
        }
        return document.getRootElement().asXML();
    }
    //�ĵ�����
    private void exportDocType(Element rootElement, DocType type) throws E5Exception
    {
        int typeID = type.getDocTypeID();
        String typeName = type.getDocTypeName();
        
        Element typeElement = rootElement.addElement("DocumentType");
        
        //properties
        exprotDocTypeProperties(type, typeName, typeElement);        
        //fields           
        exportFields( typeID, typeElement);        
    }
    // �ĵ���������
    private void exprotDocTypeProperties(DocType type, String typeName, Element typeElement)
    {
        typeElement.addElement("Name").addText(typeName);
        typeElement.addElement("DispName").addText(typeName);
        typeElement.addElement("Attribute").addText("0");
        typeElement.addElement("Description")
                    .addText((type.getDescInfo() == null)? "": type.getDescInfo());
    }
    //�ĵ������ֶ�
    private void exportFields(int typeID, Element typeElement) throws E5Exception
    {
        DocTypeField[] fields = DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        DocTypeManager typeManager = (DocTypeManager) Context.getBean(DocTypeManager.class);
        
        fields = typeManager.getFieldsExt(typeID);
        
        Element fieldsElement = typeElement.addElement("Fields");
        
        for( int j = 0; j < fields.length; j++)
        {
            DocTypeField field = fields[j];
            
            Element fieldElement = fieldsElement.addElement("Field");
            
            fieldElement.addElement("ColumnCode").addText(field.getColumnCode());
            fieldElement.addElement("ColumnName").addText(field.getColumnName());
            fieldElement.addElement("DataType").addText(field.getDataType());
            fieldElement.addElement("Length").addText(""+field.getDataLength());
            
            fieldElement.addElement("IsNull").addText(""+field.getIsNull());
            fieldElement.addElement("Status").addText(field.getStatus());
            fieldElement.addElement("Attribute").addText(String.valueOf(field.getAttribute()));
            //ȱʡֵ
            String defaultValue = field.getDefaultValue();
            if (defaultValue == null) defaultValue = "";
            fieldElement.addElement("DefaultValue").addText(defaultValue);
            //��д��ʽ
            fieldElement.addElement("EditType").addText(String.valueOf(field.getEditType()));
            //ö��ֵ
            if (field.getEditType() == DocTypeField.EDITTYPE_ENUM 
            	|| field.getEditType() == DocTypeField.EDITTYPE_COMPLEX)
            	fieldElement.addElement("Options").addText(String.valueOf(field.getOptions()));
            //ֻ��?
            fieldElement.addElement("Readonly").addText(String.valueOf(field.getReadonly()));
        }
    }
    //������
    private void exportFilters(Element filtersElement, int typeID, String typeName) throws E5Exception {
        
        FilterManager filterManager = (FilterManager) Context.getBean(FilterManager.class);
        
        Filter[] filters = DomUtils.EMPTY_FILTER_ARAAY;
        
        filters = filterManager.getByTypeID(typeID);
        
        Element filterTypeElement = filtersElement.addElement("DOCTYPE").addAttribute("NAME",typeName);
        
        for( int j = 0; j < filters.length; j++){
            Filter filter = filters[j];
            filterTypeElement.addElement("FILTER").addAttribute("NAME",filter.getFilterName())
                                                  .addAttribute("FORMULA",filter.getFormula())
                                                  .addAttribute("DESCRIPTION",filter.getDescription());               
        }
    }
    //����
    private void exportRules(Element rulesElement, int typeID, String typeName) throws E5Exception 
    {
        RuleManager ruleManger = (RuleManager) Context.getBean(RuleManager.class);
        Rule[] rules = DomUtils.EMPTY_RULE_ARRAY;
        
        rules = ruleManger.getByDoctypeID(typeID);
        
        Element ruleTypeElement = rulesElement.addElement("DOCTYPE").addAttribute("NAME",typeName);
        
        for( int j = 0; j < rules.length; j++)
        {
            Rule rule = rules[j];
            
            ruleTypeElement.addElement("RULE").addAttribute("NAME",rule.getRuleName())
                                              .addAttribute("CLASSNAME",rule.getRuleClassName())
                                              .addAttribute("VARPARAM",rule.getRuleArguments())
                                              .addAttribute("METHOD",rule.getRuleMethod())
                                              .addAttribute("DESCRIPTION",rule.getDescription());
        }
    }
}
