/*
 * Patch for MC-123450
 *
 * Authored for CraftBukkit/Spigot by Phoenix616 <mail@moep.tv> on November 5, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc123450;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
    /**
     * Redirects checking if the item frame is empty to check both that and if the item frame was updated,
     * so that the item frame only plays a sound when updated.
     * @param value  The item stack.
     * @param update Whether the item frame was updated.
     * @return True if the item frame should play its place sound, false otherwise.
     */
    @Redirect(
        at = @At(target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", value = "INVOKE", ordinal = 1),
        method = "setHeldItemStack(Lnet/minecraft/item/ItemStack;Z)V"
    )
    private boolean shouldPlaySound(ItemStack value, @Local(argsOnly = true) boolean update) {
        return !(!value.isEmpty() && update);
    }
}
