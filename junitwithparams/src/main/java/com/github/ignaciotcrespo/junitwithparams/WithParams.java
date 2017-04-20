package com.github.ignaciotcrespo.junitwithparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by crespo on 4/15/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WithParams {

    String[] names() default {"param1"};

    String[] value() default {};
}
