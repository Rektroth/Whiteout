/*
 * Patch for MC-235045
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc235045;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.StringReader;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.command.argument.EntityArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityArgumentType.class)
public abstract class EntityArgumentTypeMixin implements CommandSource {
	/**
	 *
	 * @param value         boilerplate
	 * @param stringReader  The string reader.
	 * @param commandSource The command source.
	 * @return The original entity selector reader with parsing entity argument suggestions.
	 */
	@ModifyVariable(at = @At("STORE"), method = "listSuggestions")
	private EntitySelectorReader newEntitySelectorReader (
		EntitySelectorReader value,
		@Local StringReader stringReader,
		@Local CommandSource commandSource
	) {
		EntitySelectorReader entitySelectorReader = value;
		return entitySelectorReader;
	}
}
