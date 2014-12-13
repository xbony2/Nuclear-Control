package shedar.mods.ic2.nuclearcontrol.crossmod.RF;

import ic2.api.energy.EnergyNet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class RFTileEntityEnergyCounter extends TileEntityEnergyCounter implements IEnergyHandler{

	protected EnergyStorage storage = new EnergyStorage(32000);
	private int rec;
	private int send;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);
		storage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);
		storage.writeToNBT(nbt);
	}

	/* IEnergyConnection */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {

		return true;
	}

	/* IEnergyReceiver */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (!simulate)
			rec = maxReceive;
		return storage.receiveEnergy(maxReceive, simulate);
	}

	/* IEnergyProvider */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

		return storage.extractEnergy(maxExtract, simulate);
	}

	/* IEnergyReceiver and IEnergyProvider */
	@Override
	public int getEnergyStored(ForgeDirection from) {

		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {

		return storage.getMaxEnergyStored();
	}
	 @Override
	    public void initData() {
	        super.initData();
	    }
	 
	 @Override
	 public void updateEntity(){
		 super.updateEntity();
		 //NCLog.error(storage.getEnergyStored());

		 if(!worldObj.isRemote){
				//if (updateTicker-- == 0) {
				//	updateTicker = tickRate - 1;
					counter += rec; //If rec / 2
					this.setPowerType(TileEntityAverageCounter.POWER_TYPE_RF);
				//}
					rec = 0;
		 }
		if(storage.getEnergyStored() > 0){
			 transferEnergy();
		 } 
				
	 }
	 protected void transferEnergy() {
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity tile = getWorldObj().getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
				if (!(tile instanceof RFTileEntityAverageCounter)) {
					if (tile instanceof IEnergyHandler) {
						IEnergyHandler receiver = (IEnergyHandler) tile;
						this.sendMaxTo(receiver, direction.getOpposite());
					}
				}
			}
		}

	 public int sendMaxTo(IEnergyHandler pEnergyHandler, ForgeDirection pFrom) {
		 send = Math.min(storage.getEnergyStored(), 128);
		 return extractEnergy(pFrom, pEnergyHandler.receiveEnergy(pFrom, Math.min(storage.getEnergyStored(), 128), false), false);
		
	}

}
