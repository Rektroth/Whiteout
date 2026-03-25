package io.github.rektroth.whiteout.mixin.mc147659;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.npc.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Cat spawner modifications for MC-147659 patch.
 */
@Mixin(CatSpawner.class)
public abstract class CatSpawnMixin {
	/**
	 * Sets the cat's position before it's type is set.
	 * @param pos       The cat's spawn position.
	 * @param level     boilerplate
	 * @param ci        boilerplate
	 * @param catEntity The cat.
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/entity/animal/feline/Cat;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
			value = "INVOKE"
		),
		method = "spawnCat"
	)
	private void refreshPositionAndAnglesBeforeInitialize(
			BlockPos pos,
			ServerLevel level,
			boolean persistent,
			CallbackInfo ci,
			@Local LocalRef<Cat> catEntity
	) {
		Cat cat2 = catEntity.get();
		cat2.snapTo(pos, 0.0F, 0.0F);
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
			target = "Lnet/minecraft/world/entity/animal/feline/Cat;snapTo(Lnet/minecraft/core/BlockPos;FF)V",
			value = "INVOKE"
		),
		method = "spawnCat"
	)
	private void skipRefreshPositionAndAngles(Cat instance, BlockPos blockPos, float yaw, float pitch) {
		return;
	}
}
