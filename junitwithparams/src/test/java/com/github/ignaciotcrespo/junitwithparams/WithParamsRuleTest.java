/**
 * The MIT License
 * <p>
 * Copyright (c) 2017, Ignacio Tomas Crespo (itcrespo@gmail.com)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.ignaciotcrespo.junitwithparams;

import com.github.ignaciotcrespo.junitwithparams.WithParamsRule.ParameterizedStatement;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link WithParamsRule}.
 */
public class WithParamsRuleTest {

    private WithParamsRule rule;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        rule = new WithParamsRule();
    }

    @Test
    public void apply() throws Exception {
        Statement statement = rule.apply(mock(Statement.class), mock(FrameworkMethod.class), mock(Object.class));

        assertThat(statement).isInstanceOf(ParameterizedStatement.class);
    }

    @Test
    public void get_default_exception() throws Exception {
        thrown.expect(RuntimeException.class);

        rule.get();
    }

    @Test
    public void get_default() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "");

        String value = rule.get();

        assertThat(value).isEmpty();
    }

    @Test
    public void get_name_exception() throws Exception {
        thrown.expect(RuntimeException.class);

        rule.get("anykey");
    }

    @Test
    public void get_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "value");

        String value = rule.get("anykey");

        assertThat(value).isEqualTo("value");
    }

    @Test
    public void asInt_notNumber() throws Exception {
        thrown.expect(NumberFormatException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "text");

        rule.asInt();
    }

    @Test
    public void asInt() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "45");

        int value = rule.asInt();

        assertThat(value).isEqualTo(45);
    }

    @Test
    public void asInt_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("key", "45");

        int value = rule.asInt("key");

        assertThat(value).isEqualTo(45);
    }

    @Test
    public void asInt_name_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "45");

        rule.asInt("key");
    }

    @Test
    public void asLong_notNumber() throws Exception {
        thrown.expect(NumberFormatException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "text");

        rule.asLong();
    }

    @Test
    public void asLong() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "45");

        long value = rule.asLong();

        assertThat(value).isEqualTo(45L);
    }

    @Test
    public void asLong_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("key", "45");

        long value = rule.asLong("key");

        assertThat(value).isEqualTo(45L);
    }

    @Test
    public void asLong_name_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "45");

        rule.asLong("key");
    }

    @Test
    public void asDouble_notNumber() throws Exception {
        thrown.expect(NumberFormatException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "text");

        rule.asDouble();
    }

    @Test
    public void asDouble() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "45.3");

        double value = rule.asDouble();

        assertThat(value).isEqualTo(45.3D);
    }

    @Test
    public void asDouble_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("key", "45.3");

        double value = rule.asDouble("key");

        assertThat(value).isEqualTo(45.3D);
    }

    @Test
    public void asDouble_name_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "45.3");

        rule.asDouble("key");
    }

    @Test
    public void asFloat_notNumber() throws Exception {
        thrown.expect(NumberFormatException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "text");

        rule.asFloat();
    }

    @Test
    public void asFloat() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "45.3");

        float value = rule.asFloat();

        assertThat(value).isEqualTo(45.3F);
    }

    @Test
    public void asFloat_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("key", "45.3");

        float value = rule.asFloat("key");

        assertThat(value).isEqualTo(45.3F);
    }

    @Test
    public void asFloat_name_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "45.3");

        rule.asFloat("key");
    }

    @Test
    public void asBoolean_notBoolean() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "text");

        boolean value = rule.asBoolean();

        assertThat(value).isFalse();
    }

    @Test
    public void asBoolean_true() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "true");

        boolean value = rule.asBoolean();

        assertThat(value).isTrue();
    }

    @Test
    public void asBoolean_false() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "false");

        boolean value = rule.asBoolean();

        assertThat(value).isFalse();
    }

    @Test
    public void asBoolean_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("key", "true");

        boolean value = rule.asBoolean("key");

        assertThat(value).isTrue();
    }

    @Test
    public void asBoolean_name_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "true");

        rule.asBoolean("key");
    }

    @Test
    public void as_default() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put(WithParamsRule.PARAM_DEFAULT, "45");

        AtomicInteger value = rule.as(TRANSFORM);

        assertThat(value.intValue()).isEqualTo(new AtomicInteger(45).intValue());
    }

    @Test
    public void as_name() throws Exception {
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("key", "45");

        AtomicInteger value = rule.as("key", TRANSFORM);

        assertThat(value.intValue()).isEqualTo(new AtomicInteger(45).intValue());
    }

    @Test
    public void as_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        rule.paramsMap = new HashMap<>();
        rule.paramsMap.put("anykey", "45");

        rule.as("key", TRANSFORM);
    }

    private static final WithParamsRule.Transform<AtomicInteger> TRANSFORM = new WithParamsRule.Transform<AtomicInteger>() {
        @Override
        public AtomicInteger to(final String from) {
            return new AtomicInteger(Integer.parseInt(from));
        }
    };

}