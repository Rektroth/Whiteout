/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public abstract class BlockUtil {
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
