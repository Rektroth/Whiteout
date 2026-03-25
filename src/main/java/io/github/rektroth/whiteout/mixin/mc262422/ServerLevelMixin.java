package io.github.rektroth.whiteout.mixin.mc262422;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

/**
 * Server level modifications for MC-262422 patch.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    /**
     * boilerplate
     * @param levelData                 boilerplate
     * @param dimension                 boilerplate
     * @param registryAccess            boilerplate
     * @param dimensionTypeRegistration boilerplate
     * @param isClientSide              boilerplate
     * @param isDebug                   boilerplate
     * @param biomeZoomSeed             boilerplate
     * @param maxChainedNeighborUpdates boilerplate
     */
    protected ServerLevelMixin(
        final WritableLevelData levelData,
        final ResourceKey<Level> dimension,
        final RegistryAccess registryAccess,
        final Holder<DimensionType> dimensionTypeRegistration,
        final boolean isClientSide,
        final boolean isDebug,
        final long biomeZoomSeed,
        final int maxChainedNeighborUpdates
    ) {
        super(
            levelData,
            dimension,
            registryAccess,
            dimensionTypeRegistration,
            isClientSide,
            isDebug,
            biomeZoomSeed,
            maxChainedNeighborUpdates
        );
    }

    /**
     * Modifies the existing predicate for living entities that can be struck by the lightning to exclude living
     * entities that are in spectator mode.
     * @param instance            boilerplate
     * @param baseClass           The entity class.
     * @param search              The area to search for the lightning to strike.
     * @param isAliveAndCanSeeSky The existing predicate for entities that can be struck by the lightning.
     * @return List of valid living entities for the lightning to strike.
     */
    @Redirect(
        at = @At(
            target = "Lnet/minecraft/server/level/ServerLevel;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
            value = "INVOKE"
        ),
        method = "findLightningTargetAround"
    )
    private List<LivingEntity> andIsNotSpectator(
        ServerLevel instance,
        Class<LivingEntity> baseClass,
        AABB search,
        Predicate<LivingEntity> isAliveAndCanSeeSky
    ) {
        return this.getEntitiesOfClass(
            baseClass,
            search,
            isAliveAndCanSeeSky.and((input) -> !input.isSpectator())
        );
    }
}
