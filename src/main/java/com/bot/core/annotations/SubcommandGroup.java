package com.bot.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubcommandGroup {
    String parentName();
    String name() default "";
    String description() default "Description not provided.";
    int order() default 2;
}

