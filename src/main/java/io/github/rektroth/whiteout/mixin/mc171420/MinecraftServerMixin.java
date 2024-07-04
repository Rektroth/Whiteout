/*
 * Patch for MC-171420
 *
 * Authored for CraftBukkit/Spigot by William Blake Galbreath <Blake.Galbreath@GMail.com> on October 3, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on July 4, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc171420;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.Whitelist;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public abstract PlayerManager getPlayerManager();

	/**
	 * Checks if the player is whitelisted *or* is an operator.
	 * @param instance The whitelist.
	 * @param profile  The player profile.
	 * @return True if the player is whitelisted or an operator, false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/server/Whitelist;isAllowed(Lcom/mojang/authlib/GameProfile;)Z",
			value = "INVOKE"
		),
		method = "kickNonWhitelistedPlayers"
	)
	private boolean isAllowedOrOp(Whitelist instance, GameProfile profile) {
		return instance.isAllowed(profile) || this.getPlayerManager().isOperator(profile);
	}
}
