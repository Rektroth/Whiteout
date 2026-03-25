package io.github.rektroth.whiteout.mixin.mc302297;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuInvoker {
    @Invoker("triggerSlotListeners")
    void triggerSlotListeners(final int i, final ItemStack current, final Supplier<ItemStack> currentCopy);

    @Invoker("synchronizeSlotToRemote")
    void synchronizeSlotToRemote(final int i, final ItemStack current, final Supplier<ItemStack> currentCopy);
}
