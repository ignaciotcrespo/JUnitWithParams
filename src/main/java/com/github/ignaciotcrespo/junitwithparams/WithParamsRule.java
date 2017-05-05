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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by crespo on 4/15/17.
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
        return new ParameterizedStatement(executedTests, paramsMap, method, base, errorCollector, usedParams);
    }

    public String get(final String name) {
        if (paramsMap.containsKey(name)) {
            String value = paramsMap.get(name);
            usedParams.remove(name);
            return value;
        }
        throw new RuntimeException("Can't find parameter '" + name + "'");
    }

    public String get() {
        return get(PARAM_DEFAULT);
    }

    public int asInt(final String name) {
        return Integer.parseInt(get(name));
    }

    public int asInt() {
        return asInt(PARAM_DEFAULT);
    }

    public long asLong(final String name) {
        return Long.parseLong(get(name));
    }

    public long asLong() {
        return asLong(PARAM_DEFAULT);
    }

    public double asDouble(final String name) {
        return Double.parseDouble(get(name));
    }

    public double asDouble() {
        return asDouble(PARAM_DEFAULT);
    }

    public float asFloat(final String name) {
        return Float.parseFloat(get(name));
    }

    public float asFloat() {
        return asFloat(PARAM_DEFAULT);
    }

    public boolean asBoolean(final String name) {
        return Boolean.parseBoolean(get(name));
    }

    public boolean asBoolean() {
        return asBoolean(PARAM_DEFAULT);
    }

    public <T> T as(Transform<T> transform) throws Exception {
        return as(PARAM_DEFAULT, transform);
    }

    public <T> T as(String name, Transform<T> transform) throws Exception {
        return transform.to(get(name));
    }

    public interface Transform<T> {
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

        ParameterizedStatement(final Set<String> executedTests, final HashMap<String, String> paramsMap,
                               final FrameworkMethod method, final Statement base, final ErrorCollector errorCollector,
                               HashSet<String> usedParams) {
            mMethod = method;
            mBase = base;
            this.mExecutedTests = executedTests;
            this.mParamsMap = paramsMap;
            this.errorCollector = errorCollector;
            this.usedParams = usedParams;
        }

        @Override
        public void evaluate() throws Throwable {
            WithParams annotation = mMethod.getAnnotation(WithParams.class);
            if (annotation != null) {
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
            } else {
                mBase.evaluate();
            }
        }

        private void checkUsedParams() throws WithParamsException {
            if (usedParams.size() > 0) {
                throw new WithParamsException("Some parameters were never used! " + usedParams);
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

        private void addError(final Throwable exc) throws WithParamsException {
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

}
