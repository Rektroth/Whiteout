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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WolfBegGoal.class)
public class WolfBegGoalMixin {
    @Redirect(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;isTamed()Z"),
        method = "isAttractive(Lnet/minecraft/entity/player/PlayerEntity;)Z")
    public boolean fixedBadTameCheck(WolfEntity instance) {
        return !((WolfBegGoalAccessor)this).getWolf().isTamed();
    }
}
