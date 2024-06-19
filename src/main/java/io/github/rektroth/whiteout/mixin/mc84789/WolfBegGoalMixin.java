/*
 * Patch for MC-84789
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc84789;

import net.minecraft.entity.ai.goal.WolfBegGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
     * Redirects the call to `isOf` in the method to return true only if the wolf is also not tamed.
     * @param instance The item held by the player.
     * @param item The breeding item being checked for.
     * @return True if the wolf is not tamed and the item is the desirable, false otherwise.
     */
    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"),
        method = "isAttractive")
    private boolean isBoneAndNotTamed(ItemStack instance, Item item) {
        return !this.wolf.isTamed() && instance.isOf(item);
    }

    /**
     * Redirects the call to `isBreedingItem` in the method to return true only if the wolf is also not tamed.
     * @param instance The wolf.
     * @param itemStack The item held by the player.
     * @return True if the wolf is not tamed and the item is a breeding item, false otherwise.
     */
    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/WolfEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z"),
        method = "isAttractive")
    private boolean isBreedingItemAndNotTamed(WolfEntity instance, ItemStack itemStack) {
        return !instance.isTamed() && instance.isBreedingItem(itemStack);
    }
}
