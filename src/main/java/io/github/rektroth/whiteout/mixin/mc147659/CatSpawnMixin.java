/*
 * Patch for MC-147659
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on July 4, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc147659;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.spawner.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatSpawner.class)
public abstract class CatSpawnMixin {
	/**
	 * Sets the cat's position before it's type is set.
	 * @param pos       The cat's spawn position.
	 * @param world     boilerplate
	 * @param ci        boilerplate
	 * @param catEntity The cat.
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/entity/passive/CatEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;",
			value = "INVOKE"
		),
		method = "spawn(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/server/world/ServerWorld;Z)V"
	)
	private void refreshPositionAndAnglesBeforeInitialize(
			BlockPos pos,
			ServerWorld world,
			boolean persistent,
			CallbackInfo ci,
			@Local LocalRef<CatEntity> catEntity
	) {
		CatEntity cat2 = catEntity.get();
		cat2.refreshPositionAndAngles(pos, 0.0F, 0.0F);
		catEntity.set(cat2);
	}

	/**
	 * Skips the old position set.
	 * @param instance boilerplate
	 * @param blockPos boilerplate
	 * @param yaw      boilerplate
	 * @param pitch    boilerplate
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/entity/passive/CatEntity;refreshPositionAndAngles(Lnet/minecraft/util/math/BlockPos;FF)V",
			value = "INVOKE"
		),
		method = "spawn(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/server/world/ServerWorld;Z)V"
	)
	private void skipRefreshPositionAndAngles(CatEntity instance, BlockPos blockPos, float yaw, float pitch) {
		return;
	}
}
