package io.github.rektroth.whiteout.mixin.mc302297;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessors of private abstract container menu fields for the MC-302297 patch.
 */
@Mixin(Player.class)
public interface PlayerAccessor {
    @Accessor("lastItemInMainHand")
    ItemStack whiteout$getLastItemInMainHand();

    @Accessor("lastItemInMainHand")
    void whiteout$setLastItemInMainHand(ItemStack stack);
}
