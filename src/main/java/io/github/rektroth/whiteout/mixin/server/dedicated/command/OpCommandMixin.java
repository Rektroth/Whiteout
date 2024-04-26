/*
 * Patch for MC-253721
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.server.dedicated.command;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.OpCommand;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

@Mixin(OpCommand.class)
public abstract class OpCommandMixin {
    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"
        ),
        method = "op"
    )
    private static void skipBadSendFeedback(ServerCommandSource instance, Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        // do nothing
    }

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        method = "op"
    )
    private static void fixedSendFeedback(
        ServerCommandSource source,
        Collection<GameProfile> targets,
        CallbackInfoReturnable<Integer> cir,
        PlayerManager playerManager,
        int i,
        Iterator<GameProfile> var4,
        GameProfile gameProfile
    ) {
        source.sendFeedback(() -> Text.translatable("commands.deop.success", gameProfile.getName()), true);
    }
}
