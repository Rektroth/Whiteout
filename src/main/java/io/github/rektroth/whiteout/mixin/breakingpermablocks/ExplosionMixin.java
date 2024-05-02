/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.google.common.collect.Sets;
import io.github.rektroth.whiteout.util.BlockUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(Explosion.class)
public class ExplosionMixin {
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

	/**
	 * Collects the blocks and damages the entities in the explosion radius.
	 * @author Rektroth
	 * @reason Mixins are not capable of inserting "continue" into loops.
	 * I'm too new to mixins to figure out anything other than overwriting the entire thing.
	 * The `continue` that is needed is marked.
	 * If someone comes up with a better way to do this, I would prefer it.
	 */
	@Overwrite
	public void collectBlocksAndDamageEntities() {
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

						for (float p = 0.3F; h > 0.0F; h -= 0.22500001F) {
							BlockPos blockPos = BlockPos.ofFloored(m, n, o);
							BlockState blockState = this.world.getBlockState(blockPos);

							/*
							THIS IS THE CONTINUE THAT PAPER INSERTS
							 */
							if (BlockUtil.isDestroyable(blockState.getBlock())) {
								continue;
							}
							/*
							THIS IS THE CONTINUE THAT PAPER INSERTS
							 */

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
