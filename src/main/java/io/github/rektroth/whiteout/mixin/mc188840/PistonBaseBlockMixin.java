package io.github.rektroth.whiteout.mixin.mc188840;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;

/**
 * Piston base block modifications for MC-188840 patch.
 */
@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin {
	/**
	 * Redirects the call to `addBlockEntity` in `move` to both add the block entity and immediately set the block
	 * state of the position of the head to "air" until the head is finished moving, preventing the head from being
	 * destroyed.
	 * (This is my limited technical understanding. It might be mistaken.)
	 * @param instance    The world the piston is in.
	 * @param blockEntity boilerplate
	 * @param dir         The direction the piston is facing.
	 * @param retract     Whether the piston is being retracted.
	 * @param list        List of neighboring blocks.
	 * @param i           The current loop iteration.
	 * @param map         Map of block states and positions.
	 * @param blockState2 The state of the pushed block.
	 * @param blockState3 The state of the piston.
	 * @param blockPos3   The position at which the piston head is to be made.
	 */
	@Redirect(
		at = @At(
			ordinal = 0,
			target = "Lnet/minecraft/world/level/Level;setBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V",
			value = "INVOKE"),
		method = "moveBlocks"
	)
	private void createPistonHeadWithAir(
		Level instance,
		BlockEntity blockEntity,
		@Local(argsOnly = true) Direction dir,
		@Local(argsOnly = true) boolean retract,
		@Local(ordinal = 0) List<BlockPos> list,
		@Local(ordinal = 1) int i,
		@Local LocalRef<Map<BlockPos, BlockState>> map,
		@Local(ordinal = 1) LocalRef<BlockState> blockState2,
		@Local(ordinal = 1) BlockState blockState3,
		@Local(ordinal = 2) BlockPos blockPos3
	) {
		BlockPos oldPos = list.get(i);
		blockState2.set(instance.getBlockState(oldPos));

		Map<BlockPos, BlockState> m = map.get();
		m.replace(oldPos, blockState2.get());
		map.set(m);

		instance.setBlockEntity(MovingPistonBlock
			.newMovingBlockEntity(blockPos3, blockState3, blockState2.get(), dir, retract, false));
		instance.setBlock(
			oldPos,
			Blocks.AIR.defaultBlockState(),
			Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_MOVE_BY_PISTON | 1024
		);
	}
}
