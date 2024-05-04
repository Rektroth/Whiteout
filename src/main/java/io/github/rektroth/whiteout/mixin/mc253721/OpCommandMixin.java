/*
 * Patch for MC-253721
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc253721;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.OpCommand;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(OpCommand.class)
public abstract class OpCommandMixin {
    /**
     * Redirects the call to `sendFeedback` to supply the name from the game profile rather than the targets iterator.
     * @param instance         The server command source.
     * @param feedbackSupplier boilerplate
     * @param broadcastToOps   Whether to broadcast to server operators.
     * @param gameProfile      The player's game profile.
     */
    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"
        ),
        method = "op"
    )
    private static void skipBadSendFeedback(
        ServerCommandSource instance,
        Supplier<Text> feedbackSupplier,
        boolean broadcastToOps,
        @Local GameProfile gameProfile
    ) {
        instance.sendFeedback(() -> Text.translatable("commands.op.success", gameProfile.getName()), broadcastToOps);
    }
}
