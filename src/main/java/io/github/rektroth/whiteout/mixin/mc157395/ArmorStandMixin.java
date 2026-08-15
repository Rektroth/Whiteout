package io.github.rektroth.whiteout.mixin.mc157395;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Armor stand modifications for MC-157395 patch.
 */
@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends LivingEntity {
	/**
	 * boilerplate
	 * @param type  boilerplate
	 * @param level boilerplate
	 */
	protected ArmorStandMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	/**
	 * Overrides the check for if the entity should drop experience (which only checks if its a baby)
	 * to always return true.
	 * @return True.
	 */
	@Override
	public boolean shouldDropExperience() {
		return true;
	}

	/**
	 * Overrides the check for if the entity should drop experience to skip the check if it's a baby.
	 * @param level The world.
	 * @return True if the world's game-rules have mob drops enabled; otherwise false.
	 */
	@Override
	public boolean shouldDropLoot(final ServerLevel level) {
		return level.getGameRules().get(GameRules.MOB_DROPS);
	}
}
