package io.github.rektroth.whiteout.mixin.mc99075;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Server game packet listener implementation modifications for MC-99075 patch.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin extends ServerCommonPacketListenerImpl {
    /**
     * boilerplate
     * @param server     boilerplate
     * @param connection boilerplate
     * @param cookie     boilerplate
     */
    public ServerGamePacketListenerImplMixin(
        MinecraftServer server,
        Connection connection,
        CommonListenerCookie cookie
    ) {
        super(server, connection, cookie);
    }

    /**
     * Creates an additional check for spawn protection after all other checks have failed.
     * If there is spawn protection, the user's inventory is re-synced.
     * @param instance    The player.
     * @param isTooHigh   Whether the player is at build-limit.
     * @param limit       The build limit.
     * @param serverLevel The world the player is in.
     * @param blockPos    The position being checked for spawn protection.
     */
    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;sendBuildLimitMessage(ZI)V",
            ordinal = 5
        ),
        method = "handleUseItemOn"
    )
    private void fixInventoryDesync(
        ServerPlayer instance,
        boolean isTooHigh,
        int limit,
        @Local ServerLevel serverLevel,
        @Local BlockPos blockPos
    ) {
        if (this.server.isUnderSpawnProtection(serverLevel, blockPos, instance)) {
            instance.containerMenu.sendAllDataToRemote();
        } else {
            instance.sendBuildLimitMessage(isTooHigh, limit);
        }
    }
}
