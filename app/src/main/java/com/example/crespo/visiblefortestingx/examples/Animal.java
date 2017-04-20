package com.example.crespo.visiblefortestingx.examples;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by crespo on 3/25/17.
 */
public class Animal {

    private String changedValue;

    protected long methodTwoParams(int steps, long distance) {
        return steps + distance;
    }

    void methodReturnVoid(String text) throws RuntimeException, IOException {
        changedValue = text;
    }

    private String methodWithParamGenerics(Map<Integer, Calendar> map) {
        return "yes!";
    }

    private <T> Class<T> wtf(Class<T> clazz) throws IOException{
        return clazz;
    }

    private <T extends List, V extends Set> Map<T, V> methodWithReturnGenericVariables(Class<T> clazz, V v) throws IllegalAccessException, InstantiationException {
        HashMap<T, V> map = new HashMap<>();
        map.put(clazz.newInstance(), v);
        return map;
    }

    private <T extends List, V extends Set> Map<T, V> methodWithNestedGenericsInParam(Vector<Iterator<T>> clazz, V v) throws IllegalAccessException, InstantiationException {
        return new HashMap<>();
    }

    private <T extends List, V> Map<T, V> methodWithGenericWildcard(Class<T> clazz, V v) throws IllegalAccessException, InstantiationException {
        HashMap<T, V> map = new HashMap<>();
        map.put(clazz.newInstance(), v);
        return map;
    }

}
