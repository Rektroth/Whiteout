package io.github.rektroth.whiteout.mixin.mc98153;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Player list modifications for MC-98153 patch.
 */
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    /**
     * Sends a packet to the player's client to reset their position after it has been set.
     * @param serverPlayer      boilerplate
     * @param keepAllPlayerData boilerplate
     * @param removalReason     boilerplate
     * @param cir               boilerplate
     * @param player            The player.
     */
    @Inject(
        at = @At(
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/server/level/ServerPlayer;snapTo(DDDFF)V",
            value = "INVOKE"
        ),
        method = "respawn"
    )
    private void resetPlayerPos(
            ServerPlayer serverPlayer,
            boolean keepAllPlayerData,
            Entity.RemovalReason removalReason,
            CallbackInfoReturnable<ServerPlayer> cir,
            @Local(name="player") ServerPlayer player
    ) {
        player.connection.resetPosition();
    }
}
