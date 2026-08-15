package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Sapling block modifications to prevent breaking permanent blocks.
 */
@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin {
	/**
	 * Captures tree generation when the sapling grows.
	 * @param instance  The tree grower.
	 * @param level     The world the sapling is in.
	 * @param generator boilerplate
	 * @param pos       The position of the sapling.
	 * @param state     The state of the sapling.
	 * @param random    Random tick source.
	 * @return True if a tree generates, false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/block/grower/TreeGrower;growTree(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Z",
			value = "INVOKE"
		),
		method = "advanceTree"
	)
	private boolean doNotDestroyPermanentBlocksWhenGrowing(TreeGrower instance, ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource random) {
		if (((CaptureTreeGenerationAccessor)level).whiteout$getCaptureTreeGeneration()) {
			return instance.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
		} else {
			((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(true);
			boolean b = instance.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
			((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(false);
			return b;
		}
	}
}
