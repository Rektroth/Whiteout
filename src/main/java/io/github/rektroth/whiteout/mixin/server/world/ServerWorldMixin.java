/*
 * Patch for MC-262422
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.server.world;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(
        MutableWorldProperties properties,
        RegistryKey<World> registryRef,
        DynamicRegistryManager registryManager,
        RegistryEntry<DimensionType> dimensionEntry,
        Supplier<Profiler> profiler,
        boolean isClient,
        boolean debugWorld,
        long biomeAccess,
        int maxChainedNeighborUpdates
    ) {
        super(
            properties,
            registryRef,
            registryManager,
            dimensionEntry,
            profiler,
            isClient,
            debugWorld,
            biomeAccess,
            maxChainedNeighborUpdates);
    }

    @Redirect(
        at = @At(
            target = "Lnet/minecraft/server/world/ServerWorld;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;",
            value = "INVOKE"),
        method = "getLightningPos")
    private List<LivingEntity> fixedListOfLivingEntities(
            ServerWorld instance,
            Class<LivingEntity> entityClass,
            Box box,
            Predicate<? super LivingEntity> predicate) {
        return this.getEntitiesByClass(entityClass, box, (entity) ->
            entity != null && entity.isAlive() && this.isSkyVisible(entity.getBlockPos()) && !entity.isSpectator());
    }
}
