package io.github.rektroth.whiteout.mixin.mc302297;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Invokers of private living entity methods for the MC-302297 patch.
 */
@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {
    @Invoker("detectEquipmentUpdates")
    void whiteout$detectEquipmentUpdates();
}
