package shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors;

import net.minecraft.nbt.NBTTagCompound;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import erogenousbeef.bigreactors.common.multiblock.interfaces.ITickableMultiblockPart;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityReactorPartBase;
import erogenousbeef.core.multiblock.MultiblockValidationException;

public class TileEntityBlockFetcher extends TileEntityReactorPartBase implements ITickableMultiblockPart{
	private boolean isReactorOn;
	private float energyStored;
	private float energyGen;
	private float EstoredPer;
	private int temp;
	private boolean shouldBlockCache;

	public TileEntityBlockFetcher(){
		shouldBlockCache = false;
	}
	@Override
	public void isGoodForBottom() throws MultiblockValidationException {}

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Reactor Monitor may only be used on the exterior faces, not as part of a reactor's frame or interior", xCoord, yCoord, zCoord));
	
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Reactor Monitor may only be used on the exterior faces, not as part of a reactor's frame or interior", xCoord, yCoord, zCoord));
		
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {}

	@Override
	public void onMachineActivated() {}

	@Override
	public void onMachineDeactivated() {

	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		
	}
	
	@Override
	public void onMultiblockServerTick() {
		if(shouldBlockCache){
			isReactorOn = this.getReactorController().getActive();
			energyStored = this.getReactorController().getEnergyStored();
			energyGen = this.getReactorController().getEnergyGeneratedLastTick();
			EstoredPer = this.getReactorController().getEnergyStoredPercentage();
			temp = (int) this.getReactorController().getFuelHeat();
		//NCLog.fatal(this.getReactorController().getEnergyStored());//Current Stored Energy
		//NCLog.fatal(energyStored);
		//NCLog.fatal(this.getReactorController().getActive());//On or Off	
		//NCLog.fatal(this.getReactorController().getEnergyGeneratedLastTick());//String.format("%.2f flux per tick", this.getReactorController().getEnergyGeneratedLastTick())
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		shouldBlockCache = tag.getBoolean("Cache");
	}
	@Override
	 public void writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setBoolean("Cache", shouldBlockCache);
	}
	
	public float getEnergyStored(){
		return energyStored;
	}
	public float getEnergyGenerated(){
		return energyGen;
	}
	public boolean isReactorOnline(){
		return isReactorOn;
	}
	public void startFetching(){
		shouldBlockCache = true;
	}
	public float getEnergyOutPercent(){
		return EstoredPer;
	}
	public int getTemp(){
		return temp;
	}
}
