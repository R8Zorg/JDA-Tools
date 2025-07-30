package io.github.r8zorg.jdatools;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


    /**
     * Class for &#64;OwnerOnly annotation support.<br>
     * Contains Set<Long> ownerIds = new CopyOnWriteArraySet<>()<br>
     * So do not use addOwner or removeOwner often. It is not designed for that
     * 
     * 
    */
public class OwnersRegistry {
    private static final Set<Long> ownerIds = new CopyOnWriteArraySet<>();

    /**
     * Check if user is bot's owner
     * @param userId
     * @return
    */
    public static boolean isOwner(long userId) {
        return ownerIds.contains(userId);
    }

    /**
     * Clear ownerIds Set and add provided ids
     * @param ids
    */
    public static void setOwners(Collection<Long> ids) {
        ownerIds.clear();
        ownerIds.addAll(ids);
    }

    /**
     * Add userId to owner ids
     * @param userId
    */
    public static void addOwner(long userId) {
        ownerIds.add(userId);
    }

    /**
     * Remove userId from owner ids<br>
     * @param userId
    */
    public static void removeOwner(long userId) {
        ownerIds.remove(userId);
    }
}
