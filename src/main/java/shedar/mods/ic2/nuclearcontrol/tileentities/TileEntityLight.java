package shedar.mods.ic2.nuclearcontrol.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLight extends TileEntity{
	public boolean isOn;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		
		this.isOn = nbt.getBoolean("IsLightOn");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setBoolean("IsLightOn", isOn);
	}
	
	public void updateEntity(){
		if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)){
			turnOn();
		}else{
			turnOff();
		}
	}
	
	public void turnOn(){
		this.isOn = true;
		this.getBlockType().setLightLevel(15);
		
	}
	
	public void turnOff(){
		this.isOn = false;
		this.getBlockType().setLightLevel(0);
	}
}
