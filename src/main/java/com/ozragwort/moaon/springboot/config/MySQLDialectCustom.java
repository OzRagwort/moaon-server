package com.ozragwort.moaon.springboot.config;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MySQLDialectCustom extends MySQL57Dialect {

    public MySQLDialectCustom() {

        registerFunction(
                "match_natural",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in natural language mode)")
        );

        registerFunction(
                "match_boolean",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)")
        );

        registerFunction(
                "matchs_natural",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1,?2) against (?3 in natural language mode)")
        );

        registerFunction(
                "matchs_boolean",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1,?2) against (?3 in boolean mode)")
        );
    }

}
