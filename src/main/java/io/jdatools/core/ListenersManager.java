package io.jdatools.core;

import io.jdatools.core.annotations.EventListeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 *
 * Call {@link #registerListeners(JDA)} to register all listeners
 */
public class ListenersManager {
    private final Logger logger = LoggerFactory.getLogger(ListenersManager.class);
    private final String packagesPath;

    /**
     * 
     * @param packagesPath Path to directory with classes containing listeners
    */
    public ListenersManager(String packagesPath) {
        this.packagesPath = packagesPath;
    }

    /**
     * Returns all listener classes annotated with
     * {@link io.jdatools.core.annotations.EventListeners}
     */
    public void registerListeners(JDA jda) {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(packagesPath)
                .scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(EventListeners.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                if (EventListener.class.isAssignableFrom(clazz)) {
                    try {
                        jda.addEventListener(clazz.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        logger.error(e.toString());
                    }
                }
            }
        }
    }
}
