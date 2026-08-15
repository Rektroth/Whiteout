package io.github.rektroth.whiteout.mixin.mc158900;

import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserBanList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Player list modifications for MC-158900 patch.
 */
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
	/**
	 * Redirects the call to check if the player is banned to also check that the relevant list entry isn't null.
	 * @param instance The list of banned players.
	 * @param user     The data on the player attempting to join.
	 * @return True if the player is in the list of banned profiles and the instance in the list is not null,
	 * false otherwise.
	 */
	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/players/UserBanList;isBanned(Lnet/minecraft/server/players/NameAndId;)Z"
		),
		method = "canPlayerLogin")
	private boolean containsAndIsNotNull(UserBanList instance, NameAndId user) {
		return instance.isBanned(user) && instance.get(user) != null;
	}
}
