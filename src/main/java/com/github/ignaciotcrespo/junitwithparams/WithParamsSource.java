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
 * Parameterised test annotation that reads parameter sets from a static method in the
 * test class, avoiding the need to embed large value arrays directly on the annotation.
 *
 * <p>The provider method must:
 * <ul>
 *   <li>be {@code static}</li>
 *   <li>accept no arguments</li>
 *   <li>return {@code String[][]}, where each inner array represents one test iteration</li>
 * </ul>
 *
 * <p>The {@link #names()} attribute maps the column indices of each row to parameter
 * names that can be read in the test body.  The number of names must equal the length
 * of every row returned by the provider.
 *
 * <h3>Single-parameter example</h3>
 * <pre>
 * {@literal @}Test
 * {@literal @}WithParamsSource("provideWords")
 * public void wordLength() {
 *     String word = params.get();
 *     assertTrue(word.length() &gt; 0);
 * }
 *
 * static String[][] provideWords() {
 *     return new String[][] { {"hello"}, {"world"}, {"foo"} };
 * }
 * </pre>
 *
 * <h3>Multiple-parameter example</h3>
 * <pre>
 * {@literal @}Test
 * {@literal @}WithParamsSource(value = "provideNumbers", names = {"n1", "n2", "result"})
 * public void sum() {
 *     int n1 = params.asInt("n1");
 *     int n2 = params.asInt("n2");
 *     assertEquals(params.asInt("result"), calculator.sum(n1, n2));
 * }
 *
 * static String[][] provideNumbers() {
 *     return new String[][] {
 *         {"1",  "2",  "3"},
 *         {"11", "-2", "9"}
 *     };
 * }
 * </pre>
 *
 * @see WithParamsRule
 * @see WithParams
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WithParamsSource {

    /**
     * Name of the static, no-argument method in the test class that provides the
     * parameter sets as a {@code String[][]}.
     *
     * @return the provider method name
     */
    String value();

    /**
     * Parameter names that map to the columns in each row returned by the provider
     * method.  The number of names must equal the number of elements in every row.
     * Defaults to a single name {@code "param1"}.
     *
     * @return the parameter names
     */
    String[] names() default {"param1"};
}
