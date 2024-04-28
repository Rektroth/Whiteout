/*
 * Patch for MC-188840
 *
 * Authored for CraftBukkit/Spigot by Spottedleaf <Spottedleaf@users.noreply.github.com> on June 11, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/World;addBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)V",
			value = "INVOKE"),
		method = "move"
	)
	private void fixMove(
		World instance,
		BlockEntity blockEntity,
		@Local(argsOnly = true) Direction dir,
		@Local(argsOnly = true) boolean retract,
		@Local(ordinal = 0) List<BlockPos> list,
		@Local(ordinal = 1) int j,
		@Local LocalRef<Map<BlockPos, BlockState>> map,
		@Local(ordinal = 1) LocalRef<BlockState> blockState2,
		@Local(ordinal = 1) BlockState blockState3,
		@Local(ordinal = 2) BlockPos blockPos3
	) {
		BlockPos oldPos = list.get(j);
		blockState2.set(instance.getBlockState(oldPos));

		Map<BlockPos, BlockState> m = map.get();
		m.replace(oldPos, blockState2.get());
		map.set(m);

		instance.addBlockEntity(PistonExtensionBlock
			.createBlockEntityPiston(blockPos3, blockState3, blockState2.get(), dir, retract, false));
		instance.setBlockState(
			oldPos,
			Blocks.AIR.getDefaultState(),
			Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.MOVED | 1024
		);
	}
}
