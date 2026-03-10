package io.github.rektroth.whiteout.mixin.mc259832;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mob effect instance modifications for MC-259832 patch.
 */
@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin extends MobEffectInstance {
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
     * boilerplate
     * @param effect       boilerplate
     * @param duration     boilerplate
     * @param amplifier    boilerplate
     * @param ambient      boilerplate
     * @param visible      boilerplate
     * @param showIcon     boilerplate
     * @param hiddenEffect boilerplate
     */
    public MobEffectInstanceMixin(
            final Holder<MobEffect> effect,
            final int duration,
            final int amplifier,
            final boolean ambient,
            final boolean visible,
            final boolean showIcon,
            @Nullable final MobEffectInstance hiddenEffect
    ) {
        super(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect);
    }

    /**
     * Sets the details of the effect to match a copy.
     * @param copy The copy to match the details of.
     */
    @Shadow
    abstract void setDetailsFrom(MobEffectInstance copy);

    /**
     * Keeps using hidden effects until there are no more? (idk)
     * @return False if the duration of the active effect is not 0 and there is no hidden effect available;
     * true otherwise.
     * @author Rektroth
     * @reason The fix wraps the contents of the method in a do-while loop.
     */
    @Overwrite
    private boolean downgradeToHiddenEffect() {
        if (this.duration != 0 && this.hiddenEffect == null) {
            return false;
        }

        do {
            this.setDetailsFrom(this.hiddenEffect);
            this.hiddenEffect = ((MobEffectInstanceAccessor)this.hiddenEffect).getHiddenEffect();
        } while (this.duration == 0 && this.hiddenEffect != null);

        return true;
    }
}
