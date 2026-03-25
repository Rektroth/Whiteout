package io.github.rektroth.whiteout.mixin.mc259832;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mob effect instance modifications for MC-259832 patch.
 */
@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {
    /**
     * The duration of the effect.
     */
    @Shadow
    private int duration;

    /**
     * The hidden effect.
     */
    @Shadow
    private MobEffectInstance hiddenEffect;

    /**
     * Sets the details of the effect to match a copy.
     * @param copy The copy to match the details of.
     */
    @Shadow
    abstract void setDetailsFrom(MobEffectInstance copy);

    /**
     * Continues to downgrade to hidden effects until there are none remaining.
     * @return False if the duration of the active effect is not 0 or there is no hidden effect available;
     * true otherwise.
     * @author Rektroth
     * @reason The fix essentially wraps the contents of the method in a do-while loop.
     */
    @Overwrite
    private boolean downgradeToHiddenEffect() {
        if (this.duration != 0 || this.hiddenEffect == null) {
            return false;
        }

        do {
            this.setDetailsFrom(this.hiddenEffect);
            this.hiddenEffect = ((MobEffectInstanceAccessor)this.hiddenEffect).getHiddenEffect();
        } while (this.duration == 0 && this.hiddenEffect != null);

        return true;
    }
}
