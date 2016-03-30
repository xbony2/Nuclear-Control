package shedar.mods.ic2.nuclearcontrol.crossmod.ic2;

import ic2.api.reactor.ISteamReactor;
import ic2.api.tile.IEnergyStorage;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.item.tool.ItemToolWrench;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;

/**
 * 
 * @author Speiger
 * 
 */
public class IC2ClassicCross extends IC2Cross {

	@Override
	public int getNuclearCellTimeLeft(ItemStack par1) {
		if (par1 == null) {
			return 0;
		}
		if (par1.getItem() instanceof ItemReactorUranium) {
			return 10000 - par1.getItemDamage();
		}
		return 0;
	}

	@Override
	public boolean isSteamReactor(TileEntity par1) {
		return par1 != null && par1 instanceof ISteamReactor;
	}

	@Override
	public IC2Type getType() {
		return IC2Type.SPEIGER;
	}

	@Override
	public boolean isWrench(ItemStack par1) {
		return par1 != null && par1.getItem() instanceof ItemToolWrench;
	}

	@Override
	public EnergyStorageData getStorageData(TileEntity target) {
		if (target instanceof IEnergyStorage) {
			IEnergyStorage storage = (IEnergyStorage) target;
			EnergyStorageData result = new EnergyStorageData();
			result.capacity = storage.getCapacity();
			result.stored = storage.getStored();
			result.units = EnergyStorageData.UNITS_EU;
			result.type = EnergyStorageData.TARGET_TYPE_IC2;
			return result;
		}
		return null;
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
