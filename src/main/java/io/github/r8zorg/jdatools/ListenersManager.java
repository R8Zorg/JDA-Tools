package io.github.r8zorg.jdatools;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.r8zorg.jdatools.annotations.EventListeners;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 *
 * Call {@link #getAllListeners()} to register all listeners
 */
public class ListenersManager {
    private final Logger logger = LoggerFactory.getLogger(ListenersManager.class);
    private final String packagesPath;
    private final CommandsManager commandsManager;

    /**
     * 
     * @param packagesPath Path to directory with classes containing listeners
    */
    public ListenersManager(String packagesPath, CommandsManager commandsManager) {
        this.packagesPath = packagesPath;
        this.commandsManager = commandsManager;
    }

    /**
     * Returns all listener classes annotated with
     * {@link io.github.r8zorg.jdatools.annotations.EventListeners}
     */
    public Object[] getAllListeners() {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(packagesPath)
                .scan()) {
            List<Object> listeners = new ArrayList<>();
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(EventListeners.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                if (EventListener.class.isAssignableFrom(clazz)) {
                    try {
                        listeners.add(clazz.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        logger.error(e.toString());
                    }
                }
            }
            listeners.add(new SlashCommandsHandler(commandsManager));
            return listeners.toArray(new EventListener[0]);
        }
    }
}
