package io.github.rektroth.whiteout.mixin.mc121706;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Armor stand modifications for MC-121706 patch.
 */
@Mixin(RangedBowAttackGoal.class)
public abstract class RangedBowAttackGoalMixin<T extends Monster & RangedAttackMob> {
    @Final
    @Shadow
    private T mob;

    /**
     * Does nothing; is meant to skip the original setLookAt call.
     * @param instance     boilerplate
     * @param target       boilerplate
     * @param yMaxRotSpeed boilerplate
     * @param xMaxRotAngle boilerplate
     */
    @Redirect(
        at = @At(
            target = "Lnet/minecraft/world/entity/ai/control/LookControl;setLookAt(Lnet/minecraft/world/entity/Entity;FF)V",
            value = "INVOKE"
        ),
        method = "tick"
    )
    private void skipOriginalSetLookAt(
        LookControl instance,
        Entity target,
        float yMaxRotSpeed,
        float xMaxRotAngle
    ) { }

    /**
     * Always updates the look at target before the method finishes.
     * @param ci     boilerplate
     * @param target The target entity.
     */
    @Inject(at = @At("TAIL"), method = "tick")
    private void newSetLookAt(CallbackInfo ci, @Local(name = "target") LivingEntity target) {
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }
}
