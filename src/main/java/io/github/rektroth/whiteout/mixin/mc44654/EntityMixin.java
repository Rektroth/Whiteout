package io.github.rektroth.whiteout.mixin.mc44654;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public boolean needsSync;

    @Shadow
    public abstract EntityType<?> getType();

    /**
     * Tells the game that the entity has a non-zero velocity if the update interval is equal to the 32-bit integer limit.
     * (I absolutely hate the way this injection is done, but I'm not sure there's a more elegant way to do it
     * without copying code from the setPos() method.)
     * @param x  boilerplate
     * @param y  boilerplate
     * @param z  boilerplate
     * @param ci boilerplate
     */
    @Inject(
        at = @At(
            ordinal = 0,
            target = "Lnet/minecraft/world/entity/Entity;isRemoved()Z",
            shift = At.Shift.AFTER,
            value = "JUMP"
        ),
        method = "setPosRaw",
        slice = @Slice(
            from = @At(
                target = "Lnet/minecraft/world/level/entity/EntityInLevelCallback;onMove()V",
                value = "INVOKE"
            ),
            to = @At(value = "RETURN"
        )
    ))
    private void hasVelocityIfUpdateIntervalIsMax(final double x, final double y, final double z, CallbackInfo ci) {
        if (this.getType().updateInterval() == Integer.MAX_VALUE) {
            this.needsSync = true;
        }
    }
}
