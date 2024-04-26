/*
 * Patch for MC-108513
 *
 * Authored for CraftBukkit/Spigot by Max Lee <max@themoep.de> on May 27, 2021.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity.boss.dragon;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragonFight.class)
public interface EnderDragonFightAccessor {
	@Accessor("dragonSpawnState")
	EnderDragonSpawnState getDragonSpawnState();
}
