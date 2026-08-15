package io.github.rektroth.whiteout.mixin.mc300685;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.WaypointCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Waypoint command modifications for the MC-300685 patch.
 */
@Mixin(WaypointCommand.class)
public class WaypointCommandMixin {
	/**
	 * Redirects getting the world that the source of the command is in to instead get the world that the waypoint
	 * itself is in if the waypoint is a living entity.
	 * @param instance The source of the server command.
	 * @param waypoint The waypoint specified in the command.
	 * @return The world that the waypoint is in if it is a living entity; the world that the source of the command is
	 * in otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/commands/CommandSourceStack;getLevel()Lnet/minecraft/server/level/ServerLevel;",
			value = "INVOKE"
		),
		method = "mutateIcon"
	)
	private static ServerLevel betterGetWorld(
		CommandSourceStack instance,
		@Local(argsOnly = true) WaypointTransmitter waypoint
	) {
		return (waypoint instanceof LivingEntity livingEntity)
			? (ServerLevel) livingEntity.level()
			: instance.getLevel();
	}
}
