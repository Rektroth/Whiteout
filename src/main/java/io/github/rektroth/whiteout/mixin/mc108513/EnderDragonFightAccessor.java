package io.github.rektroth.whiteout.mixin.mc108513;

import net.minecraft.world.level.dimension.end.DragonRespawnStage;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Ender dragon fight accessors for MC-108513 patch.
 */
@Mixin(EnderDragonFight.class)
public interface EnderDragonFightAccessor {
	/**
	 * Gets the dragon's respawn stage.
	 * @return The dragon's respawn stage.
	 */
	@Accessor("respawnStage")
	DragonRespawnStage getRespawnStage();
}
