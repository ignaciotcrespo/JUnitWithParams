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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameterised test annotation that runs the test twice: once with the provided
 * non-null string value and once with {@code null}.
 *
 * <p>This is useful for null-safety testing — it ensures that code handles both a
 * valid value and {@code null} without requiring you to write two separate tests.
 *
 * <h3>Single-parameter example</h3>
 * <pre>
 * {@literal @}Test
 * {@literal @}WithNullParam("hello")
 * public void handlesNull() {
 *     String value = params.get(); // "hello" on first run, null on second
 *     // assert whatever makes sense for both cases
 * }
 * </pre>
 *
 * <h3>Named-parameter example</h3>
 * <pre>
 * {@literal @}Test
 * {@literal @}WithNullParam(value = "hello", name = "input")
 * public void handlesNullNamed() {
 *     String value = params.get("input");
 * }
 * </pre>
 *
 * @see WithParamsRule#get()
 * @see WithBooleanParams
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WithNullParam {

    /**
     * The non-null value to inject on the first test iteration.
     *
     * @return the string value
     */
    String value();

    /**
     * The parameter name used to retrieve the value in the test body via
     * {@link WithParamsRule#get(String)}.  Defaults to {@code "param1"}.
     *
     * @return the parameter name
     */
    String name() default "param1";
}
