package io.github.rektroth.whiteout.mixin.mc259832;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Ender dragon fight accessors for MC-108513 patch.
 */
@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor {
	/**
	 * Gets the mob effect's hidden effect.
	 * @return The mob effect's hidden effect.
	 */
	@Accessor("hiddenEffect")
	MobEffectInstance getHiddenEffect();
}
