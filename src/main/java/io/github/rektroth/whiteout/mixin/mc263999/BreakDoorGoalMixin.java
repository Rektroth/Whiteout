/*
 * Patch for MC-263999
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc263999;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.entity.ai.goal.DoorInteractGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BreakDoorGoal.class)
public class BreakDoorGoalMixin extends DoorInteractGoal {
	@Unique
	private BlockState oldDoorState = null;

	public BreakDoorGoalMixin(MobEntity mob) {
		super(mob);
	}

	/**
	 * Gets the state of the door before its broken.
	 * @param ci boilerplate
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
			value="INVOKE"
		),
		method = "tick"
	)
	private void getOldDoorState(CallbackInfo ci) {
		oldDoorState = this.mob.getWorld().getBlockState(this.doorPos);
	}

	/**
	 * Syncs the world event using the state of the door before it was broken.
	 * @param instance The world.
	 * @param eventId  The event ID.
	 * @param blockPos The position of the door.
	 * @param data     boilerplate
	 */
	@Redirect(
		at = @At(
			ordinal = 2,
			target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V",
			value = "INVOKE"
		),
		method = "tick"
	)
	private void syncWorldEventWithOldDoorState(World instance, int eventId, BlockPos blockPos, int data) {
		instance.syncWorldEvent(eventId, blockPos, Block.getRawIdFromState(oldDoorState));
	}
}
