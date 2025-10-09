package io.github.rektroth.whiteout.mixin.mc117075;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import net.minecraft.world.tick.TickManager;
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

@Mixin(World.class)
public abstract class WorldMixin {
    @Final
    @Shadow
    protected List<BlockEntityTickInvoker> blockEntityTickers;

    @Unique
    private ReferenceOpenHashSet<BlockEntityTickInvoker> toRemove;

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
     * @param instance               boilerplate
     * @param blockEntityTickInvoker The block entity to be unloaded.
     */
    @Redirect(at = @At(target = "Ljava/util/Iterator;remove()V", value = "INVOKE"), method = "tickBlockEntities")
    private void addToRemove(Iterator instance, @Local BlockEntityTickInvoker blockEntityTickInvoker) {
        this.toRemove.add(blockEntityTickInvoker);
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
