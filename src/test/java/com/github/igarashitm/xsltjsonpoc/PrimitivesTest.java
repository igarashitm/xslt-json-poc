package com.github.igarashitm.xsltjsonpoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrimitivesTest extends TestBase {
    @Test
    public void testPrimitive() throws Exception {
        loadYamlRoute( "03-primitives/primitives.yaml");
        var template = context.createFluentProducerTemplate();
        // XSLT json-to-xml() doesn't recognize primitives as JSON and throw an exception
        var exchange = template.withBody("hello").to("direct:mapping").send();
        assertNotNull(exchange.getException());
    }
}
