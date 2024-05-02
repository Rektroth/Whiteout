/*
 * Patch for MC-257487
 * 
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on November 12, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 12, 2023.
 */

package io.github.rektroth.whiteout.mixin.mc257487;

import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {
	@Final
	@Shadow
	private ServerBossBar bossBar;

	/**
	 * Modifies the method to reset the boss bar's name if the dragon itself does not have a custom name.
	 * @param dragon The ender dragon.
	 * @param ci     boilerplate
	 */
	@Inject(at = @At("TAIL"), method = "updateFight")
	private void fixedCustomNameCheck(EnderDragonEntity dragon, CallbackInfo ci) {
		if (!dragon.hasCustomName()) {
			this.bossBar.setName(Text.translatable("entity.minecraft.ender_dragon"));
		}
	}
}
