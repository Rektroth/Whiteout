/*
 * Patch for MC-235045
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc235045;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
	@Unique
	private boolean registeredAskServerSuggestionsForTree = false;

	/**
	 * Resets the value of `registeredAskServerSuggestionsForTree` to false each time `makeTreeForSource` is called.
	 * @param tree        boilerplate
	 * @param result      boilerplate
	 * @param source      boilerplate
	 * @param resultNodes boilerplate
	 * @param ci          boilerplate
	 */
	@Inject(at = @At("HEAD"), method = "makeTreeForSource")
	private void setRegisteredAskServerSuggestionsForTreeToFalse(
		CommandNode<ServerCommandSource> tree,
		CommandNode<CommandSource> result,
		ServerCommandSource source,
		Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> resultNodes,
		CallbackInfo ci
	) {
		registeredAskServerSuggestionsForTree = false;
	}

	/**
	 * Requires the client to ask the server for command argument suggestions.
	 * @param tree                            boilerplate
	 * @param result                          boilerplate
	 * @param source                          boilerplate
	 * @param resultNodes                     boilerplate
	 * @param ci                              boilerplate
	 * @param requiredArgumentBuilderLocalRef The required argument builder.
	 */
	@Inject(
		at = @At(
			target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;getSuggestionsProvider()Lcom/mojang/brigadier/suggestion/SuggestionProvider;",
			value = "INVOKE"
		),
		method = "makeTreeForSource"
	)
	private void changeSuggestions(
		CommandNode<ServerCommandSource> tree,
		CommandNode<CommandSource> result,
		ServerCommandSource source,
		Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> resultNodes,
		CallbackInfo ci,
		@Local LocalRef<RequiredArgumentBuilder<CommandSource, ?>> requiredArgumentBuilderLocalRef
	) {
		RequiredArgumentBuilder<CommandSource, ?> requiredArgumentBuilder = requiredArgumentBuilderLocalRef.get();

		if (requiredArgumentBuilder.getSuggestionsProvider() != null) {
			requiredArgumentBuilder.suggests(SuggestionProviders.getLocalProvider(requiredArgumentBuilder.getSuggestionsProvider()));
			registeredAskServerSuggestionsForTree = requiredArgumentBuilder.getSuggestionsProvider() == SuggestionProviders.ASK_SERVER;
		} else if (!registeredAskServerSuggestionsForTree && requiredArgumentBuilder.getType() instanceof EntityArgumentType) {
			requiredArgumentBuilder.suggests(requiredArgumentBuilder.getType()::listSuggestions);
			registeredAskServerSuggestionsForTree = true;
		}

		requiredArgumentBuilderLocalRef.set(requiredArgumentBuilder);
	}

	/**
	 * Skips the check of the suggestion provider to allow for our change to replace the entire block.
	 * @param instance boilerplate
	 * @return         Null.
	 */
	@Redirect(
		at = @At(
			target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;getSuggestionsProvider()Lcom/mojang/brigadier/suggestion/SuggestionProvider;",
			value = "INVOKE"
		),
		method = "makeTreeForSource"
	)
	private SuggestionProvider returnNull(RequiredArgumentBuilder<CommandSource, ?> instance) {
		return null;
	}
}
