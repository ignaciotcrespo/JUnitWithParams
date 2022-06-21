package com.github.ignaciotcrespo.junitwithparams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class WithBooleanParamsTest {

    @Rule
    public WithParamsRule params = new WithParamsRule();

    private static boolean testedTrue = false;
    private static boolean testedFalse = false;

    @Test
    @WithBooleanParams
    public void testedForEachValue() {
        boolean param = params.asBoolean();

        if (testedTrue && param) fail("True was already tested!");
        if (testedFalse && !param) fail("False was already tested!");

        if (!testedTrue && param) testedTrue = true;
        if (!testedFalse && !param) testedFalse = true;

        assertEquals(param, params.asBoolean());
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        testedTrue = false;
        testedFalse = false;
    }

    @AfterClass
    public static void afterClass() throws Exception {
        assertTrue(testedTrue);
        assertTrue(testedFalse);
    }
}
