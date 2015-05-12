/**
 * 
 * @author Zuxelus
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;

public class IC2NCCreativeTabs extends CreativeTabs {
	private static ItemStack itemToolThermometer;

	public IC2NCCreativeTabs() {
		super("Nuclear Control 2");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		if (itemToolThermometer == null) {
			itemToolThermometer = new ItemStack(IC2NuclearControl.itemToolThermometer);
		}
		return itemToolThermometer;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return IC2NuclearControl.itemToolThermometer;
	}

	@Override
	public String getTranslatedTabLabel() {
		return StatCollector.translateToLocal("nc.itemgroup.tab");
	}

}