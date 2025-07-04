package com.bot.modules.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubcommandGroup {
    String parent();
    String name();
    String description();
}

