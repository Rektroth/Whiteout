package io.github.rektroth.whiteout.mixin.mc117075;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

/**
 * Level modifications for MC-117075 patch.
 */
@Mixin(Level.class)
public abstract class LevelMixin {
    @Final
    @Shadow
    protected List<TickingBlockEntity> blockEntityTickers;

    @Unique
    private ReferenceOpenHashSet<TickingBlockEntity> toRemove;

    /**
     * Instantiates a list of block entities to be unloaded.
     * @param ci boilerplate
     */
    @Inject(
        at = @At(target = "Ljava/util/List;iterator()Ljava/util/Iterator;", value = "INVOKE"),
        method = "tickBlockEntities")
    private void instantiateToRemove(CallbackInfo ci) {
        this.toRemove = new ReferenceOpenHashSet<>();
        toRemove.add(null);
    }

    /**
     * Redirects the unloading of a given block entity to instead add that block entity to the list of block entities
     * to be unloaded at the end of the game tick.
     * @param instance          boilerplate
     * @param blockEntityTicker The block entity to be unloaded.
     */
    @Redirect(at = @At(target = "Ljava/util/Iterator;remove()V", value = "INVOKE"), method = "tickBlockEntities")
    private void addToRemove(Iterator instance, @Local TickingBlockEntity blockEntityTicker) {
        this.toRemove.add(blockEntityTicker);
    }

    /**
     * Unloads all block entities set to be unloaded at once.
     * @param ci boilerplate
     */
    @Inject(at = @At(value = "TAIL"), method = "tickBlockEntities")
    private void removeAll(CallbackInfo ci) {
        this.blockEntityTickers.removeAll(toRemove);
        this.toRemove = null;
    }
}
