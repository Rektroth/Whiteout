package io.github.rektroth.whiteout.mixin.mc307764;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerConnectionListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerConnectionListener.class)
public class ServerConnectionListenerMixin {
    @Redirect(at = @At(target = "Lnet/minecraft/network/Connection;tick()V", value = "INVOKE"), method = "tick")
    private void tickThing(Connection instance) {
        if (!instance.handleConnectionDisconnectOnNextTick) {
            instance.tick();
        }
    }
}
