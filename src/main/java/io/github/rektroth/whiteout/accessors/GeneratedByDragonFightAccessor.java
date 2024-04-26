/*
 * Patch for MC-108513
 *
 * Authored for CraftBukkit/Spigot by Max Lee <max@themoep.de> on May 27, 2021.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.accessors;

public interface GeneratedByDragonFightAccessor {
    default void whiteout$setGeneratedByDragonFight(boolean generatedByDragonFight) {
        // do nothing
    }
}
