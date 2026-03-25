package io.github.rektroth.whiteout.mixin.mc302297;

import com.google.common.base.Suppliers;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

/**
 * Server player modifications for MC-302297 patch.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    /**
     * boilerplate
     * @param level       boilerplate
     * @param gameProfile boilerplate
     */
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    /**
     * Broadcasts non-container slot changes when the player is currently in a container.
     * @param ci boilerplate
     */
    @Inject(
        at = @At(
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;broadcastChanges()V",
            value = "INVOKE"
        ),
        method = "tick"
    )
    private void broadcastNonContainerSlotChangesWhenInContainer(CallbackInfo ci) {
        if (this.containerMenu != this.inventoryMenu) {
            for (int i = InventoryMenu.RESULT_SLOT; i < InventoryMenu.ARMOR_SLOT_END; i++) {
                this.broadcastSlotChange(i);
            }

            this.broadcastSlotChange(InventoryMenu.SHIELD_SLOT);
        }
    }

    @Unique
    private void broadcastSlotChange(int slot) {
        ItemStack item = this.inventoryMenu.slots.get(slot).getItem();
        Supplier<ItemStack> supplier = Suppliers.memoize(item::copy);
        ((AbstractContainerMenuInvoker)this.inventoryMenu).triggerSlotListeners(slot, item, supplier);
        ((AbstractContainerMenuInvoker)this.inventoryMenu).synchronizeSlotToRemote(slot, item, supplier);
    }
}
