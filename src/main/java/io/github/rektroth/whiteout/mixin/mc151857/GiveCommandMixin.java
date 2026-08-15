package io.github.rektroth.whiteout.mixin.mc151857;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Give command modifications for MC-151857 patch.
 */
@Mixin(GiveCommand.class)
public abstract class GiveCommandMixin {
	/**
	 * Corrects the translation component to be for multiple players instead of a single.
	 * @param instance           The command source stack.
	 * @param messageSupplier    boilerplate
	 * @param broadcast          Whether the result of the command should be broadcast to server admins.
	 * @param count              The number of items given to the player(s).
	 * @param prototypeItemStack The item(s) given to the player(s).
	 * @param players            The player(s) that the item(s) were given to.
	 */
	@Redirect(
		at = @At(
			ordinal = 1,
			target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V",
			value = "INVOKE"),
		method = "giveItem"
	)
	private static void sendSuccessForMultiple(
        CommandSourceStack instance,
        Supplier<Component> messageSupplier,
        boolean broadcast,
        @Local(argsOnly = true) int count,
        @Local(name = "prototypeItemStack") ItemStack prototypeItemStack,
        @Local(argsOnly = true) Collection<ServerPlayer> players
    ) {
		instance.sendSuccess(() -> Component.translatable("commands.give.success.multiple", count, prototypeItemStack.getDisplayName(), players.size()), broadcast);
	}
}
