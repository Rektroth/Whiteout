/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.world.explosion;

import com.google.common.collect.Sets;
import io.github.rektroth.whiteout.util.BlockUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Final
    @Shadow
    private ObjectArrayList<BlockPos> affectedBlocks;

    @Final
    @Shadow
    private Map<PlayerEntity, Vec3d> affectedPlayers;

    @Final
    @Shadow
    private ExplosionBehavior behavior;

    @Final
    @Shadow
    private DamageSource damageSource;

    @Final
    @Shadow
    private Entity entity;

    @Final
    @Shadow
    private float power;

    @Final
    @Shadow
    private World world;

    @Final
    @Shadow
    private double x;

    @Final
    @Shadow
    private double y;

    @Final
    @Shadow
    private double z;

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/explosion/ExplosionBehavior;canDestroyBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;F)Z"),
        locals = LocalCapture.CAPTURE_FAILHARD,
        method = "collectBlocksAndDamageEntities")
    private void noHeadlessPiston(
        CallbackInfo ci,
        Set<BlockPos> set,
        int i,
        int j,
        int k,
        int l,
        double d,
        double e,
        double f,
        double g,
        float h,
        double m,
        double n,
        double o,
        float p,
        BlockPos blockPos,
        BlockState blockState
    ) {
        if (blockState.getBlock() == Blocks.MOVING_PISTON) {
            BlockEntity extension = this.world.getBlockEntity(blockPos);

            if (extension instanceof PistonBlockEntity blockEntity && blockEntity.isSource()) {
                Direction direction = blockState.get(PistonHeadBlock.FACING);
                set.add(blockPos.offset(direction.getOpposite()));
            }
        }
    }

    private void collectBlocksAndDamageEntitiesWithoutDestroyingIndestructables() {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        Set<BlockPos> set = Sets.newHashSet();

        int k;
        int l;
        for(int j = 0; j < 16; ++j) {
            for(k = 0; k < 16; ++k) {
                for(l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = ((float)j / 15.0F * 2.0F - 1.0F);
                        double e = ((float)k / 15.0F * 2.0F - 1.0F);
                        double f = ((float)l / 15.0F * 2.0F - 1.0F);
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double m = this.x;
                        double n = this.y;
                        double o = this.z;

                        for(float p = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                            BlockState blockState = this.world.getBlockState(blockPos);

                            if (BlockUtil.isDestroyable(blockState.getBlock())) {
                                continue;
                            }

                            FluidState fluidState = this.world.getFluidState(blockPos);

                            if (!this.world.isInBuildLimit(blockPos)) {
                                break;
                            }

                            Optional<Float> optional = this.behavior.getBlastResistance((Explosion)(Object)this, this.world, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (h > 0.0F && this.behavior.canDestroyBlock((Explosion)(Object)this, this.world, blockPos, blockState, h)) {
                                set.add(blockPos);
                            }

                            m += d * 0.30000001192092896;
                            n += e * 0.30000001192092896;
                            o += f * 0.30000001192092896;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0F;
        k = MathHelper.floor(this.x - (double)q - 1.0);
        l = MathHelper.floor(this.x + (double)q + 1.0);
        int r = MathHelper.floor(this.y - (double)q - 1.0);
        int s = MathHelper.floor(this.y + (double)q + 1.0);
        int t = MathHelper.floor(this.z - (double)q - 1.0);
        int u = MathHelper.floor(this.z + (double)q + 1.0);
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, r, t, l, s, u));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
        Iterator<Entity> var34 = list.iterator();

        while(true) {
            Entity entity;
            double w;
            double x;
            double y;
            double v;
            double z;
            do {
                do {
                    do {
                        if (!var34.hasNext()) {
                            return;
                        }

                        entity = var34.next();
                    } while(entity.isImmuneToExplosion((Explosion)(Object)this));

                    v = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double)q;
                } while(!(v <= 1.0));

                w = entity.getX() - this.x;
                x = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                y = entity.getZ() - this.z;
                z = Math.sqrt(w * w + x * x + y * y);
            } while(z == 0.0);

            w /= z;
            x /= z;
            y /= z;
            if (this.behavior.shouldDamage((Explosion)(Object)this, entity)) {
                entity.damage(this.damageSource, this.behavior.calculateDamage((Explosion)(Object)this, entity));
            }

            double aa = (1.0 - v) * (double)Explosion.getExposure(vec3d, entity) * (double)this.behavior.getKnockbackModifier(entity);
            double ab;
            if (entity instanceof LivingEntity livingEntity) {
                ab = ProtectionEnchantment.transformExplosionKnockback(livingEntity, aa);
            } else {
                ab = aa;
            }

            w *= ab;
            x *= ab;
            y *= ab;
            Vec3d vec3d2 = new Vec3d(w, x, y);
            entity.setVelocity(entity.getVelocity().add(vec3d2));
            if (entity instanceof PlayerEntity playerEntity) {
                if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                    this.affectedPlayers.put(playerEntity, vec3d2);
                }
            }

            entity.onExplodedBy(this.entity);
        }
    }
}
