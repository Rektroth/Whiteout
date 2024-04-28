package io.github.rektroth.whiteout.mixin.server.world;

import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageInvoker {
	@Invoker("shouldTick")
	boolean invokeShouldTick(ChunkPos pos);
}
