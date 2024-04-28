/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.block;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin {
	@Final
	@Shadow
	protected SaplingGenerator generator;

	@Redirect(
		at = @At(
			target = "Lnet/minecraft/block/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z",
			value = "INVOKE"
		),
		method = "generate"
	)
	private boolean dontDestroyPermanentBlocksWhenGrowing(SaplingGenerator instance, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random) {
		if (((CaptureTreeGenerationAccessor)world).whiteout$getCaptureTreeGeneration()) {
			return this.generator.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
		} else {
			((CaptureTreeGenerationAccessor)world).whiteout$setCaptureTreeGeneration(true);
			boolean b = this.generator.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
			((CaptureTreeGenerationAccessor)world).whiteout$setCaptureTreeGeneration(false);
			return b;
		}
	}
}
