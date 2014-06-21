package shedar.mods.ic2.nuclearcontrol.utils;

import shedar.mods.ic2.nuclearcontrol.IRedstoneConsumer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RedstoneHelper
{

    private static boolean isPoweredWire(World world, int x, int y, int z)
    {
        return world.getBlockId(x, y, z) == Block.redstoneWire.blockID &&
                Block.blocksList[Block.redstoneWire.blockID].isProvidingStrongPower(world, x, y, z, 1)>0;
    }
    
    public static void checkPowered(World world, TileEntity tileentity)
    {
        if(world != null && tileentity!=null && tileentity instanceof IRedstoneConsumer)
        {
            boolean powered = world.isBlockIndirectlyGettingPowered(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord) ||
                    isPoweredWire(world, tileentity.xCoord+1, tileentity.yCoord, tileentity.zCoord) ||
                    isPoweredWire(world, tileentity.xCoord-1, tileentity.yCoord, tileentity.zCoord) ||
                    isPoweredWire(world, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord+1) ||
                    isPoweredWire(world, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord-1);
            ((IRedstoneConsumer)tileentity).setPowered(powered);        
        }
    }

}
