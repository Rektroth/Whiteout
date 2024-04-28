/*
 * Patch for MC-248588
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.block;

import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeveledCauldronBlock.class)
public abstract class LeveledCauldronBlockMixin {
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/entity/Entity;canModifyAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
			value="INVOKE"
		),
		method = "onEntityCollision"
	)
	private boolean fixedCanModifyAt(Entity instance, World world, BlockPos pos) {
		return (instance instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
			&& instance.canModifyAt(world, pos);
	}
}
