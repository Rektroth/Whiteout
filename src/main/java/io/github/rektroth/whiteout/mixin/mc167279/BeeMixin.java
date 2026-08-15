package io.github.rektroth.whiteout.mixin.mc167279;

import io.github.rektroth.whiteout.util.BeeFlyingMoveControl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal {
    /**
     * boilerplate
     * @param entityType boilerplate
     * @param level      boilerplate
     */
    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Modifies the bee flying control to give them gravity when in the void.
     * @param entityType boilerplate
     * @param level      boilerplate
     * @param ci         boilerplate
     */
    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void fixedMoveControl(EntityType<? extends Bee> entityType, Level level, CallbackInfo ci) {
        this.moveControl = new BeeFlyingMoveControl(this, 20, true);
    }
}
