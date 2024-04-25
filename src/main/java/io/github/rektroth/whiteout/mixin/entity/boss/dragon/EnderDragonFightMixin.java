/*
 * Patch for MC-257487
 * 
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on November 12, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 12, 2023.
 */

package io.github.rektroth.whiteout.mixin.entity.boss.dragon;

import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {
	@Inject(at = @At("TAIL"), method = "updateFight(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;)V")
	private void fixedCustomNameCheck(EnderDragonEntity dragon, CallbackInfo ci) {
		ServerBossBar bossBar = ((EnderDragonFightAccessor)this).getBossBar();

		if (dragon.hasCustomName()) {
			bossBar.setName(dragon.getDisplayName());
		} else {
			bossBar.setName(Text.translatable("entity.minecraft.ender_dragon"));
		}
	}

	@Redirect(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;hasCustomName()Z"),
		method = "updateFight(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;)V")
	private boolean skipBadCustomNameCheck(EnderDragonEntity instance) {
		return false;
	}
}
