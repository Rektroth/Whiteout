package io.github.rektroth.whiteout.mixin.mc263999;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Break door goal modifications for MC-263999 patch.
 */
@Mixin(BreakDoorGoal.class)
public class BreakDoorGoalMixin extends DoorInteractGoal {
	@Unique
	private BlockState oldDoorState = null;

	/**
	 * boilerplate
	 * @param mob boilerplate
	 */
	public BreakDoorGoalMixin(Mob mob) {
		super(mob);
	}

	/**
	 * Gets the state of the door before it's broken.
	 * @param ci boilerplate
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
			value="INVOKE"
		),
		method = "tick"
	)
	private void getOldDoorState(CallbackInfo ci) {
		oldDoorState = this.mob.level().getBlockState(this.doorPos);
	}

	/**
	 * Triggers the level event using the state of the door before it was broken.
	 * @param instance The world.
	 * @param type     ID of the event type.
	 * @param pos      The position of the door.
	 * @param data     boilerplate
	 */
	@Redirect(
		at = @At(
			ordinal = 2,
			target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V",
			value = "INVOKE"
		),
		method = "tick"
	)
	private void syncWorldEventWithOldDoorState(Level instance, int type, BlockPos pos, int data) {
		instance.levelEvent(type, pos, Block.getId(oldDoorState));
	}
}
