package io.github.rektroth.whiteout.mixin.mc84789;

import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BegGoal.class)
public abstract class WolfBegGoalMixin {
    @Final
    @Shadow
    private Wolf wolf;

    /**
     * Redirects the call to `is` in the method to return true only if the wolf is also not tame.
     * @param instance The item held by the player.
     * @param item     The item being checked for.
     * @return True if the wolf is not tamed and the item is the desirable, false otherwise.
     */
    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"),
        method = "playerHoldingInteresting")
    private boolean isBoneAndNotTamed(ItemStack instance, Object item) {
        return !this.wolf.isTame() && instance.typeHolder().value() == item;
    }
}
