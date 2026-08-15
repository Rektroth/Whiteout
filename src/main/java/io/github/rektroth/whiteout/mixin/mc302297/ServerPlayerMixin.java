package io.github.rektroth.whiteout.mixin.mc302297;

import com.mojang.authlib.GameProfile;
import io.github.rektroth.whiteout.accessors.InventoryMenuAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Server player modifications for the MC-302297 patch.
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
	 * Broadcasts equipment and crafting slot changes when the player is in a container.
	 * @param ci boilerplate
	 */
	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;broadcastChanges()V",
			value = "INVOKE"),
		method = "tick")
	private void broadcastNonContainerSlotChanges(CallbackInfo ci) {
		if (this.containerMenu != this.inventoryMenu) {
			((InventoryMenuAccessor)this.inventoryMenu).whiteout$broadcastNonContainerSlotChanges();
		}
	}
}
