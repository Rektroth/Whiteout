/*
 * Patch for MC-145260
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co> on March 2, 2019.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 13, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc145260;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    private boolean enforceWhitelist;

    @Shadow
    private PlayerManager playerManager;

    /**
     * Modifies the target method to return {@code true} only if the whitelist is also enabled.
     * @param cir The return callback.
     */
    @Inject(at = @At("RETURN"), cancellable = true, method = "isEnforceWhitelist")
    private void isEnforceWhitelistAndWhitelistEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.enforceWhitelist && this.playerManager.isWhitelistEnabled());
    }
}
