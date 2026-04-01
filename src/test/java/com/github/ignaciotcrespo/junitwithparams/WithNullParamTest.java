package com.github.ignaciotcrespo.junitwithparams;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration tests for {@link WithNullParam}.
 *
 * <p>Verifies that the annotation runs the test exactly twice: once with the non-null
 * value and once with {@code null}, both for the default parameter name and for a
 * custom named parameter.
 */
public class WithNullParamTest {

    @Rule
    public WithParamsRule params = new WithParamsRule();

    // --- state for testedForEachValue ---
    private static boolean testedNonNull;
    private static boolean testedNull;

    // --- state for testedForEachValueNamed ---
    private static boolean testedNonNullNamed;
    private static boolean testedNullNamed;

    // --- state for nullIterationReturnsNull ---
    private static int nullCount;

    @BeforeClass
    public static void beforeClass() {
        testedNonNull = false;
        testedNull = false;
        testedNonNullNamed = false;
        testedNullNamed = false;
        nullCount = 0;
    }

    @Test
    @WithNullParam("hello")
    public void testedForEachValue() {
        String value = params.get();

        if (testedNonNull && "hello".equals(value)) fail("Non-null was already tested!");
        if (testedNull && value == null) fail("Null was already tested!");

        if (!testedNonNull && "hello".equals(value)) testedNonNull = true;
        if (!testedNull && value == null) testedNull = true;
    }

    @Test
    @WithNullParam(value = "world", name = "input")
    public void testedForEachValueNamed() {
        String value = params.get("input");

        if (testedNonNullNamed && "world".equals(value)) fail("Non-null named was already tested!");
        if (testedNullNamed && value == null) fail("Null named was already tested!");

        if (!testedNonNullNamed && "world".equals(value)) testedNonNullNamed = true;
        if (!testedNullNamed && value == null) testedNullNamed = true;
    }

    @Test
    @WithNullParam("something")
    public void nullIterationReturnsNullValue() {
        String value = params.get();
        if (value == null) {
            nullCount++;
            assertNull(value);
        } else {
            assertEquals("something", value);
        }
    }

    @AfterClass
    public static void afterClass() {
        assertTrue("Non-null value was never tested", testedNonNull);
        assertTrue("Null value was never tested", testedNull);
        assertTrue("Non-null named value was never tested", testedNonNullNamed);
        assertTrue("Null named value was never tested", testedNullNamed);
        assertEquals("Expected exactly one null iteration in nullIterationReturnsNullValue", 1, nullCount);
    }
}
