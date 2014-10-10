package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFilter extends Slot {
	private final int slotIndex;

	public SlotFilter(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
		this.slotIndex = slotIndex;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (inventory instanceof ISlotItemFilter)
			return ((ISlotItemFilter) inventory).isItemValid(slotIndex, itemStack);
		return super.isItemValid(itemStack);
	}

}
