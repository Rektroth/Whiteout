package io.github.rektroth.whiteout.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public abstract class BlockUtil {
	/**
	 * Returns whether a given block is destroyable (not permanent).
	 * @param block The block.
	 * @return Whether the given block is destroyable.
	 */
	public static boolean isDestroyable(Block block) {
		return block != Blocks.BEDROCK
			&& block != Blocks.END_PORTAL_FRAME
			&& block != Blocks.END_PORTAL
			&& block != Blocks.END_GATEWAY
			&& block != Blocks.COMMAND_BLOCK
			&& block != Blocks.REPEATING_COMMAND_BLOCK
			&& block != Blocks.CHAIN_COMMAND_BLOCK
			&& block != Blocks.BARRIER
			&& block != Blocks.STRUCTURE_BLOCK
			&& block != Blocks.JIGSAW;
	}
}
