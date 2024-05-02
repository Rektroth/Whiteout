/*
 * borrowed from jellysquid
 * please don't hurt me :(
 */

package io.github.rektroth.whiteout.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.rektroth.whiteout.Whiteout;
import io.github.rektroth.whiteout.config.Option;
import io.github.rektroth.whiteout.config.WhiteoutConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class WhiteoutMixinPlugin implements IMixinConfigPlugin {
	private static final String MIXIN_PACKAGE_ROOT = "io.github.rektroth.whiteout.mixin.";
	private final Logger logger = LogManager.getLogger("Whiteout");
	private static final Supplier<Boolean> TRUE = () -> true;
	private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
		"nethervoiddamage.compat.lithium.LithiumCompatPortalForcerMixin", () -> FabricLoader.getInstance().isModLoaded("lithium"),
		"nethervoiddamage.PortalForcerMixin", () -> !FabricLoader.getInstance().isModLoaded("lithium"),
		"mc27056.compat.lithium.LithiumCompatExplosionMixin", () -> FabricLoader.getInstance().isModLoaded("lithium"),
		"mc27056.ExplosionsMixin", () -> !FabricLoader.getInstance().isModLoaded("lithium")
	);

	private WhiteoutConfig config;

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!mixinClassName.startsWith(MIXIN_PACKAGE_ROOT)) {
			this.logger.error("Expected mixin '{}' to start with package root '{}', treating as foreign and " +
				"disabling!", mixinClassName, MIXIN_PACKAGE_ROOT);

			return false;
		}

		String mixin = mixinClassName.substring(MIXIN_PACKAGE_ROOT.length());

		if (!CONDITIONS.getOrDefault(mixin, TRUE).get()) {
			return false;
		}

		Option option = this.config.getEffectiveOptionForMixin(mixin);

		if (option == null) {
			this.logger.error("No rules matched mixin '{}', treating as foreign and disabling!", mixin);

			return false;
		}

		if (option.isOverridden()) {
			String source = "[unknown]";

			if (option.isUserDefined()) {
				source = "user configuration";
			} else if (option.isModDefined()) {
				source = "mods [" + String.join(", ", option.getDefiningMods()) + "]";
			}

			if (option.isEnabled()) {
				this.logger.warn("Force-enabling mixin '{}' as rule '{}' (added by {}) enables it", mixin,
					option.getName(), source);
			} else {
				this.logger.warn("Force-disabling mixin '{}' as rule '{}' (added by {}) disables it and children", mixin,
					option.getName(), source);
			}
		}

		return option.isEnabled();
	}

	@Override
	public void onLoad(String mixinPackage) {
		try {
			this.config = WhiteoutConfig.load(new File("./config/whiteout.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Could not load configuration file for Whiteout", e);
		}

		this.logger.info(
			"Loaded configuration file for Whiteout: {} options available, {} override(s) found",
			this.config.getOptionCount(),
			this.config.getOptionOverrideCount());

		Whiteout.CONFIG = this.config;
	}

	// boilerplate

	@Override
	public String getRefMapperConfig() {
		return "";
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
