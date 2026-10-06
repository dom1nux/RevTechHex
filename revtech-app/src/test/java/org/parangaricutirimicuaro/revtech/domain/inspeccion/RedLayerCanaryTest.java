package org.parangaricutirimicuaro.revtech.domain.inspeccion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class RedLayerCanaryTest {
    @Test
    void falla() { fail("canary: this layer must block the stack merge"); }
}
