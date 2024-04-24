/*
 * Patch for MC-257487
 * 
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on November 12, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 12, 2023.
 */

package io.github.rektroth.whiteout.mixin.entity.boss.dragon;

import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragonFight.class)
public interface EnderDragonFightAccessor {
	@Accessor("bossBar")
	public ServerBossBar getBossBar();
}
