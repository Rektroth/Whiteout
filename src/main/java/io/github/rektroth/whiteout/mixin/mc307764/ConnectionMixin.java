package io.github.rektroth.whiteout.mixin.mc307764;

import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 *
 */
@Mixin(Connection.class)
public class ConnectionMixin {
    @Unique
    public boolean handleConnectionDisconnectOnNextTick = false;
}
