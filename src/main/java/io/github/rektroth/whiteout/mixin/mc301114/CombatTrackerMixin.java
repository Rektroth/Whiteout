package io.github.rektroth.whiteout.mixin.mc301114;

import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Combat tracker modifications for the MC-301114 patch.
 */
@Mixin(CombatTracker.class)
public abstract class CombatTrackerMixin {
    @Final
	@Shadow
	private List<CombatEntry> entries;

	/**
	 * Clears excess combat entries before the latest entry is added.
	 * @param source boilerplate
	 * @param damage boilerplate
	 * @param ci     boilerplate
	 */
	@Inject(at = @At(target = "Ljava/util/List;add(Ljava/lang/Object;)Z", value = "INVOKE"), method = "recordDamage")
	private void clearExcessCombatEntries(DamageSource source, float damage, CallbackInfo ci) {
		if (!this.entries.isEmpty()) {
            while (this.entries.size() > 10240) {
				this.entries.removeFirst();
			}
		}
	}
}
