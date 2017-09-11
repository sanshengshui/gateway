package com.aiyolo.common;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class ExtendedMySQLDialect extends MySQLDialect {

    public ExtendedMySQLDialect() {
        super();
        registerFunction("regexp", new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, "?1 REGEXP ?2"));
    }

}
