/*
 * Patch for MC-158900
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc158900;

import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	/**
	 * Redirects the call to `contains` to also check that the profile instance in the list isn't null.
	 * @param bannedPlayerList The list of banned players.
	 * @param configEntry      The player config entry.
	 * @return True if the profile is in the list of banned profiles and the instance in the list is mot null,
	 * false otherwise.
	 */
	@Redirect(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/BannedPlayerList;contains(Lnet/minecraft/server/PlayerConfigEntry;)Z"),
		method = "checkCanJoin")
	private boolean containsAndIsNotNull(BannedPlayerList bannedPlayerList, PlayerConfigEntry configEntry) {
		return bannedPlayerList.contains(configEntry) && bannedPlayerList.get(configEntry) != null;
	}
}
