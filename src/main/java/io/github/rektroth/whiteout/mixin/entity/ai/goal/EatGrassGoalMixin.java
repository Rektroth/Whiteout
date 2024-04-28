/*
 * Patch for MC-210802
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity.ai.goal;

import io.github.rektroth.whiteout.mixin.server.world.ThreadedAnvilChunkStorageInvoker;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EatGrassGoal.class)
public abstract class EatGrassGoalMixin {
	@Final
	@Shadow
	private MobEntity mob;

	@Final
	@Shadow
	private World world;

	@Inject(at = @At("HEAD"), method = "canStart", cancellable = true)
	private void fixCanUse(CallbackInfoReturnable<Boolean> cir) {
		if (!((ThreadedAnvilChunkStorageInvoker)((ServerWorld)this.world).getChunkManager().threadedAnvilChunkStorage).invokeShouldTick(this.mob.getChunkPos())) {
			cir.setReturnValue(false);
		}
	}
}
