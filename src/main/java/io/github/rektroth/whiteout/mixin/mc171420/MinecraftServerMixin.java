package io.github.rektroth.whiteout.mixin.mc171420;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserWhiteList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Minecraft server modifications for MC-171420 patch.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public abstract PlayerList getPlayerList();

	/**
	 * Checks if the player is whitelisted *or* is an operator.
	 * @param instance   The whitelist.
	 * @param nameAndId  The name and ID of the player being checked.
	 * @return True if the player is whitelisted or an operator, false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/server/players/UserWhiteList;isWhiteListed(Lnet/minecraft/server/players/NameAndId;)Z",
			value = "INVOKE"
		),
		method = "kickUnlistedPlayers"
	)
	private boolean isAllowedOrOp(UserWhiteList instance, NameAndId nameAndId) {
		return instance.isWhiteListed(nameAndId) || this.getPlayerList().isOp(nameAndId);
	}
}
