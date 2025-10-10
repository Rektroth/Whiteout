package io.github.rektroth.whiteout.mixin.mc300685;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WaypointCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WaypointCommand.class)
public class WaypointCommandMixin {
	/**
	 * Redirects getting the world that the source of the command is in to instead get the world that the waypoint
	 * itself is in if the waypoint is a living entity and only get the world that the source of the command is in if
	 * not.
	 * @param source   The source of the server command.
	 * @param waypoint The waypoint specified in the command.
	 * @return The world that the waypoint is in if it is a living entity; the world that the source of the command is
	 * in otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/server/command/ServerCommandSource;getWorld()Lnet/minecraft/server/world/ServerWorld;",
			value="INVOKE"
		),
		method = "updateWaypointConfig"
	)
	private static ServerWorld betterGetWorld(
		ServerCommandSource source,
		@Local(argsOnly = true) ServerWaypoint waypoint
	) {
		return (waypoint instanceof LivingEntity livingEntity)
			? (ServerWorld) livingEntity.getEntityWorld()
			: source.getWorld();
	}
}
