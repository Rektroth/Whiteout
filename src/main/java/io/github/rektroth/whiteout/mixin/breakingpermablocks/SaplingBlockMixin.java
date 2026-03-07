package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin {
	/**
	 * Captures tree generation before the sapling grows.
	 * @param level  The world the sapling is in.
	 * @param pos    The position of the sapling.
	 * @param state  The state of the sapling.
	 * @param random Random tick source.
	 * @param ci     boilerplate
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/level/block/grower/TreeGrower;growTree(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Z",
			value = "INVOKE"
		),
		method = "advanceTree"
	)
	private void setCaptureTreeGenerationTrue(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, CallbackInfo ci) {
		if (((CaptureTreeGenerationAccessor)level).whiteout$getCaptureTreeGeneration()) {
			((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(true);
		}
	}

	/**
	 * Captures tree generation after the sapling grows.
	 * @param level  The world the sapling is in.
	 * @param pos    The position of the sapling.
	 * @param state  The state of the sapling.
	 * @param random Random tick source.
	 * @param ci     boilerplate
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/level/block/grower/TreeGrower;growTree(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Z",
			value = "INVOKE_ASSIGN"
		),
		method = "advanceTree"
	)
	private void setCaptureTreeGenerationFalse(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, CallbackInfo ci) {
		if (((CaptureTreeGenerationAccessor)level).whiteout$getCaptureTreeGeneration()) {
			((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(false);
		}
	}
}
