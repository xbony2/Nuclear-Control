package shedar.mods.ic2.nuclearcontrol.crossmod.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.guihook.IContainerTooltipHandler;

public class TooltipHandler implements IContainerTooltipHandler {
	@Override
	public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
		return currenttip;
	}

	@Override
	public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> arg2) {
		if (itemstack != null && itemstack.hasTagCompound()) {
			NBTTagCompound tags = itemstack.getTagCompound();
			if (tags.hasKey("_webSensorId")) {
				long id = tags.getLong("_webSensorId");
				if (id > 0) arg2.add("Web Id: " + id);
			}
		}
		return arg2;
	}

	@Override
	public List<String> handleItemTooltip(GuiContainer arg0, ItemStack arg1, int arg2, int arg3, List<String> arg4) {
		return arg4;
	}

}
