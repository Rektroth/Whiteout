/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(ExplosionImpl.class)
public class ExplosionMixin {
	@Final
	@Shadow
	private ExplosionBehavior behavior;

	@Final
	@Shadow
	private float power;

	@Final
	@Shadow
	private ServerWorld world;

	@Final
	@Shadow
	private Vec3d pos;

	/**
	 * Skips the existing loop.
	 * @param prevValue boilerplate
	 */
	@ModifyConstant(constant = @Constant(floatValue = 0.0F, ordinal = 0), method = "getBlocksToDestroy")
	private float skipLoop(float prevValue) {
		return Float.MAX_VALUE;
	}

	/**
	 * Modified loop that gets skipped if the block being checked is not destroyable.
	 * @param cir boilerplate
	 * @param set The set of positions of blocks to destroy.
	 * @param d   d
	 * @param e   e
	 * @param f   f
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/util/math/random/Random;nextFloat()F",
			value = "INVOKE_ASSIGN"
		),
		method = "getBlocksToDestroy"
	)
	private void newLoop(
		CallbackInfoReturnable<List<BlockPos>> cir,
		@Local LocalRef<Set<BlockPos>> set,
		@Local(ordinal = 0) double d,
		@Local(ordinal = 1) double e,
		@Local(ordinal = 2) double f
	) {
		Set<BlockPos> newSet = set.get();

		// these 4 already exist in the base class as local variables,
		// but Fabric claims it can't find them - so I must redefine them here
		float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
		double m = this.pos.x;
		double n = this.pos.y;
		double o = this.pos.z;

		for (float p = 0.3F; h > 0.0F; h -= 0.22500001F) {
			BlockPos blockPos = BlockPos.ofFloored(m, n, o);
			BlockState blockState = this.world.getBlockState(blockPos);

			if (!BlockUtil.isDestroyable(blockState.getBlock())) {
				continue;
			}

			FluidState fluidState = this.world.getFluidState(blockPos);

			if (!this.world.isInBuildLimit(blockPos)) {
				break;
			}

			Optional<Float> optional = this.behavior.getBlastResistance((Explosion)this, this.world, blockPos, blockState, fluidState);

			if (optional.isPresent()) {
				h -= (optional.get() + 0.3F) * 0.3F;
			}

			if (h > 0.0F && this.behavior.canDestroyBlock((Explosion)this, this.world, blockPos, blockState, h)) {
				newSet.add(blockPos);
				set.set(newSet);
			}

			m += d * 0.30000001192092896;
			n += e * 0.30000001192092896;
			o += f * 0.30000001192092896;
		}
	}
}
