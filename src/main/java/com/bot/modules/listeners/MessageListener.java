package com.bot.modules.listeners;

import com.bot.core.annotations.EventListeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@EventListeners
public class MessageListener extends ListenerAdapter {
    final static Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        logger.info("{} wrote: {}", event.getAuthor().getName(), message);
    }
}
