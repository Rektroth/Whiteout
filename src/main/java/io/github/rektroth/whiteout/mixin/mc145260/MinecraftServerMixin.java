package io.github.rektroth.whiteout.mixin.mc145260;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Minecraft server modifications for MC-145260 patch.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    private boolean enforceWhitelist;

    @Shadow
    private PlayerList playerList;

    /**
     * Modifies the target method to return {@code true} only if the whitelist is also enabled.
     * @param cir The return callback.
     */
    @Inject(at = @At("RETURN"), cancellable = true, method = "isEnforceWhitelist")
    private void isEnforceWhitelistAndWhitelistEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.enforceWhitelist && this.playerList.isUsingWhitelist());
    }
}
