package io.github.rektroth.whiteout.mixin.mc33041;

import com.mojang.datafixers.DataFixer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.notifications.NotificationManager;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * Dedicated server modifications for MC-33041 patch.
 */
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {
	@Final
    @Shadow
	private static Logger LOGGER;

	/**
	 * Handles console input.
	 * @param msg    The message.
	 * @param source The command source.
	 */
	@Shadow
	public void handleConsoleInput(final String msg, final CommandSourceStack source) { }

	/**
	 * boilerplate
	 * @param serverThread        boilerplate
	 * @param storageSource       boilerplate
	 * @param packRepository      boilerplate
	 * @param worldStem           boilerplate
	 * @param gameRules           boilerplate
	 * @param proxy               boilerplate
	 * @param fixerUpper          boilerplate
	 * @param services            boilerplate
	 * @param levelLoadListener   boilerplate
	 * @param propagatesCrashes   boilerplate
	 * @param notificationManager boilerplate
	 */
	public DedicatedServerMixin(
		Thread serverThread,
		LevelStorageSource.LevelStorageAccess storageSource,
		PackRepository packRepository,
		WorldStem worldStem,
		Optional<GameRules> gameRules,
		Proxy proxy,
		DataFixer fixerUpper,
		Services services,
		LevelLoadListener levelLoadListener,
		boolean propagatesCrashes,
		NotificationManager notificationManager
	) {
		super(
			serverThread,
			storageSource,
			packRepository,
			worldStem,
			gameRules,
			proxy,
			fixerUpper,
			services,
			levelLoadListener,
			propagatesCrashes,
			notificationManager);
	}

	/**
	 * Replaces the console thread with one that will immediately return if the server is executed in javaw.
	 * @param thread The original console thread.
	 * @return A modified console thread.
	 */
	@ModifyVariable(at = @At("STORE"), method = "initServer", name = "consoleThread")
	public Thread betterConsoleThread(Thread thread) {
		return new Thread("Server console handler") {
			{
				Objects.requireNonNull(DedicatedServerMixin.this);
			}

			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

				// start of relevant portion
				try {
					System.in.available();
				} catch (IOException ex) {
					return;
				}
				// end of relevant portion

				String line;

				try {
					while (!DedicatedServerMixin.this.isStopped() && DedicatedServerMixin.this.isRunning() && (line = reader.readLine()) != null) {
						DedicatedServerMixin.this.handleConsoleInput(line, DedicatedServerMixin.this.createCommandSourceStack());
					}
				} catch (IOException var4) {
					DedicatedServerMixin.LOGGER.error("Exception handling console input", (Throwable)var4);
				}
			}
		};
	}
}
