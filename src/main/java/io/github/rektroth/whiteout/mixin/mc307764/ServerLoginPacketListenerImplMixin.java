package io.github.rektroth.whiteout.mixin.mc307764;

import io.github.rektroth.whiteout.Whiteout;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 *
 */
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Final
    @Shadow
    private Connection connection;

    @Shadow
    public abstract String getUserName();

    @Unique
    public void whiteout$disconnectAsync(Component reason) {
        try {
            Whiteout.LOGGER.info("Disconnecting {}: {}", this.getUserName(), reason.getString());
            this.connection.send(new ClientboundLoginDisconnectPacket(reason), PacketSendListener.thenRun(() -> this.connection.disconnect(reason)));
            this.connection.handleConnectionDisconnectOnNextTick = true;
        } catch (Exception e) {
            Whiteout.LOGGER.error("Error whilst disconnecting player", e);
        }
    }
}
