package io.github.rektroth.whiteout.mixin.mc163962;

import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Merchant offer modifications for MC-163962 patch.
 */
@Mixin(MerchantOffer.class)
public abstract class MerchantOfferMixin {
	@Final
	@Shadow
	private int maxUses;

	@Shadow
	private int demand;

	@Shadow
	private int uses;

	/**
	 * Modifies the method to floor demand at 0.
	 * @param ci boilerplate
	 */
	@Inject(at = @At("TAIL"), method = "updateDemand")
	public void fixedUpdateDemandBonus(CallbackInfo ci) {
		this.demand = Math.max(0, this.demand + this.uses - (this.maxUses - this.uses));
	}
}
