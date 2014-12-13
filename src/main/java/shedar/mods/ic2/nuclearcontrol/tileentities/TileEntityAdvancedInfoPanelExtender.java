package shedar.mods.ic2.nuclearcontrol.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;

public class TileEntityAdvancedInfoPanelExtender extends TileEntityInfoPanelExtender {
	
	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ADVANCED_EXTENDER);
	}
}
