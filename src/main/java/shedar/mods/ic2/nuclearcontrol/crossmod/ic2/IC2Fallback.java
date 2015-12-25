package shedar.mods.ic2.nuclearcontrol.crossmod.ic2;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;

/**
 * 
 * @author Speiger
 * 
 */
public class IC2Fallback extends IC2Cross {

	@Override
	public int getNuclearCellTimeLeft(ItemStack par1) {
		return 0;
	}

	@Override
	public boolean isWrench(ItemStack par1) {
		return false;
	}

	@Override
	public EnergyStorageData getStorageData(TileEntity target) {
		return null;
	}

	@Override
	public IC2Type getType() {
		return IC2Type.NONE;
	}

	@Override
	public boolean isSteamReactor(TileEntity par1) {
		return false;
	}

	@Override
	public ReactorInfo getReactorInfo(TileEntity par1) {
		return null;
	}

	@Override
	public boolean isMultiReactorPart(TileEntity par1) {
		return false;
	}

}
