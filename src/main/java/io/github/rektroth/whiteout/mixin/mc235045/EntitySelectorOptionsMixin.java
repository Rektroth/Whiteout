/*
 * Patch for MC-235045
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc235045;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelectorOptions;

import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.function.Predicate;

@Mixin(EntitySelectorOptions.class)
public abstract class EntitySelectorOptionsMixin {
	@Unique
	private static final DynamicCommandExceptionType ERROR_ENTITY_TAG_INVALID = new DynamicCommandExceptionType((object) -> {
		return Text.of("Invalid or unknown entity type tag '" + object + "'");
		// missing hover text "You can disable this error in 'whiteout.properties'"
	});

	@Shadow
	private static void putOption(
		String id,
		EntitySelectorOptions.SelectorHandler handler,
		Predicate<EntitySelectorReader> condition,
		Text description
	) { }

	@Redirect(
		at = @At(
			ordinal = 15,
			target = "Lnet/minecraft/command/EntitySelectorOptions;putOption(Ljava/lang/String;Lnet/minecraft/command/EntitySelectorOptions$SelectorHandler;Ljava/util/function/Predicate;Lnet/minecraft/text/Text;)V",
			value = "INVOKE"
		),
		method = "register"
	)
	private static void fixedPutOption(
		String id,
		EntitySelectorOptions.SelectorHandler handler,
		Predicate<EntitySelectorReader> condition,
		Text description
	) {
		putOption("type", (reader) -> {
			reader.setSuggestionProvider((builder, consumer) -> {
				CommandSource.suggestIdentifiers(Registries.ENTITY_TYPE.getIds(), builder, String.valueOf('!'));
				CommandSource.suggestIdentifiers(Registries.ENTITY_TYPE.streamTags().map(TagKey::id), builder, "!#");

				if (!reader.excludesEntityType()) {
					CommandSource.suggestIdentifiers(Registries.ENTITY_TYPE.getIds(), builder);
					CommandSource.suggestIdentifiers(Registries.ENTITY_TYPE.streamTags().map(TagKey::id), builder, String.valueOf('#'));
				}

				return builder.buildFuture();
			});

			int i = reader.getReader().getCursor();
			boolean bl = reader.readNegationCharacter();

			if (reader.excludesEntityType() && !bl) {
				reader.getReader().setCursor(i);
				throw EntitySelectorOptions.INAPPLICABLE_OPTION_EXCEPTION
					.createWithContext(reader.getReader(), "type");
			} else {
				if (bl) {
					reader.setExcludesEntityType();
				}

				if (reader.readTagCharacter()) {
					TagKey<EntityType<?>> tagKey = TagKey.of(
						RegistryKeys.ENTITY_TYPE,
						Identifier.fromCommandInput(reader.getReader()));

					if (/*reader.parsingEntityArgumentSuggestions &&*/ Registries.ENTITY_TYPE.getEntryList(tagKey).isEmpty()) {
						reader.getReader().setCursor(i);
						throw ERROR_ENTITY_TAG_INVALID.createWithContext(reader.getReader(), tagKey);
					}

					reader.addPredicate((entity) -> entity.getType().isIn(tagKey) != bl);
				} else {
					Identifier identifier = Identifier.fromCommandInput(reader.getReader());
					EntityType<?> entityType = Registries.ENTITY_TYPE.getOrEmpty(identifier).orElseThrow(() -> {
						reader.getReader().setCursor(i);
						return EntitySelectorOptions.INVALID_TYPE_EXCEPTION
							.createWithContext(reader.getReader(), identifier.toString());
					});

					if (Objects.equals(EntityType.PLAYER, entityType) && !bl) {
						reader.setIncludesNonPlayers(false);
					}

					reader.addPredicate((entity) -> Objects.equals(entityType, entity.getType()) != bl);

					if (!bl) {
						reader.setEntityType(entityType);
					}
				}

			}
		}, (reader) -> !reader.selectsEntityType(), Text.translatable("argument.entity.options.type.description"));
	}
}
