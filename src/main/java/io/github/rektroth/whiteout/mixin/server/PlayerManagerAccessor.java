package io.github.rektroth.whiteout.mixin.server;

import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccessor {
	@Accessor
	BannedPlayerList getBannedProfiles();
}
