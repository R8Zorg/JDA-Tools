package com.bot.modules.annotations;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@FunctionalInterface
public interface OptionExtractor {
    Object extract(OptionMapping mapping);
}
