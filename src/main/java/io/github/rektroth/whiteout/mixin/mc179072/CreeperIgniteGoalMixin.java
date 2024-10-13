/*
 * Patch for MC-179072
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 13, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc179072;

import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.entity.ai.goal.CreeperIgniteGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreeperIgniteGoal.class)
public abstract class CreeperIgniteGoalMixin extends Goal {
	@Final
	@Shadow
	private CreeperEntity creeper;

	@Shadow
	public abstract boolean canStart();

	/**
	 * Overrides the target method to only continue if the target player is not in creative or spectator mode.
	 * @return {@code true} if the target player is not in creative or spectator mode.
	 */
	@Unique
	@Override
	public boolean shouldContinue() {
		return !EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(this.creeper.getTarget()) && this.canStart();
	}
}
