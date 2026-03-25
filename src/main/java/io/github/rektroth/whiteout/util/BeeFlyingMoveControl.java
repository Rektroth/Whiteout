package io.github.rektroth.whiteout.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;

/**
 * An extension of `FlyingMoveControl` specifically for bees.
 */
public class BeeFlyingMoveControl extends FlyingMoveControl {
	public BeeFlyingMoveControl(final Mob entity, final int maxPitchChange, final boolean noGravity) {
		super(entity, maxPitchChange, noGravity);
	}

	@Override
	public void tick() {
		if (this.mob.getY() <= this.mob.level().getMinY()) {
			this.mob.setNoGravity(false);
		}

		super.tick();
	}
}
