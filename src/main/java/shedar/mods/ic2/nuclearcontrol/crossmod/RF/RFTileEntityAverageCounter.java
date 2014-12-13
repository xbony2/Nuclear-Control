package shedar.mods.ic2.nuclearcontrol.crossmod.RF;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class RFTileEntityAverageCounter extends TileEntityAverageCounter implements IEnergyHandler{

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
		 if(storage.getEnergyStored() > 0){
			 transferEnergy();
		 }
		 if(!worldObj.isRemote){
			index = (index+1) % 11 * 20;
			data[index] = 0;
			double total = getAverage();
			//NCLog.fatal("Extract: " + send);
			//NCLog.fatal("RECIEVE: " + rec);
			data[index] = total;
			setPowerType(POWER_TYPE_RF);
			send = 0;
			rec = 0;
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

/*
 * @Override
	public void updateEntity(){
		if (!init){
			initData();
			markDirty();
		}
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if (!worldObj.isRemote){
			if (!addedToEnergyNet){
				EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(event);
				prevTotal = EnergyNet.instance.getTotalEnergyEmitted(this);
				addedToEnergyNet = true;
			}
			if (updateTicker-- == 0){
				updateTicker = tickRate-1;
				index = (index+1) % DATA_POINTS;
				data[index] = 0;
				getAverage();
				double total = EnergyNet.instance.getTotalEnergyEmitted(this);
				if(total > 0){
					if(prevTotal!=-1){
						total = total - prevTotal;
						prevTotal += total;
					}else{
						prevTotal = total;
					}
					if(total>0)
						data[index] += total;
					setPowerType(POWER_TYPE_EU);
				}
			}
		}
		super.updateEntity();
	}
 */
}
