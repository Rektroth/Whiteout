/*
 * Patch for MC-224454
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.block;

import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AzaleaBlock.class)
public abstract class AzaleaBlockMixin extends PlantBlock {
    protected AzaleaBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
