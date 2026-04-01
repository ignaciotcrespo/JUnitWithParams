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

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * JUnit {@link MethodRule} that enables parameterised tests using annotations instead of
 * a custom runner.  Because it is a rule rather than a runner, it is compatible with any
 * existing JUnit runner (Spring, Robolectric, Mockito, AndroidJUnit4, etc.).
 *
 * <h3>Supported annotations</h3>
 * <ul>
 *   <li>{@link WithParams} — explicit string value/names arrays</li>
 *   <li>{@link WithBooleanParams} — shorthand for {@code true} / {@code false}</li>
 *   <li>{@link WithNullParam} — one non-null value followed by {@code null}</li>
 *   <li>{@link WithEnumParams} — all constants of an enum class</li>
 *   <li>{@link WithParamsSource} — parameter sets provided by a static method</li>
 * </ul>
 *
 * <h3>Quick start</h3>
 * <pre>
 * public class CalculatorTest {
 *
 *     {@literal @}Rule
 *     public WithParamsRule params = new WithParamsRule();
 *
 *     {@literal @}Test
 *     {@literal @}WithParams({"2", "4", "8", "1000"})
 *     public void isEven() {
 *         assertTrue(calculator.isEven(params.asInt()));
 *     }
 * }
 * </pre>
 */
public class WithParamsRule implements MethodRule {

    @VisibleForTesting
    static final String PARAM_DEFAULT = "param1";

    @VisibleForTesting
    HashMap<String, String> paramsMap = new HashMap<String, String>();
    private HashSet<String> usedParams = new HashSet<String>();

    private Set<String> executedTests = new HashSet<String>();

