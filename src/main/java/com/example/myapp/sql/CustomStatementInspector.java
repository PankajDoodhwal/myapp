package com.example.myapp.sql;

import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class CustomStatementInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        if (ctx != null) {
            ctx.addSqlLog(sql);
        }
        return sql;
    }
}
