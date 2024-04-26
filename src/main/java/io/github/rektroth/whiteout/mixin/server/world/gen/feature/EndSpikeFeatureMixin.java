/*
 * Patch for MC-108513
 *
 * Authored for CraftBukkit/Spigot by Max Lee <max@themoep.de> on May 27, 2021.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.server.world.gen.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.rektroth.whiteout.accessors.GeneratedByDragonFightAccessor;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndSpikeFeature.class)
public class EndSpikeFeatureMixin {
    @Inject(
        at = @At(
            target = "Lnet/minecraft/world/ServerWorldAccess;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
            value = "INVOKE"),
        method = "generateSpike")
    private void setGeneratedByDragonFight(
            ServerWorldAccess world,
            Random random,
            EndSpikeFeatureConfig config,
            EndSpikeFeature.Spike spike,
            CallbackInfo ci,
            @Local LocalRef<EndCrystalEntity> endCrystalEntity
    ) {
        GeneratedByDragonFightAccessor g = (GeneratedByDragonFightAccessor)(endCrystalEntity.get());
        g.whiteout$setGeneratedByDragonFight(true);
        endCrystalEntity.set((EndCrystalEntity)g);
    }
}
