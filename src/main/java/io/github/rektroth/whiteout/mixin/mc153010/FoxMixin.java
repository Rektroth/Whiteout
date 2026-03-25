package io.github.rektroth.whiteout.mixin.mc153010;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fox modifications for MC-143010 patch.
 */
@Mixin(Fox.class)
public class FoxMixin {
	/**
	 * Redirects checking if the fox's item stack is empty to also check if the doMobLoot gamerule is enabled.
	 * @param itemStack The fox's item stack.
	 * @param level     The world the fox is in.
	 * @return False if the fox's item stack isn't empty and the doMobLoot gamerule is enabled; true otherwise.
	 */
	@Redirect(
		at = @At(target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", value = "INVOKE"),
		method = "dropAllDeathLoot"
	)
	private static boolean notIsNotEmptyAndDoMobLoot(ItemStack itemStack, @Local(argsOnly = true)ServerLevel level) {
		return !(!itemStack.isEmpty() && level.getGameRules().get(GameRules.MOB_DROPS));
	}
}
