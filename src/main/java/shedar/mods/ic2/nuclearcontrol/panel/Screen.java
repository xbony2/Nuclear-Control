package shedar.mods.ic2.nuclearcontrol.panel;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IScreenPart;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;

public class Screen
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    private int coreX;
    private int coreY;
    private int coreZ;
    private boolean powered = false;

    public TileEntityInfoPanel getCore(IBlockAccess world)
    {
        TileEntity tileEntity = world.getTileEntity(coreX, coreY, coreZ);
        if(tileEntity == null || !(tileEntity instanceof TileEntityInfoPanel))
            return null;
        return (TileEntityInfoPanel)tileEntity;
    }
    
    public void setCore(TileEntityInfoPanel core)
    {
        coreX = core.xCoord;
        coreY = core.yCoord;
        coreZ = core.zCoord;
        powered = core.getPowered();
    }
    
    public boolean isBlockNearby(TileEntity tileEntity)
    {
        int x = tileEntity.xCoord;
        int y = tileEntity.yCoord;
        int z = tileEntity.zCoord;
        return  (x == minX-1 && y>=minY && y<=maxY && z>=minZ && z<=maxZ) ||
                (x == maxX+1 && y>=minY && y<=maxY && z>=minZ && z<=maxZ) ||
                (x >= minX && x<=maxX && y==minY-1 && z>=minZ && z<=maxZ) ||
                (x >= minX && x<=maxX && y==maxY+1 && z>=minZ && z<=maxZ) ||
                (x >= minX && x<=maxX && y>=minY && y<=maxY && z==minZ-1) ||
                (x >= minX && x<=maxX && y>=minY && y<=maxY && z==maxZ+1);
    }
    
    public boolean isBlockPartOf(TileEntity tileEntity)
    {
        int x = tileEntity.xCoord;
        int y = tileEntity.yCoord;
        int z = tileEntity.zCoord;
        return  x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }
    
    public void init(boolean force, World world)
    {
        for(int x = minX; x<=maxX; x++)
        {
            for(int y = minY; y<=maxY; y++)
            {
                for(int z = minZ; z<=maxZ; z++)
                {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if(tileEntity == null || !(tileEntity instanceof IScreenPart))
                        continue;
                    ((IScreenPart)tileEntity).setScreen(this); 
                    if(powered || force)
                    {
                        world.markBlockForUpdate(x, y, z);
                        world.updateAllLightTypes(x, y, z);
                    }
                }
            }
        }
    }
    
    
    public void destroy(boolean force, World world)
    {
        for(int x = minX; x<=maxX; x++)
        {
            for(int y = minY; y<=maxY; y++)
            {
                for(int z = minZ; z<=maxZ; z++)
                {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if(tileEntity == null || !(tileEntity instanceof IScreenPart))
                        continue;
                    IScreenPart part = (IScreenPart)tileEntity;
                    Screen targetScreen = part.getScreen(); 
                    if(targetScreen!=null && targetScreen.equals(this))
                    {
                        part.setScreen(null);
                        part.updateData();
                    }
                    if(powered || force)
                    {
                        world.markBlockForUpdate(x, y, z);
                        world.updateAllLightTypes(x, y, z);
                    }
                }
            }
        }
    }
    
    public void turnPower(boolean on, World world)
    {
        if(powered!=on)
        {
            powered = on;
            for(int x = minX; x<=maxX; x++)
            {
                for(int y = minY; y<=maxY; y++)
                {
                    for(int z = minZ; z<=maxZ; z++)
                    {
                        world.markBlockForUpdate(x, y, z);
                        world.updateAllLightTypes(x, y, z);
                    }
                }
            }
        }
    }
    
    public void markUpdate(World world)
    {
        for(int x = minX; x<=maxX; x++)
        {
            for(int y = minY; y<=maxY; y++)
            {
                for(int z = minZ; z<=maxZ; z++)
                {
                    world.markBlockForUpdate(x, y, z);
                }
            }
        }
    }
    
    public NBTTagCompound toTag()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("minX", minX);
        tag.setInteger("minY", minY);
        tag.setInteger("minZ", minZ);

        tag.setInteger("maxX", maxX);
        tag.setInteger("maxY", maxY);
        tag.setInteger("maxZ", maxZ);
        
        return tag;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + coreX;
        result = prime * result + coreY;
        result = prime * result + coreZ;
        result = prime * result + maxX;
        result = prime * result + maxY;
        result = prime * result + maxZ;
        result = prime * result + minX;
        result = prime * result + minY;
        result = prime * result + minZ;
        return result;
    }
    
    public boolean isCore(int x, int y, int z)
    {
        return x == coreX && y == coreY && z == coreZ;
    }
    
    public int getDx()
    {
        return maxX-minX;
    }

    public int getDy()
    {
        return maxY-minY;
    }

    public int getDz()
    {
        return maxZ-minZ;
    }
    
    public int getHeight(TileEntityInfoPanel core)
    {
        if(core == null)
            return 0;
        int rotation = core.getRotation();
        switch (core.getFacing())
        {
        case 0:
        case 1:
            if( rotation == 0 || rotation == 3)
                return getDz()+1;
            else
                return getDx()+1;
        case 2:
        case 3:
            if( rotation == 0 || rotation == 3)
                return getDy()+1;
            else
                return getDx()+1;
        case 4:
        case 5:
            if( rotation == 0 || rotation == 3)
                return getDy()+1;
            else
                return getDz()+1;
        }
        return 1;
    }
    
    public int getWidth(TileEntityInfoPanel core)
    {
        if(core == null)
            return 0;
        int rotation = core.getRotation();
        switch (core.getFacing())
        {
        case 0:
        case 1:
            if( rotation == 0 || rotation == 3)
                return getDx()+1;
            else
                return getDz()+1;
        case 2:
        case 3:
            if( rotation == 0 || rotation == 3)
                return getDx()+1;
            else
                return getDy()+1;
        case 4:
        case 5:
            if( rotation == 0 || rotation == 3)
                return getDz()+1;
            else
                return getDy()+1;
        }
        return 1;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Screen other = (Screen) obj;
        if (coreX != other.coreX)
            return false;
        if (coreY != other.coreY)
            return false;
        if (coreZ != other.coreZ)
            return false;
        if (maxX != other.maxX)
            return false;
        if (maxY != other.maxY)
            return false;
        if (maxZ != other.maxZ)
            return false;
        if (minX != other.minX)
            return false;
        if (minY != other.minY)
            return false;
        if (minZ != other.minZ)
            return false;
        return true;
    }
}
