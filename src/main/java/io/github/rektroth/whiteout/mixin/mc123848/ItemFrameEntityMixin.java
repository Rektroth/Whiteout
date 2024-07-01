/*
 * Patch for MC-123848
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 25, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc123848;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {
    /**
     * boilerplate
     * @param entityType boilerplate
     * @param world      boilerplate
     */
    protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Overrides the parent method to drop the item stack as an entity *below* the item frame when facing down.
     * @param stack The item stack.
     * @return The item entity to drop.
     */
    @Nullable
    @Override
    public ItemEntity dropStack(ItemStack stack) {
        return this.dropStack(stack, getMovementDirection().equals(Direction.DOWN) ? -0.6F : 0.0F);
    }
}
