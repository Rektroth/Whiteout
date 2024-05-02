package io.github.rektroth.whiteout.accessors;

public interface CaptureTreeGenerationAccessor {
    default void whiteout$setCaptureTreeGeneration(boolean captureTreeGeneration) {
        // do nothing
    }

    default boolean whiteout$getCaptureTreeGeneration() {
        return false;
    }
}
