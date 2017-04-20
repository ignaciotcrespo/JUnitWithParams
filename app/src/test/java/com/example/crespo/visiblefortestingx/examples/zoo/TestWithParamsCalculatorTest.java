package com.example.crespo.visiblefortestingx.examples.zoo;

import com.github.ignaciotcrespo.junitwithparams.WithParams;
import com.github.ignaciotcrespo.junitwithparams.WithParamsRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by crespo on 4/4/17.
 */
public class TestWithParamsCalculatorTest {

    @Rule
    public WithParamsRule params = new WithParamsRule();

    @Test
    @WithParams(
            names = {"n1", "n2", "result"},
            value = {
                    "1", "2", "3",
                    "11", "-2", "9",
                    "0", "0", "0",
            }
    )
    public void sum() {
        int n1 = params.asInt("n1");
        int n2 = params.asInt("n2");
        int result = params.asInt("result");

        assertEquals(result, n1 + n2);
    }

    @Test
    @WithParams({"2", "4", "1486"})
    public void even() throws Exception {
        assertTrue(params.asInt() % 2 == 0);
    }

}
