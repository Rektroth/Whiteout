/*
 * Patch for MC-123450
 *
 * Authored for CraftBukkit/Spigot by Phoenix616 <mail@moep.tv> on November 5, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 26, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc123450;

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
    /**
     * Sets the map ID to the ID of the map in the player's item stack rather than the already removed map,
     * so that the map ID is not null when it tries to remove the green marker.
     * @param stack          The item stack.
     * @param ci             boilerplate
     * @param mapIdComponent The map ID.
     */
    @Inject(
        at = @At(
            target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;getMapId()Lnet/minecraft/component/type/MapIdComponent;",
            value = "INVOKE_ASSIGN"),
        method = "removeFromFrame"
    )
    private void getMapIdFromItem(ItemStack stack, CallbackInfo ci, @Local LocalRef<MapIdComponent> mapIdComponent) {
        mapIdComponent.set(stack.get(DataComponentTypes.MAP_ID));
    }
}
