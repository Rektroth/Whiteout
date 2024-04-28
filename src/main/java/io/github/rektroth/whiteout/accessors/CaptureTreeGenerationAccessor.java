/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.accessors;

public interface CaptureTreeGenerationAccessor {
    default void whiteout$setCaptureTreeGeneration(boolean captureTreeGeneration) {
        // do nothing
    }

    default boolean whiteout$getCaptureTreeGeneration() {
        return false;
    }
}
