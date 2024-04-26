/*
 * Patch for MC-108513
 *
 * Authored for CraftBukkit/Spigot by Max Lee <max@themoep.de> on May 27, 2021.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity.decoration;

import io.github.rektroth.whiteout.accessors.GeneratedByDragonFightAccessor;
import io.github.rektroth.whiteout.mixin.entity.boss.dragon.EnderDragonFightAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import java.util.Objects;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity implements GeneratedByDragonFightAccessor {
    @Unique
    public boolean generatedByDragonFight = false;

    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void setBeamTarget(@Nullable BlockPos beamTarget);

    @Unique
    public void whiteout$setGeneratedByDragonFight(boolean generatedByDragonFight) {
        this.generatedByDragonFight = generatedByDragonFight;
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tickNotInvulnerable(CallbackInfo ci) {
        if (this.generatedByDragonFight && this.isInvulnerable()) {
            // I can't figure out what `uuid` and `getOriginalWorld()` map to in Fabric
            // It might be possible that 1.20.5 got rid of them entirely?
            // Will want to check how Paper modified this function after they update
            // The fix as-is will still prevent most common exploits
            if (/*!Objects.equals(((ServerWorld)this.getWorld()).uuid, this.getOriginWorld())
                ||*/ ((ServerWorld)this.getWorld()).getEnderDragonFight() == null
                || ((EnderDragonFightAccessor)((ServerWorld)this.getWorld()).getEnderDragonFight()).getDragonSpawnState() == null
                || ((EnderDragonFightAccessor)((ServerWorld)this.getWorld()).getEnderDragonFight()).getDragonSpawnState().ordinal() > EnderDragonSpawnState.SUMMONING_DRAGON.ordinal()
            ) {
                this.setInvulnerable(false);
                this.setBeamTarget(null);
            }
        }
    }

    // Prefixing the custom NBT data with "Paper." makes this fix cross-compatible with Paper worlds

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    private void writeGeneratedByDragonFightToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.generatedByDragonFight) {
            nbt.putBoolean("Paper.GeneratedByDragonFight", this.generatedByDragonFight);
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    private void readGeneratedByDragonFightFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Paper.GeneratedByDragonFight")) {
            this.generatedByDragonFight = nbt.getBoolean("Paper.GeneratedByDragonFight");
        }
    }
}
