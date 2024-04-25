/*
 * Patch for MC-252817
 *
 * Authored for CraftBukkit/Spigot by braindead <totsuka.sama@gmail.com> on November 5, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity.decoration;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
    @Inject(
            at = @At(
                    target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;getMapId()Lnet/minecraft/component/type/MapIdComponent;",
                    value = "INVOKE_ASSIGN"),
            method = "removeFromFrame(Lnet/minecraft/item/ItemStack;)V"
    )
    private void getMapIdFromItem(ItemStack stack, CallbackInfo ci, @Local LocalRef<MapIdComponent> mapIdComponent) {
        mapIdComponent.set(stack.get(DataComponentTypes.MAP_ID));
    }
}
