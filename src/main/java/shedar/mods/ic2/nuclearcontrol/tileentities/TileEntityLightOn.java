package shedar.mods.ic2.nuclearcontrol.tileentities;

import shedar.mods.ic2.nuclearcontrol.blocks.BlockNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.FloodLight;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityLightOn extends TileEntity implements IEnergySink{
	
	final double maxEnergy = 64;
	final int maxInput = 32;
	final double energyUsedPerTick = 1;
	
	double energy = 0;
	boolean addedToEnergyNet = false;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		energy = nbt.getDouble("energy");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setDouble("energy", energy);
    }

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		double req;
	    if (energy < maxEnergy / 2)
	    	req = maxEnergy - energy;
	    else
	    	req = 0;
			return req;
	    
	}
	
	@Override
	public int getSinkTier() {
		return 1;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
	  	if (amount > maxInput){
	  		explodeMachineAt(getWorldObj(), xCoord, yCoord, zCoord);
	  		return 0;
	  	}
	  	boolean hadEnergy = energy > 0;
	  	energy += amount;
	  	if (hadEnergy != (energy > 0)) {
	  		markDirty();
	  		updateBlock();
	  	}
	  	return 0;
	}
	
	public void updateEntity() {
		if (!worldObj.isRemote) {
			if (!this.addedToEnergyNet) {
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
				this.addedToEnergyNet = true;
			}
			if (isActive()) {
				energy -= energyUsedPerTick;
				if (energy < 0)
					energy = 0;
				markDirty();
				if (!isActive())
					updateBlock();
			}
		}
	}
	
	@Override
	public void invalidate() {
		removeFromEnergyNet();
		super.invalidate();
	}
	
	@Override
	public void onChunkUnload() {
		removeFromEnergyNet();
		super.onChunkUnload();
	}
	
	void removeFromEnergyNet() {
		if (addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			addedToEnergyNet = false;
		}
	}
	
	BlockNuclearControlMain getBlock() {
		return (BlockNuclearControlMain)getBlockType();
	}

	void updateBlock() {
		BlockNuclearControlMain block = getBlock();
		block.update(worldObj, xCoord, yCoord, zCoord);
		block.updateBeam(worldObj, xCoord, yCoord, zCoord);
	}

	public boolean isActive() {
		boolean redstone =  receivingRedstoneSignal();
		return energy > 0 && (redstone);
	}
	
	boolean receivingRedstoneSignal() {
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	public static void explodeMachineAt(World world, int x, int y, int z) {
		world.setBlock(x, y, z, Blocks.air, 0, 0x3);
		world.newExplosion(null, x + 0.5, y + 0.5, z + 0.5, 0.3F, false, true);
	}
}
