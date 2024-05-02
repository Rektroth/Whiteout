/*
 * Patch for MC-84789
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc84789;

import net.minecraft.entity.ai.goal.WolfBegGoal;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WolfBegGoal.class)
public abstract class WolfBegGoalMixin {
    @Final
    @Shadow
    private WolfEntity wolf;

    /**
     * Redirects the call to `isTamed` in the method to invert the result.
     * @param instance The wolf.
     * @return True if not tamed, false otherwise.
     */
    @Redirect(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;isTamed()Z"),
        method = "isAttractive")
    private boolean isNotTamed(WolfEntity instance) {
        return !instance.isTamed();
    }
}
