package io.github.rektroth.whiteout.mixin.mc123848;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Item frame modifications for MC-123848 patch.
 */
@Mixin(ItemFrame.class)
public abstract class ItemFrameEntityMixin extends HangingEntity {
    /**
     * boilerplate
     * @param entityType boilerplate
     * @param level      boilerplate
     */
    protected ItemFrameEntityMixin(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Overrides the parent method to drop the item stack as an entity *below* the item frame when facing down.
     * @param level The world the item frame is in.
     * @param stack The item stack.
     * @return The item entity to drop.
     */
    @Override
    public ItemEntity spawnAtLocation(final @NonNull ServerLevel level, final @NonNull ItemStack stack) {
        return super.spawnAtLocation(level, stack, getDirection().equals(Direction.DOWN) ? 0.6F : 0.0F);
    }
}
