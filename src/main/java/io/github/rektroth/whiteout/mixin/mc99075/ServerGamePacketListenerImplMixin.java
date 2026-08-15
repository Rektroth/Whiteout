package io.github.rektroth.whiteout.mixin.mc99075;

import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Server game packet listener implementation modifications for MC-99075 patch.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    /**
     * If there is spawn protection, the user's inventory is re-synced.
     * @param packet boilerplate
     * @param ci     boilerplate
     */
    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;sendSpawnProtectionMessage(Lnet/minecraft/core/BlockPos;)V"
        ),
        method = "handleUseItemOn"
    )
    private void fixInventoryDesync(
        ServerboundUseItemOnPacket packet,
        CallbackInfo ci
    ) {
        this.player.containerMenu.sendAllDataToRemote();
    }
}
