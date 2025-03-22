package com.github.igarashitm.xsltjsonpoc;

import org.junit.jupiter.api.Test;

public class WithBuiltinWithLosslessTest extends TestBase {
    @Test
    public void test() throws Exception {
        loadYamlRoute( "01-b-with-builtin-with-lossless/01-b-full.yaml");
        var template = context.createFluentProducerTemplate();
        var exchange = template.to("direct:mapping").send();
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
        System.out.println(prettyPrintJson(exchange.getMessage().getBody(String.class)));
    }
}
