/*
 * Patch for MC-167279
 *
 * Authored for CraftBukkit/Spigot by William Blake Galbreath <Blake.Galbreath@GMail.com> on January 26, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc167279;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity {
    /**
     * boilerplate
     * @param entityType boilerplate
     * @param world      boilerplate
     */
    protected BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Modifies the bee flying control to give them gravity when in the void.
     * @param entityType boilerplate
     * @param world      boilerplate
     * @param ci         boilerplate
     */
    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void fixedMoveControl(EntityType<? extends BeeEntity> entityType, World world, CallbackInfo ci) {
        class BeeFlyingMoveControl extends FlightMoveControl {
            public BeeFlyingMoveControl(final MobEntity entity, final int maxPitchChange, final boolean noGravity) {
                super(entity, maxPitchChange, noGravity);
            }

            @Override
            public void tick() {
                if (!BeeEntityMixin.this.getEntityWorld().isInBuildLimit(this.entity.getBlockPos())) {
                    this.entity.setNoGravity(false);
                }

                super.tick();
            }
        }

        this.moveControl = new BeeFlyingMoveControl(this, 20, true);
    }
}
