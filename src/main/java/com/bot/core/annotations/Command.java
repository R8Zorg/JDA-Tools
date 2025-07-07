package com.bot.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.dv8tion.jda.api.interactions.InteractionContextType;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name() default "";
    String description() default "Description not provided.";
    InteractionContextType type() default InteractionContextType.GUILD;
    int order() default 1;
}