    private ErrorCollector errorCollector = new ErrorCollector();

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new ParameterizedStatement(executedTests, paramsMap, method, base, errorCollector, usedParams, target);
    }

    /**
     * Returns the value of the default parameter ({@code "param1"}) as a {@link String}.
     *
     * @return the current parameter value, or {@code null} if {@code null} was injected
     * @throws RuntimeException if no parameter named {@code "param1"} exists
     */
    public String get() {
        return get(PARAM_DEFAULT);
    }

    /**
     * Returns the value of the named parameter as a {@link String}.
     *
     * @param name the parameter name
     * @return the current parameter value, or {@code null} if {@code null} was injected
     * @throws RuntimeException if no parameter with the given name exists
     */
    public String get(final String name) {
        if (paramsMap.containsKey(name)) {
            String value = paramsMap.get(name);
            usedParams.remove(name);
            return value;
        }
        throw new RuntimeException("Can't find parameter '" + name + "'");
    }

    /**
     * Returns the value of the default parameter parsed as an {@code int}.
     *
     * @return the current parameter value as an int
     * @throws NumberFormatException if the value cannot be parsed
     */
    public int asInt() {
        return asInt(PARAM_DEFAULT);
    }

    /**
     * Returns the value of the named parameter parsed as an {@code int}.
     *
     * @param name the parameter name
     * @return the current parameter value as an int
     * @throws NumberFormatException if the value cannot be parsed
     */
    public int asInt(final String name) {
        return Integer.parseInt(get(name));
    }

    /**
     * Returns the value of the default parameter parsed as a {@code long}.
     *
     * @return the current parameter value as a long
     * @throws NumberFormatException if the value cannot be parsed
     */
    public long asLong() {
        return asLong(PARAM_DEFAULT);
    }

    /**
     * Returns the value of the named parameter parsed as a {@code long}.
     *
     * @param name the parameter name
     * @return the current parameter value as a long
     * @throws NumberFormatException if the value cannot be parsed
     */
    public long asLong(final String name) {
        return Long.parseLong(get(name));
    }

    /**
     * Returns the value of the default parameter parsed as a {@code double}.
     *
     * @return the current parameter value as a double
     * @throws NumberFormatException if the value cannot be parsed
     */
    public double asDouble() {
        return asDouble(PARAM_DEFAULT);
    }

    /**
     * Returns the value of the named parameter parsed as a {@code double}.
     *
     * @param name the parameter name
     * @return the current parameter value as a double
     * @throws NumberFormatException if the value cannot be parsed
     */
    public double asDouble(final String name) {
        return Double.parseDouble(get(name));
    }

    /**
     * Returns the value of the default parameter parsed as a {@code float}.
     *
     * @return the current parameter value as a float
     * @throws NumberFormatException if the value cannot be parsed
     */
    public float asFloat() {
        return asFloat(PARAM_DEFAULT);
    }

    /**
     * Returns the value of the named parameter parsed as a {@code float}.
     *
     * @param name the parameter name
     * @return the current parameter value as a float
     * @throws NumberFormatException if the value cannot be parsed
     */
    public float asFloat(final String name) {
        return Float.parseFloat(get(name));
    }

    /**
     * Returns the value of the default parameter parsed as a {@code boolean}.
     * Any value other than {@code "true"} (case-insensitive) returns {@code false}.
     *
     * @return the current parameter value as a boolean
     */
    public boolean asBoolean() {
        return asBoolean(PARAM_DEFAULT);
    }

    /**
     * Returns the value of the named parameter parsed as a {@code boolean}.
     * Any value other than {@code "true"} (case-insensitive) returns {@code false}.
     *
     * @param name the parameter name
     * @return the current parameter value as a boolean
     */
    public boolean asBoolean(final String name) {
        return Boolean.parseBoolean(get(name));
    }

    /**
     * Converts the value of the default parameter using the provided {@link Transform}.
     *
     * @param <T>       the target type
     * @param transform the conversion function
     * @return the transformed value
     * @throws Exception if the transform throws
     */
    public <T> T as(Transform<T> transform) throws Exception {
        return as(PARAM_DEFAULT, transform);
    }

    /**
     * Converts the value of the named parameter using the provided {@link Transform}.
     *
     * @param <T>       the target type
     * @param name      the parameter name
     * @param transform the conversion function
     * @return the transformed value
     * @throws Exception if the transform throws
     */
    public <T> T as(String name, Transform<T> transform) throws Exception {
        return transform.to(get(name));
    }

    /**
     * Returns the value of the default parameter as an enum constant.
     * The stored string must match the {@link Enum#name()} of one of the constants.
     *
     * <p>Returns {@code null} if {@code null} was injected (e.g. via {@link WithNullParam}).
     *
     * @param <E>       the enum type
     * @param enumClass the enum class
     * @return the matching enum constant, or {@code null}
     * @throws IllegalArgumentException if the value does not match any constant name
     */
    public <E extends Enum<E>> E asEnum(Class<E> enumClass) {
        return asEnum(PARAM_DEFAULT, enumClass);
    }

    /**
     * Returns the value of the named parameter as an enum constant.
     * The stored string must match the {@link Enum#name()} of one of the constants.
     *
     * <p>Returns {@code null} if {@code null} was injected (e.g. via {@link WithNullParam}).
     *
     * @param <E>       the enum type
     * @param name      the parameter name
     * @param enumClass the enum class
     * @return the matching enum constant, or {@code null}
     * @throws IllegalArgumentException if the value does not match any constant name
     */
    public <E extends Enum<E>> E asEnum(String name, Class<E> enumClass) {
        String value = get(name);
        if (value == null) {
            return null;
        }
        return Enum.valueOf(enumClass, value);
    }

    /**
     * Functional interface for custom type conversions used with {@link #as(Transform)}.
     *
     * @param <T> the target type
     */
    public interface Transform<T> {
        /**
         * Converts a string parameter value to the target type.
         *
         * @param from the raw string value
         * @return the converted value
         */
        T to(String from);
    }

    @VisibleForTesting
    static class ParameterizedStatement extends Statement {
        private final FrameworkMethod mMethod;
        private final Statement mBase;
        private final ErrorCollector errorCollector;
        private Set<String> mExecutedTests;
        private HashMap<String, String> mParamsMap;
        private HashSet<String> usedParams;
        private final Object mTarget;

        ParameterizedStatement(final Set<String> executedTests, final HashMap<String, String> paramsMap,
                               final FrameworkMethod method, final Statement base, final ErrorCollector errorCollector,
                               HashSet<String> usedParams, Object target) {
            mMethod = method;
            mBase = base;
            this.mExecutedTests = executedTests;
            this.mParamsMap = paramsMap;
            this.errorCollector = errorCollector;
            this.usedParams = usedParams;
            this.mTarget = target;
        }

        @Override
        public void evaluate() throws Throwable {
            WithBooleanParams booleanParams = mMethod.getAnnotation(WithBooleanParams.class);
            WithNullParam nullParam = mMethod.getAnnotation(WithNullParam.class);
            WithEnumParams enumParams = mMethod.getAnnotation(WithEnumParams.class);
            WithParamsSource paramsSource = mMethod.getAnnotation(WithParamsSource.class);
            WithParams annotation = mMethod.getAnnotation(WithParams.class);

            if (booleanParams != null) {
                evaluateWithParams(createBooleanParamsAnnotation());
            } else if (nullParam != null) {
                evaluateWithNullParam(nullParam);
            } else if (enumParams != null) {
                evaluateWithEnumParams(enumParams);
            } else if (paramsSource != null) {
                evaluateWithParamsSource(paramsSource);
            } else if (annotation != null) {
                evaluateWithParams(annotation);
            } else {
                mBase.evaluate();
            }
        }

        private void evaluateWithParams(final WithParams annotation) throws Throwable {
            checkDuplicated();
            checkParameters(annotation);
            Iterator<String> values = Arrays.asList(annotation.value()).iterator();
            Iterator<String> names = prepareToExecute(annotation);
            int tests = 0;
            while (values.hasNext()) {
                mParamsMap.put(names.next(), values.next());
                if (noMore(names)) {
                    prepareUsedParams();
                    executeTest();
                    checkUsedParams();
                    names = prepareToExecute(annotation);
                    tests++;
                }
            }
            System.out.println("-- Passed " + (tests - errorCollector.getErrors()) + " of " + tests + " tests --\n");
            errorCollector.verify();
        }

        private void evaluateWithNullParam(final WithNullParam annotation) throws Throwable {
            checkDuplicated();
            String paramName = annotation.name();
            int errorsBefore = errorCollector.getErrors();

            // Iteration 1: provided non-null value
            mParamsMap.clear();
            mParamsMap.put(paramName, annotation.value());
            prepareUsedParams();
            executeTest();
            checkUsedParams();

            // Iteration 2: null value
            mParamsMap.clear();
            mParamsMap.put(paramName, null);
            prepareUsedParams();
            executeTest();
            checkUsedParams();

            int tests = 2;
            int errors = errorCollector.getErrors() - errorsBefore;
            System.out.println("-- Passed " + (tests - errors) + " of " + tests + " tests --\n");
            errorCollector.verify();
        }

        private void evaluateWithEnumParams(final WithEnumParams annotation) throws Throwable {
            checkDuplicated();
            String paramName = annotation.name();
            Enum<?>[] constants = annotation.value().getEnumConstants();
            int tests = 0;
            int errorsBefore = errorCollector.getErrors();

            for (Enum<?> constant : constants) {
                mParamsMap.clear();
                mParamsMap.put(paramName, constant.name());
                prepareUsedParams();
                executeTest();
                checkUsedParams();
                tests++;
            }

            int errors = errorCollector.getErrors() - errorsBefore;
            System.out.println("-- Passed " + (tests - errors) + " of " + tests + " tests --\n");
            errorCollector.verify();
        }

        private void evaluateWithParamsSource(final WithParamsSource annotation) throws Throwable {
            checkDuplicated();
            String methodName = annotation.value();
            String[] names = annotation.names();

            Class<?> targetClass = mTarget.getClass();
            Method sourceMethod;
            try {
                sourceMethod = targetClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                throw new WithParamsException("Source method '" + methodName + "()' not found in "
                        + targetClass.getName());
            }

            if (!Modifier.isStatic(sourceMethod.getModifiers())) {
                throw new WithParamsException("Source method '" + methodName + "()' must be static");
            }
            sourceMethod.setAccessible(true);

            String[][] rows;
            try {
                rows = (String[][]) sourceMethod.invoke(null);
            } catch (Exception e) {
                throw new WithParamsException("Failed to invoke source method '" + methodName + "()': "
                        + e.getMessage());
            }

            int tests = 0;
            int errorsBefore = errorCollector.getErrors();

            for (String[] row : rows == null ? new String[0][] : rows) {
                if (row.length != names.length) {
                    throw new WithParamsException("Row at index " + tests + " has " + row.length
                            + " element(s) but " + names.length + " name(s) were declared");
                }
                mParamsMap.clear();
                for (int i = 0; i < names.length; i++) {
                    mParamsMap.put(names[i], row[i]);
                }
                prepareUsedParams();
                executeTest();
                checkUsedParams();
                tests++;
            }

            int errors = errorCollector.getErrors() - errorsBefore;
            System.out.println("-- Passed " + (tests - errors) + " of " + tests + " tests --\n");
            errorCollector.verify();
        }

        @VisibleForTesting
        void checkUsedParams() throws WithParamsException {
            if (usedParams.size() > 0) {
                addError(new WithParamsException("Some parameters were never used! " + usedParams));
            }
        }

        private void prepareUsedParams() {
            usedParams.addAll(mParamsMap.keySet());
        }

        private void checkParameters(final WithParams annotation) throws WithParamsException {
            int namesLen = annotation.names().length;
            int valuesLen = annotation.value().length;
            if (valuesLen % namesLen != 0) {
                throw new WithParamsException("Invalid number of parameters. Check you added " + namesLen +
                        " values on each group");
            }
        }

        private boolean noMore(final Iterator<String> names) {
            return !names.hasNext();
        }

        private Iterator<String> prepareToExecute(final WithParams annotation) {
            List<String> namesList = Arrays.asList(annotation.names());
            Iterator<String> names = namesList.iterator();
            mParamsMap.clear();
            return names;
        }

        private void executeTest() throws WithParamsException {
            try {
                mBase.evaluate();
                mExecutedTests.add(mMethod.getName());
                showPassed();
            } catch (Throwable exc) {
                addError(exc);
            }
        }

        private void showPassed() {
            System.out.println("Passed " + mParamsMap);
        }

        @VisibleForTesting
        void addError(final Throwable exc) throws WithParamsException {
            String message = exc.getMessage();
            errorCollector.addError(new WithParamsException(formatted("Fail! " + mMethod.getName() +
                    "() -> " + mParamsMap
                    + (message == null ? "" : "\n" +
                    message)
            ), exc));
        }

        private void checkDuplicated() throws WithParamsException {
            if (mExecutedTests.contains(mMethod.getName())) {
                throw new WithParamsException("Test '" + mMethod.getName() +
                        "()' already executed with parameters. Don't add more than one WithParamsRule in the class.");
            }
        }

        private String formatted(final String desc) {
            if (!desc.isEmpty()) {
                return "\n[\n" + desc + "\n]\n";
            }
            return desc;
        }
    }

    private static WithParams createBooleanParamsAnnotation() {
        return new WithParams() {
            @Override
            public Class<WithParams> annotationType() {
                return WithParams.class;
            }

            @Override
            public String[] names() {
                return new String[]{PARAM_DEFAULT};
            }

            @Override
            public String[] value() {
                return new String[]{"true", "false"};
            }
        };
    }
}
