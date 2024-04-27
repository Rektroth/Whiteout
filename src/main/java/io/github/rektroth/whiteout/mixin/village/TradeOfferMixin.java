package io.github.rektroth.whiteout.mixin.village;

import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TradeOffer.class)
public abstract class TradeOfferMixin {
	@Final
	@Shadow
	private int maxUses;

	@Shadow
	private int demandBonus;

	@Shadow
	private int uses;

	@Inject(at = @At("TAIL"), method = "updateDemandBonus")
	public void fixedUpdateDemandBonus(CallbackInfo ci) {
		this.demandBonus = Math.max(0, this.demandBonus + this.uses - (this.maxUses - this.uses));
	}
}
