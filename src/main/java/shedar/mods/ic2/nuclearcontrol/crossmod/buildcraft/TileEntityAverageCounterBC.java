package shedar.mods.ic2.nuclearcontrol.crossmod.buildcraft;

import ic2.api.Direction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;

public class TileEntityAverageCounterBC extends TileEntityAverageCounter implements IPowerReceptor, IPowerEmitter, IPipeConnection
{
    private static final int MAX_SEND = 100;

    protected PowerHandler powerHandler;

    public TileEntityAverageCounterBC()
    {
        super();
        powerHandler = new PowerHandler(this, PowerHandler.Type.MACHINE);
        powerHandler.configurePowerPerdition(1, 100);
    }
    
    @Override
    public void initData() {
        super.initData();
        if (!worldObj.isRemote) {
            powerHandler.configure(1, MAX_SEND, 1, 1000);
        }
    }

    @Override
    public void updateEntity() 
    {
        super.updateEntity();
        if (worldObj.isRemote)
            return;
        Direction[] directions = Direction.values();
        for (Direction apiDirection : directions) 
        {
            ForgeDirection direction = apiDirection.toForgeDirection();
            if(direction.ordinal() == getFacing())
            {
                continue;
            }
            int x = direction.offsetX + xCoord;
            int y = direction.offsetY + yCoord;
            int z = direction.offsetZ + zCoord;
            TileEntity tile = worldObj.getTileEntity(x, y, z);
            if (tile!=null && tile instanceof IPowerReceptor && ((IPowerReceptor)tile).getPowerReceiver(direction.getOpposite())!=null) 
            {
                PowerReceiver receptor = ((IPowerReceptor) tile).getPowerReceiver(direction.getOpposite());
                if(powerHandler.getEnergyStored() >= receptor.getMinEnergyReceived() && MAX_SEND >= receptor.getMinEnergyReceived())
                {
                    double toSend = Math.min(powerHandler.getEnergyStored(), receptor.getMaxEnergyReceived());
                    double needed = receptor.receiveEnergy(PowerHandler.Type.MACHINE, toSend, direction.getOpposite());
                    powerHandler.useEnergy(1, needed, true);
                    data[index] += needed;
                }
            }

        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        powerHandler.writeToNBT(nbttagcompound);
    }    

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        powerHandler.readFromNBT(nbttagcompound);
    }

    @Override
    public boolean canEmitPowerFrom(ForgeDirection side)
    {
        return side.ordinal() != getFacing();
    }

    @Override
    public PowerReceiver getPowerReceiver(ForgeDirection side)
    {
        return powerHandler.getPowerReceiver();
    }

    @Override
    public void doWork(PowerHandler workProvider)
    {
        setPowerType(TileEntityAverageCounter.POWER_TYPE_MJ);
    }

    @Override
    public World getWorld()
    {
        return worldObj;
    }

    @Override
    public ConnectOverride overridePipeConnection(PipeType type, ForgeDirection with)
    {
        if (type == PipeType.POWER)
            return ConnectOverride.DEFAULT;
        return ConnectOverride.DISCONNECT;
    }
}
