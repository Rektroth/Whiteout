/*
 * Patch for MC-248588
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc248588;

import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeveledCauldronBlock.class)
public abstract class LeveledCauldronBlockMixin {
	/**
	 * Redirects the call to `canModifyAt` in `onEntityCollision` to also check that either the mob is not a player
	 * or mob griefing is enabled.
	 * @param instance    The entity in the cauldron.
	 * @param serverWorld The world the cauldron is in.
	 * @param pos         The position of the cauldron.
	 * @return True if the cauldron can be modified and either the entity is not a player or mob griefing is enabled,
	 * false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/entity/Entity;canModifyAt(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)Z",
			value="INVOKE"
		),
		method = "onEntityCollision"
	)
	private boolean canGrief(Entity instance, ServerWorld serverWorld, BlockPos pos) {
		return (instance instanceof PlayerEntity || serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
			&& instance.canModifyAt(serverWorld, pos);
	}
}
