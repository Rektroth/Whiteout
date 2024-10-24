/*
 * Patch for MC-99075
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 13, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc99075;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    private @Nullable Vec3d requestedTeleportPos;

    @Inject(
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/Vec3d;getY()D"),
        method = "onPlayerInteractBlock")
    private void fixInventoryDesync(
        PlayerInteractBlockC2SPacket packet,
        CallbackInfo ci,
        @Local ServerWorld serverWorld,
        @Local BlockPos blockPos
    ) {
        if (!(this.requestedTeleportPos == null && serverWorld.canPlayerModifyAt(this.player, blockPos))) {
            this.player.playerScreenHandler.syncState();
        }
    }
}
