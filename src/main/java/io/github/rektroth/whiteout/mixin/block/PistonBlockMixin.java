/*
 * Patch for MC-188840
 *
 * Authored for CraftBukkit/Spigot by Spottedleaf <Spottedleaf@users.noreply.github.com> on June 11, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(at = @At("HEAD"), method = "onSyncedBlockEvent", cancellable = true)
	private void dontTriggerIfWrongDirection(
		BlockState state,
		World world,
		BlockPos pos,
		int type,
		int data,
		CallbackInfoReturnable<Boolean> cir
	) {
		Direction direction = state.get(FacingBlock.FACING);
		Direction directionAsQueued = Direction.byId(data & 7);

		if (direction != directionAsQueued) {
			cir.setReturnValue(false);
		}
	}

	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
			value = "INVOKE",
			ordinal = 1
		),
		method = "onSyncedBlockEvent"
	)
	private boolean removeBlockIfNotPermanent(World instance, BlockPos pos, boolean move, @Local Direction direction) {
		if (instance.getBlockState(pos) == Blocks.PISTON_HEAD.getDefaultState().with(FacingBlock.FACING, direction)) {
			return instance.removeBlock(pos, move);
		}

		if (!instance.isClient) {
			((ServerWorld)instance).getChunkManager().markForUpdate(pos);
		}

		return true;
	}
}
