package io.github.rektroth.whiteout.mixin;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class WhiteoutMixinPlugin implements IMixinConfigPlugin {
	private static final Supplier<Boolean> TRUE = () -> true;

	private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
		"io.github.rektroth.whiteout.mixin.nethervoiddamage.compat.lithium.DontLinkToPortalOnRoof", () -> FabricLoader.getInstance().isModLoaded("lithium"),
		"io.github.rektroth.whiteout.mixin.nethervoiddamage.DontLinkToPortalOnRoof", () -> !FabricLoader.getInstance().isModLoaded("lithium")
	);

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
	}

	// boilerplate

	@Override
	public void onLoad(String mixinPackage) {

	}

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
