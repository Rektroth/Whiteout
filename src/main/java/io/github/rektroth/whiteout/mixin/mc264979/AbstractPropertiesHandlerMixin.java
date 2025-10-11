package io.github.rektroth.whiteout.mixin.mc264979;

import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Mixin(AbstractPropertiesHandler.class)
public class AbstractPropertiesHandlerMixin {
	/**
	 * Skips loading the server.properties file if it doesn't exist in the first place.
	 * @param path The path of the server.properties file.
	 * @param cir  The returnable callback information.
	 */
	@Inject(at = @At(value = "HEAD"), method = "loadProperties", cancellable = true)
	private static void dontLoadPropertiesIfFileDoesntExist(Path path, CallbackInfoReturnable<Properties> cir) {
		if (!Files.exists(path)) {
			cir.setReturnValue(new Properties());
		}
	}
}
