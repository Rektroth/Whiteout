package io.github.rektroth.whiteout.mixin.mc257487;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ender dragon fight modifications for MC-257487 patch.
 */
@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {
	@Shadow
	private ServerBossEvent dragonEvent;

	/**
	 * Modifies the method to reset the boss bar's name if the dragon itself does not have a custom name.
	 * @param dragon The ender dragon.
	 * @param ci     boilerplate
	 */
	@Inject(
		at = @At(target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;hasCustomName()Z", value="INVOKE"),
		method = "updateDragon"
	)
	private void fixedCustomNameCheck(EnderDragon dragon, CallbackInfo ci) {
		if (!dragon.hasCustomName()) {
			this.dragonEvent.setName(Component.translatable("entity.minecraft.ender_dragon"));
		}
	}
}
