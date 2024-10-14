/*
 * Patch for MC-273635
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 28, 2024.
 */

package io.github.rektroth.whiteout.util;

import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;

import java.util.Optional;

public class TrialSpawnerDataResettableWithLogic extends TrialSpawnerData {
	/**
	 * Resets the trial spawner's data, but based on provided logic.
	 * @param logic The logic.
	 */
	public void reset(TrialSpawnerLogic logic) {
		this.spawnedMobsAlive.clear();

		if (!logic.getConfig().spawnPotentials().isEmpty()) {
			this.spawnData = Optional.empty();
		}

		this.deactivate();
	}
}
