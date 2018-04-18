package shedar.mods.ic2.nuclearcontrol.crossmod.ic2;

import ic2.api.tile.IEnergyStorage;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.block.reactor.tileentity.TileEntityReactorAccessHatch;
import ic2.core.block.reactor.tileentity.TileEntityReactorRedstonePort;
import ic2.core.item.reactor.ItemReactorLithiumCell;
import ic2.core.item.reactor.ItemReactorMOX;
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
public class IC2ExpCross extends IC2Cross{
	
	@Override
	public int getNuclearCellTimeLeft(ItemStack par1) {
		if (par1 == null)
		{
			return 0;
		}
		if (par1.getItem() instanceof ItemReactorUranium || par1.getItem() instanceof ItemReactorLithiumCell || par1.getItem() instanceof ItemReactorMOX)
		{
			int dmg = par1.getMaxDamage() - par1.getItemDamage();
			return (dmg > 0) ? dmg : 0;
		}

		return -1;
	}

	@Override
	public IC2Type getType() {
		return IC2Type.EXP;
	}

	@Override
	public boolean isWrench(ItemStack par1) {
		return par1 != null && par1.getItem() instanceof ItemToolWrench;
	}

	@Override
	public boolean isSteamReactor(TileEntity par1) {
		return false;
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
		if(par1 == null || !(par1 instanceof TileEntityNuclearReactorElectric))
		{
			return null;
		}
		TileEntityNuclearReactorElectric reactor = (TileEntityNuclearReactorElectric)par1;
		ReactorInfo info = new ReactorInfo();
		info.isOnline = reactor.getActive();
		info.outTank = reactor.getoutputtank().getFluidAmount();
		info.inTank = reactor.getinputtank().getFluidAmount();
		info.emitHeat = reactor.EmitHeat;
		info.coreTemp = (int)(((double)reactor.getHeat() / (double)reactor.getMaxHeat()) * 100D);
		return info;
	}

	@Override
	public boolean isMultiReactorPart(TileEntity par1) {
		if(par1 instanceof TileEntityReactorRedstonePort || par1 instanceof TileEntityReactorAccessHatch)
		{
			return true;
		}
		return false;
	}

}
