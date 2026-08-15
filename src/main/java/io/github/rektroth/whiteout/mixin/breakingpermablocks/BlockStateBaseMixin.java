package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Block state base modifications to prevent breaking permanent blocks.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Final
    @Shadow
    private PushReaction pushReaction;

    @Shadow
    public abstract Block getBlock();

    /**
     * Modifies the `getPistonBehavior` method to return BLOCK behavior if the block is permanent.
     * @param cir The callback.
     */
    @Inject(at = @At("RETURN"), method = "getPistonPushReaction", cancellable = true)
    private void blockIfPermanent(CallbackInfoReturnable<PushReaction> cir) {
        cir.setReturnValue(!BlockUtil.isDestroyable(this.getBlock()) ? PushReaction.IMMOVEABLE : this.pushReaction);
    }
}
