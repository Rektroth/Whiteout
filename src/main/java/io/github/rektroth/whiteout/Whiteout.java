package io.github.rektroth.whiteout;

import io.github.rektroth.whiteout.config.WhiteoutConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Whiteout implements ModInitializer {
	public static WhiteoutConfig CONFIG;
	public static final Logger LOGGER = LoggerFactory.getLogger("whiteout");

	@Override
	public void onInitialize() {
		if (CONFIG == null) {
			throw new IllegalStateException("The mixin plugin did not initialize the config! Did it not load?");
		}
	}
}
