package io.github.r8zorg.jdatools;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class OwnersRegistry {
    private static final Set<Long> ownerIds = new CopyOnWriteArraySet<>();

    public static boolean isOwner(long userId) {
        return ownerIds.contains(userId);
    }

    public static void addOwner(long userId) {
        ownerIds.add(userId);
    }

    public static void removeOwner(long userId) {
        ownerIds.remove(userId);
    }
}
