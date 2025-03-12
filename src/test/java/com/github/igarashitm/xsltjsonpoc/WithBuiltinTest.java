package com.github.igarashitm.xsltjsonpoc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WithBuiltinTest extends TestBase {
    private final Logger logger = LoggerFactory.getLogger(WithBuiltinTest.class);

    public WithBuiltinTest() throws Exception {
    }

    @Test
    public void testSource() throws Exception {
        loadYamlRoute( "01-with-builtin/01-01-source.yaml");
        var template = context.createFluentProducerTemplate();
        var exchange = template.to("direct:mapping").send();
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
        System.out.println(exchange.getMessage().getBody(String.class));
    }

    @Test
    public void testTarget() throws Exception {
        loadYamlRoute( "01-with-builtin/01-02-target.yaml");
        var template = context.createFluentProducerTemplate();
        var exchange = template.to("direct:mapping").send();
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
        System.out.println(prettyPrintJson(exchange.getMessage().getBody(String.class)));
    }

    @Test
    public void testFull() throws Exception {
        loadYamlRoute("01-with-builtin/01-03-full.yaml");
        var template = context.createFluentProducerTemplate();
        var exchange = template.to("direct:mapping").send();
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
        System.out.println(prettyPrintJson(exchange.getMessage().getBody(String.class)));
    }
}
