package com.example.crespo.visiblefortestingx.examples.zoo;

import com.example.crespo.visiblefortestingx.examples.Animal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by crespo on 3/25/17.
 */

public class Dog extends Animal {

    private final String finalField = "wadafac";

    private List<Date> fieldWithGenerics;

    private InnerPrivate fieldWithPrivateType;

    private List<InnerPrivate> fieldWithPrivateGeneric;

    private int methodWithoutArguments() {
        return 3;
    }

    private InnerPrivate methodWithPrivateReturn() {
        return new InnerPrivate();
    }

    private Map<String, InnerPrivate> methodWithReturnWithPrivateGenerics() {
        return new HashMap<>();
    }

    private String methodWithPrivateParam(Integer num, InnerPrivate paramPrivate) {
        return paramPrivate.toString();
    }

    private static int staticMethod(String text){
        return 9;
    }

    private static class InnerPrivate {

    }
}
