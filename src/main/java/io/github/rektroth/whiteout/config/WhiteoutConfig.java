/*
 * borrowed from jellysquid
 * please don't hurt me :(
 */

package io.github.rektroth.whiteout.config;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.CustomValue.CvType;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WhiteoutConfig {
	private static final Logger LOGGER = LogManager.getLogger("WhiteoutConfig");
	private static final String JSON_KEY_WHITEOUT_OPTIONS = "whiteout:options";
	private final Map<String, Option> options = new HashMap<>();
	private final Set<Option> optionsWithDependencies = new ObjectLinkedOpenHashSet<>();

	private WhiteoutConfig() {
		// Defines the default rules which can be configured by the user or other mods.
		InputStream defaultPropertiesStream = WhiteoutConfig.class.getResourceAsStream("/assets/whiteout/whiteout-mixin-config-default.properties");

		if (defaultPropertiesStream == null) {
			throw new IllegalStateException("Whiteout mixin config default properties could not be read!");
		}

		try (BufferedReader propertiesReader = new BufferedReader(new InputStreamReader(defaultPropertiesStream))) {
			Properties properties = new Properties();
			properties.load(propertiesReader);
			properties.forEach((ruleName, enabled) -> this.addMixinRule((String) ruleName, Boolean.parseBoolean((String) enabled)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Whiteout mixin config default properties could not be read!");
		}
	}

	public static WhiteoutConfig load(File file) {
		WhiteoutConfig config = new WhiteoutConfig();

		if (file.exists()) {
			Properties props = new Properties();

			try (FileInputStream fin = new FileInputStream(file)) {
				props.load(fin);
			} catch (IOException e) {
				throw new RuntimeException("Could not load config file", e);
			}

			config.readProperties(props);
		} else {
			try {
				writeDefaultConfig(file);
			} catch (IOException e) {
				LOGGER.warn("Could not write default configuration file", e);
			}
		}

		config.applyModOverrides();
		return config;
	}

	private void addMixinRule(String mixin, boolean enabled) {
		if (this.options.putIfAbsent(mixin, new Option(mixin, enabled, false)) != null) {
			throw new IllegalStateException("Mixin rule already defined: " + mixin);
		}
	}

	private void readProperties(Properties props) {
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			Option option = this.options.get(key);

			if (option == null) {
				LOGGER.warn("No configuration key exists with name '{}', ignoring", key);
				continue;
			}

			boolean enabled;

			if (value.equalsIgnoreCase("true")) {
				enabled = true;
			} else if (value.equalsIgnoreCase("false")) {
				enabled = false;
			} else {
				LOGGER.warn("Invalid value '{}' encountered for configuration key '{}', ignoring", value, key);
				continue;
			}

			option.setEnabled(enabled, true);
		}
	}

	private void applyModOverrides() {
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			ModMetadata meta = container.getMetadata();

			if (meta.containsCustomValue(JSON_KEY_WHITEOUT_OPTIONS)) {
				CustomValue overrides = meta.getCustomValue(JSON_KEY_WHITEOUT_OPTIONS);

				if (overrides.getType() != CvType.OBJECT) {
					LOGGER.warn("Mod '{}' contains invalid Whiteout option overrides, ignoring", meta.getId());
					continue;
				}

				for (Map.Entry<String, CustomValue> entry : overrides.getAsObject()) {
					this.applyModOverride(meta, entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private void applyModOverride(ModMetadata meta, String name, CustomValue value) {
		Option option = this.options.get(name);

		if (option == null) {
			LOGGER.warn("Mod '{}' attempted to override option '{}', which doesn't exist, ignoring", meta.getId(), name);
			return;
		}

		if (value.getType() != CvType.BOOLEAN) {
			LOGGER.warn("Mod '{}' attempted to override option '{}' with an invalid value, ignoring", meta.getId(), name);
			return;
		}

		boolean enabled = value.getAsBoolean();

		// disabling the option takes precedence over enabling
		if (!enabled && option.isEnabled()) {
			option.clearModsDefiningValue();
		}

		if (!enabled || option.isEnabled() || option.getDefiningMods().isEmpty()) {
			option.addModOverride(enabled, meta.getId());
		}
	}

	public Option getEffectiveOptionForMixin(String mixinClassName) {
		String key = mixinClassName.substring(0, mixinClassName.indexOf('.'));
		return this.options.get(key);
	}

	private static void writeDefaultConfig(File file) throws IOException {
		File dir = file.getParentFile();

		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IOException("Could not create parent directories");
			}
		} else if (!dir.isDirectory()) {
			throw new IOException("The parent file is not a directory");
		}

		try (Writer writer = new FileWriter(file)) {
			writer.write("# This is the configuration file for Whiteout.\n");
			writer.write("# By default, this file will be empty except for this notice.\n");
		}
	}

	public int getOptionCount() {
		return this.options.size();
	}

	public int getOptionOverrideCount() {
		return (int) this.options.values()
			.stream()
			.filter(Option::isOverridden)
			.count();
	}

	public boolean isCompatMixin(String mixinClassName) {
		String key = mixinClassName.substring(0, mixinClassName.indexOf('.'));
		return key.equals("compat");
	}

	public String getCompatMod(String mixinClassName) {
		if (isCompatMixin(mixinClassName)) {
			String key = mixinClassName.substring(0, mixinClassName.indexOf('.'));
			return mixinClassName.substring((key + ".compat.").length(), mixinClassName.indexOf('.'));
		}

		return null;
	}
}
