package io.github.rektroth.whiteout.mixin.mc157464;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Villager goal packages modifications for MC-157464 patch.
 */
@Mixin(VillagerGoalPackages.class)
public abstract class VillagerGoalPackagesMixin {
    /**
     * Redirects the call to `create` to instead make a call to `create` with a predicate that checks if
     * the villager is not sleeping.
     * @param speedModifier        The villager's speed.
     * @param interruptOngoingWalk Whether the target requires walking.
     * @param maxDistToWalk        How far away the target is.
     * @return The villager task.
     */
    @Redirect(
        at = @At(
            target = "Lnet/minecraft/world/entity/ai/behavior/GoToWantedItem;create(FZI)Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;",
            value = "INVOKE"),
        method = "getCorePackage")
    private static BehaviorControl<LivingEntity> goToWantedItemFix(float speedModifier, boolean interruptOngoingWalk, int maxDistToWalk) {
        return GoToWantedItem.create(
            villager -> !villager.isSleeping(),
            speedModifier,
            interruptOngoingWalk,
            maxDistToWalk
        );
    }
}
