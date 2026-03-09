package io.github.rektroth.whiteout.mixin.mc179072;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Swell goal modifications for MC-179072 patch.
 */
@Mixin(SwellGoal.class)
public abstract class SwellGoalMixin extends Goal {
	@Final
	@Shadow
	private Creeper creeper;

	@Shadow
	private LivingEntity target;

	/**
	 * Adds a condition to cancel swelling if the target is in creative or spectator mode.
	 * @param ci boilerplate
	 */
	@Inject(at = @At(target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z", value = "INVOKE_ASSIGN"), method="tick")
	private void cancelSwellIfCreativeOrSpectator(CallbackInfo ci) {
		if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.target)) {
			this.creeper.setSwellDir(-1);
		}
	}
}
