package com.github.igarashitm.xsltjsonpoc;

import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

class WithXjTest extends TestBase {
    public WithXjTest() throws Exception {
    }

    @Test
    public void testSource() throws Exception {
        loadYamlRoute( "02-with-xj/02-01-source.yaml");
    }

    @Test
    public void testTarget() throws Exception {
        loadYamlRoute( "02-with-xj/02-02-target.yaml");
    }

    @Test
    public void testFull() throws Exception {
        loadYamlRoute( "02-with-xj/02-03-full.yaml");
    }
}
