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
 * Parameterised test annotation that automatically iterates over every constant of an
 * enum class.  This eliminates the need to list each enum value as a string in
 * {@link WithParams}, and keeps tests resilient to future enum additions.
 *
 * <p>Each enum constant is stored by its {@link Enum#name()} and can be retrieved in
 * the test body via {@link WithParamsRule#asEnum(Class)}.
 *
 * <h3>Example</h3>
 * <pre>
 * enum Color { RED, GREEN, BLUE }
 *
 * {@literal @}Test
 * {@literal @}WithEnumParams(Color.class)
 * public void testAllColors() {
 *     Color color = params.asEnum(Color.class);
 *     assertNotNull(color);
 * }
 * </pre>
 *
 * <h3>Named-parameter example</h3>
 * <pre>
 * {@literal @}Test
 * {@literal @}WithEnumParams(value = Color.class, name = "color")
 * public void testAllColorsNamed() {
 *     Color color = params.asEnum("color", Color.class);
 * }
 * </pre>
 *
 * @see WithParamsRule#asEnum(Class)
 * @see WithParamsRule#asEnum(String, Class)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WithEnumParams {

    /**
     * The enum class whose constants will be iterated.
     *
     * @return the enum class
     */
    Class<? extends Enum<?>> value();

    /**
     * The parameter name used to retrieve the current constant in the test body via
     * {@link WithParamsRule#asEnum(Class)} or {@link WithParamsRule#asEnum(String, Class)}.
     * Defaults to {@code "param1"}.
     *
     * @return the parameter name
     */
    String name() default "param1";
}
