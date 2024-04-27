/*
 * Patch for MC-123450
 *
 * Authored for CraftBukkit/Spigot by Phoenix616 <mail@moep.tv> on November 5, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 26, 2024.
 */

/*
 * Patch for MC-123848
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {
    protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
        at = @At(
            target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;getMapId()Lnet/minecraft/component/type/MapIdComponent;",
            value = "INVOKE_ASSIGN"),
        method = "removeFromFrame"
    )
    private void getMapIdFromItem(ItemStack stack, CallbackInfo ci, @Local LocalRef<MapIdComponent> mapIdComponent) {
        mapIdComponent.set(stack.get(DataComponentTypes.MAP_ID));
    }

    @Redirect(
        at = @At(target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", value = "INVOKE", ordinal = 1),
        method = "setHeldItemStack(Lnet/minecraft/item/ItemStack;Z)V"
    )
    private boolean fixedPlaySoundCheck(ItemStack value, @Local(argsOnly = true) boolean update) {
        return !(!value.isEmpty() && update);
    }

    @Nullable
    @Override
    public ItemEntity dropStack(ItemStack stack) {
        return this.dropStack(stack, getMovementDirection().equals(Direction.DOWN) ? -0.6F : 0.0F);
    }
}
