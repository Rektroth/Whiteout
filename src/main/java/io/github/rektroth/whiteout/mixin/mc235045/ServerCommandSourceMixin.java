/*
 * Patch for MC-235045
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc235045;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Collections;

@Mixin(ServerCommandSource.class)
public abstract class ServerCommandSourceMixin implements CommandSource {
	@Final
	@Shadow
	private CommandOutput output;

	/**
	 * Provides a collection of entity UUIDs that are in hittable range.
	 * @return A collection of entity UUIDs.
	 */
	@Override
	public Collection<String> getEntitySuggestions() {
		if (this.output instanceof ServerPlayerEntity player) {
			final Entity cameraEntity = player.getCameraEntity();
			final double pickDistance = player.getEntityInteractionRange();
			final Vec3d min = cameraEntity.getCameraPosVec(1.0F);
			final Vec3d rotationVec = cameraEntity.getRotationVec(1.0F);

			final Vec3d max = min.add(
				rotationVec.x * pickDistance,
				rotationVec.y * pickDistance,
				rotationVec.z * pickDistance);

			final Box box = cameraEntity
				.getBoundingBox()
				.stretch(rotationVec.multiply(pickDistance))
				.expand(1.0D, 1.0D, 1.0D);

			final EntityHitResult hitResult = ProjectileUtil.raycast(
				cameraEntity,
				min,
				max,
				box,
				(e) -> !e.isSpectator() && e.canHit(),
				pickDistance * pickDistance);

			return hitResult != null ?
				Collections.singletonList(hitResult.getEntity().getUuidAsString()) :
				CommandSource.super.getEntitySuggestions();
		}

		return CommandSource.super.getEntitySuggestions();
	}
}
