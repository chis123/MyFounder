/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2005-7-22  11:09:38
 *  
 *********************************************************************/
package com.founder.e5.dom.facade;

import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;

public class EUIDFacade {

    public static final byte IDTYPE_DOCLIB = 1;

    public static final byte IDTYPE_DOCTYPE = 2;

    public static final byte IDTYPE_DOCTYPEFIELD = 3;

    public static final byte IDTYPE_FOLDERVIEW = 4;

    public static final byte IDTYPE_RULE = 5;

    public static final byte IDTYPE_FILTER = 6;

    public static final byte IDTYPE_DOCLIBADDITIONALS = 7;
    
    public static final byte IDTYPE_REL_TABLE = 8;

    public static int getID(byte idType) throws E5Exception {

        switch (idType) {

        case IDTYPE_DOCLIB:
            return (int) EUID.getID("DocLibID");

        case IDTYPE_DOCTYPE:
            return (int) EUID.getID("DocTypeID");

        case IDTYPE_DOCTYPEFIELD:
            return (int) EUID.getID("FieldID");

        case IDTYPE_FILTER:
            return (int) EUID.getID("FILTERID");

        case IDTYPE_FOLDERVIEW:
            return (int) EUID.getID("FVID");

        case IDTYPE_RULE:
            return (int) EUID.getID("RULEID");

        case IDTYPE_DOCLIBADDITIONALS:
            return (int) EUID.getID("DocLibAdditionals");
            
        case IDTYPE_REL_TABLE:
            return (int) EUID.getID("DomRelTable");

        default:
            return -1;
        }

    }

}
