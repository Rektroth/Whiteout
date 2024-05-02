package io.github.rektroth.whiteout;

import io.github.rektroth.whiteout.config.WhiteoutConfig;
import net.fabricmc.api.ModInitializer;

public class Whiteout implements ModInitializer {
	public static WhiteoutConfig CONFIG;

	@Override
	public void onInitialize() {
		if (CONFIG == null) {
			throw new IllegalStateException("The mixin plugin did not initialize the config! Did it not load?");
		}
	}
}
