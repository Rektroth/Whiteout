/*
 * Patch for MC-259571
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc259571;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    @Shadow
    private GameMode gameMode;

    @Shadow
    protected abstract void setGameMode(GameMode gameMode, @Nullable GameMode previousGameMode);

    /**
     * Redirects the call to `setGameMode` to set the previous game mode to player's current game mode.
     * @param instance         boilerplate
     * @param gameMode         The game mode to set the player to.
     * @param previousGameMode boilerplate
     */
    @Redirect(
        at = @At(
            target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V",
            value = "INVOKE"),
        method = "changeGameMode")
    private void fixedSetGameMode(ServerPlayerInteractionManager instance, GameMode gameMode, GameMode previousGameMode) {
        this.setGameMode(gameMode, this.gameMode);
    }
}
