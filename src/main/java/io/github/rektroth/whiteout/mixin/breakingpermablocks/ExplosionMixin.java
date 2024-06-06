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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Final
	@Shadow
	private ObjectArrayList<BlockPos> affectedBlocks;

	@Final
	@Shadow
	private Map<PlayerEntity, Vec3d> affectedPlayers;

	@Final
	@Shadow
	private ExplosionBehavior behavior;

	@Final
	@Shadow
	private DamageSource damageSource;

	@Final
	@Shadow
	private Entity entity;

	@Final
	@Shadow
	private float power;

	@Final
	@Shadow
	private World world;

	@Final
	@Shadow
	private double x;

	@Final
	@Shadow
	private double y;

	@Final
	@Shadow
	private double z;

	@ModifyConstant(constant = @Constant(floatValue = 0.0F, ordinal = 0), method = "collectBlocksAndDamageEntities")
	private float skipLoop(float prevValue) {
		return Float.MAX_VALUE;
	}

	@Inject(
		at = @At(
			by = 4,
			shift = At.Shift.BY,
			target = "Lnet/minecraft/util/math/random/Random;nextFloat()F",
			value = "INVOKE"
		),
		method = "collectBlocksAndDamageEntities"
	)
	private void newLoop(
		CallbackInfo ci,
		@Local LocalRef<Set<BlockPos>> set,
		@Local(ordinal = 0) float h,
		@Local(ordinal = 0) double d,
		@Local(ordinal = 1) double e,
		@Local(ordinal = 2) double f,
		@Local(ordinal = 4) double m,
		@Local(ordinal = 5) double n,
		@Local(ordinal = 6) double o
	) {
		Set<BlockPos> newSet = set.get();
		float h2 = h;
		double m2 = m;
		double n2 = n;
		double o2 = o;

		for (float p = 0.3F; h2 > 0.0F; h2 -= 0.22500001F) {
			BlockPos blockPos = BlockPos.ofFloored(m2, n2, o2);
			BlockState blockState = this.world.getBlockState(blockPos);

			if (BlockUtil.isDestroyable(blockState.getBlock())) {
				continue;
			}

			FluidState fluidState = this.world.getFluidState(blockPos);

			if (!this.world.isInBuildLimit(blockPos)) {
				break;
			}

			Optional<Float> optional = this.behavior.getBlastResistance((Explosion) (Object) this, this.world, blockPos, blockState, fluidState);

			if (optional.isPresent()) {
				h2 -= (optional.get() + 0.3F) * 0.3F;
			}

			if (h2 > 0.0F && this.behavior.canDestroyBlock((Explosion) (Object) this, this.world, blockPos, blockState, h2)) {
				newSet.add(blockPos);
				set.set(newSet);
			}

			m2 += d * 0.30000001192092896;
			n2 += e * 0.30000001192092896;
			o2 += f * 0.30000001192092896;
		}
	}
}
