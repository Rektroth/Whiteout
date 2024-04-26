/*
 * Patch for MC-157464
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity.ai.brain.task;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.ai.brain.task.WalkToNearestVisibleWantedItemTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillagerTaskListProvider.class)
public abstract class VillagerTaskListProviderMixin {
    @Redirect(
        at = @At(
            target = "Lnet/minecraft/entity/ai/brain/task/WalkToNearestVisibleWantedItemTask;create(FZI)Lnet/minecraft/entity/ai/brain/task/Task;",
            value = "INVOKE"),
        method = "createCoreTasks")
    private static Task<LivingEntity> goToWantedItemFix(float speed, boolean requiresWalkTarget, int radius) {
        return WalkToNearestVisibleWantedItemTask.create(
            villager -> !villager.isSleeping(),
            speed,
            requiresWalkTarget,
            radius
        );
    }
}
