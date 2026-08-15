package io.github.rektroth.whiteout.mixin.mc302297;

import com.google.common.base.Suppliers;
import io.github.rektroth.whiteout.accessors.InventoryMenuAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

/**
 * Inventory menu modifications for the MC-302297 patch.
 */
@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu implements InventoryMenuAccessor {
    /**
     * boilerplate
     * @param menuType    boilerplate
     * @param containerId boilerplate
     */
    protected InventoryMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    /**
     * Broadcasts equipment and crafting slot changes.
     */
    @Unique
    public void whiteout$broadcastNonContainerSlotChanges() {
        for (int i = InventoryMenu.RESULT_SLOT; i < InventoryMenu.ARMOR_SLOT_END; i++) {
            this.broadcastSlotChange(i);
        }

        this.broadcastSlotChange(InventoryMenu.SHIELD_SLOT);
    }

    @Unique
    private void broadcastSlotChange(int slot) {
        ItemStack item = this.slots.get(slot).getItem();
        Supplier<ItemStack> supplier = Suppliers.memoize(item::copy);
        ((AbstractContainerMenuInvoker)this).invokeTriggerSlotListeners(slot, item, supplier);
        ((AbstractContainerMenuInvoker)this).invokeSynchronizeSlotToRemote(slot, item, supplier);
    }
}
