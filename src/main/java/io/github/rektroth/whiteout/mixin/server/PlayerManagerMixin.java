/*
 * Patch for MC-4
 * 
 * Authored for CraftBukkit/Spigot by BillyGalbreath <blake.galbreath@gmail.com> on December 8, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on July 11, 2023.
 */

package io.github.rektroth.whiteout.mixin.server;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Unique
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

	@Inject(
		at = @At("HEAD"),
		method = "checkCanJoin",
		cancellable = true)
	private void fixedBanCheck(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> ci) {
		if (((PlayerManagerAccessor)this).getBannedProfiles().contains(profile)) {
			BannedPlayerEntry bannedPlayerEntry = ((PlayerManagerAccessor)this).getBannedProfiles().get(profile);

			if (bannedPlayerEntry != null) {
				MutableText mutableText = Text.translatable(
					"multiplayer.disconnect.banned.reason",
					bannedPlayerEntry.getReason());

				if (bannedPlayerEntry.getExpiryDate() != null) {
					mutableText.append(Text.translatable(
						"multiplayer.disconnect.banned.expiration",
						DATE_FORMATTER.format(bannedPlayerEntry.getExpiryDate())));
				}

				ci.setReturnValue(mutableText);
			}
		}
	}

	@Redirect(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/BannedPlayerList;contains(Lcom/mojang/authlib/GameProfile;)Z"),
		method = "checkCanJoin")
	private boolean skipBadBanCheck(BannedPlayerList bannedProfiles, GameProfile profile) {
		return false;
	}
}
