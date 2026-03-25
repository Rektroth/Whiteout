package io.github.rektroth.whiteout.mixin.mc123450;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Item frame modifications for MC-123450 patch.
 */
@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin {
    /**
     * Redirects checking if the item frame is empty to check both that and if the item frame was updated,
     * so that the item frame only plays a sound when updated.
     * @param value  The item stack.
     * @param update Whether the item frame was updated.
     * @return True if the item frame should play its place sound, false otherwise.
     */
    @Redirect(
        at = @At(target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", value = "INVOKE", ordinal = 1),
        method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V"
    )
    private boolean shouldPlaySound(ItemStack value, @Local(argsOnly = true) boolean update) {
        return !(!value.isEmpty() && update);
    }
}
