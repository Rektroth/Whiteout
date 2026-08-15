package io.github.rektroth.whiteout.accessors;

/**
 * Accessor for the custom `captureTreeGeneration` field.
 */
public interface CaptureTreeGenerationAccessor {
    default void whiteout$setCaptureTreeGeneration(boolean captureTreeGeneration) {
        // do nothing
    }

    default boolean whiteout$getCaptureTreeGeneration() {
        return false;
    }
}
