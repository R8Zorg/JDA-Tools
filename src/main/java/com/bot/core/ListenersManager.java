package com.bot.core;

import com.bot.core.annotations.EventListeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;

public class ListenersManager {
    private final Logger logger = LoggerFactory.getLogger(ListenersManager.class);
    private final String packagesPath;

    public ListenersManager(String packagesPath) {
        this.packagesPath = packagesPath;
    }

    /**
     * Register all listener classes annotated with {@link com.bot.core.annotations.EventListeners}
     * @param jda {@link JDA}
     */
    public void RegisterAllListeners(JDA jda) {
        ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(packagesPath)
                .scan();
        for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(EventListeners.class.getName())) {
            Class<?> clazz = classInfo.loadClass();
            if (EventListener.class.isAssignableFrom(clazz)) {
                try {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    jda.addEventListener(instance);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }
}
