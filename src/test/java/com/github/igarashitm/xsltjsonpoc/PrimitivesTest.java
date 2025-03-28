package com.github.igarashitm.xsltjsonpoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimitivesTest extends TestBase {
    @Test
    public void testTopmostPrimitive() throws Exception {
        loadYamlRoute( "03-primitives/primitives.yaml");
        var template = context.createFluentProducerTemplate();

        var exchange = template.withBody("\"hello\"").to("direct:mapping").send();
        assertEquals("\"hello\"", exchange.getMessage().getBody());

        exchange = template.withBody("123456").to("direct:mapping").send();
        assertEquals("123456", exchange.getMessage().getBody());
    }
}
