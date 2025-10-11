package io.github.rektroth.whiteout.mixin.mc153010;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoxEntity.class)
public class FoxEntityMixin {
	/**
	 * Redirects checking if the fox's item stack is empty to also check if the doMobLoot gamerule is enabled.
	 * @param itemStack The fox's item stack.
	 * @param world     The world the fox is in.
	 * @return False if the fox's item stack isn't empty and the doMobLoot gamerule is enabled; true otherwise.
	 */
	@Redirect(at = @At(target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", value = "INVOKE"), method = "drop")
	private static boolean notIsNotEmptyAndDoMobLoot(ItemStack itemStack, @Local(argsOnly = true) ServerWorld world) {
		return !(!itemStack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT));
	}
}
