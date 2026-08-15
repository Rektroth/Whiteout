package io.github.rektroth.whiteout.mixin.mc302297;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Server game packet listener implementation modifications for the MC-302297 patch.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    /**
     * The server player.
     */
    @Shadow
    public ServerPlayer player;

    @Inject(
        at = @At(
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;broadcastChanges()V",
            value = "INVOKE"),
        method = "handleContainerClick")
    private void broadcastOffHandAndEquipmentChangesIfFullResyncNotNeeded(ServerboundContainerClickPacket packet, CallbackInfo ci) {
        this.broadcastOffHandAndEquipmentChanges(packet);
    }

    @Inject(
        at = @At(
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;broadcastFullState()V",
            value = "INVOKE"),
        method = "handleContainerClick")
    private void broadcastOffHandAndEquipmentChangesIfFullResyncNeeded(ServerboundContainerClickPacket packet, CallbackInfo ci) {
        this.broadcastOffHandAndEquipmentChanges(packet);
    }

    @Unique
    private void broadcastOffHandAndEquipmentChanges(ServerboundContainerClickPacket packet) {
        if (packet.buttonNum() == Inventory.SLOT_OFFHAND && this.player.containerMenu != this.player.inventoryMenu) {
            this.player.connection.send(new ClientboundContainerSetSlotPacket(
                this.player.inventoryMenu.containerId,
                this.player.inventoryMenu.incrementStateId(),
                InventoryMenu.SHIELD_SLOT,
                this.player.inventoryMenu.getSlot(InventoryMenu.SHIELD_SLOT).getItem().copy()));
        }

        ((LivingEntityInvoker)this.player).whiteout$detectEquipmentUpdates();
        ItemStack mainHandItemStack = this.player.getMainHandItem();
        if (!ItemStack.matches(((PlayerAccessor)this.player).whiteout$getLastItemInMainHand(), mainHandItemStack)) {
            if (!ItemStack.isSameItem(((PlayerAccessor)this.player).whiteout$getLastItemInMainHand(), mainHandItemStack)) {
                this.player.resetAttackStrengthTicker();
            }

            ((PlayerAccessor)this.player).whiteout$setLastItemInMainHand(mainHandItemStack.copy());
        }
    }
}
