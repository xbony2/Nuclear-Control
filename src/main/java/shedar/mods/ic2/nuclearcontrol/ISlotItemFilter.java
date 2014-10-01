package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.item.ItemStack;

public interface ISlotItemFilter {
	boolean isItemValid(int slotIndex, ItemStack itemStack);
}
