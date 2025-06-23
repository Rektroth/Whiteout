/*
 * Patch for MC-248588
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc248588;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(LeveledCauldronBlock.class)
public abstract class LeveledCauldronBlockMixin {
    @Shadow
    private void onFireCollision(BlockState state, World world, BlockPos pos) { }

    /**
	 * Modifies the entity collision handling to check if the entity is either a player or mob griefing is enabled
	 * before applying a change to the cauldron state.
	 * @param instance       The entity collision handler.
	 * @param collisionEvent The collision event.
	 * @param entityConsumer The entity attempting to modify the cauldron.
	 * @param state          The state of the cauldron.
	 * @param world          The world the cauldron is in.
	 * @param serverWorld    The server world the cauldron is in.
	 * @param blockPos       The position of the cauldron.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/entity/EntityCollisionHandler;addPreCallback(Lnet/minecraft/entity/CollisionEvent;Ljava/util/function/Consumer;)V",
			value = "INVOKE"
		),
		method = "onEntityCollision"
	)
	private void canGrief2(
		EntityCollisionHandler instance,
		CollisionEvent collisionEvent,
		Consumer<Entity> entityConsumer,
		@Local(argsOnly = true) BlockState state,
		@Local(argsOnly = true) World world,
		@Local ServerWorld serverWorld,
		@Local(ordinal = 1) BlockPos blockPos
	) {
		instance.addPreCallback(collisionEvent, (collidedEntity) -> {
			if (collidedEntity.isOnFire()
				&& collidedEntity.canModifyAt(serverWorld, blockPos)
				&& (collidedEntity instanceof PlayerEntity || serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) // the relevant change
			) {
				this.onFireCollision(state, world, blockPos);
			}
		});
	}
}
