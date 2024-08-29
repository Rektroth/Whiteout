/*
 * Patch for MC-235045
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc235045;

import net.minecraft.command.EntitySelectorReader;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntitySelectorReader.class)
public abstract class EntitySelectorReaderMixin {
	// nothing yet
}
