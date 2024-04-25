/*
 * Patch for MC-84789
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity.ai.goal;

import net.minecraft.entity.ai.goal.WolfBegGoal;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WolfBegGoal.class)
public interface WolfBegGoalAccessor {
	@Accessor("wolf")
	WolfEntity getWolf();
}
