package shedar.mods.ic2.nuclearcontrol.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.IScreenPart;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanelExtender;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanelExtender;
import cpw.mods.fml.common.FMLCommonHandler;

public class ScreenManager
{
    
    private final Map<Integer,List<Screen>> screens; 
    private final Map<Integer,List<TileEntityInfoPanel>> unusedPanels;
    
    public ScreenManager()
    {
        screens = new HashMap<Integer, List<Screen>>();
        unusedPanels = new HashMap<Integer, List<TileEntityInfoPanel>>();
    }
    
    private int getWorldKey(World world)
    {
        if(world == null)
            return -10;
        if(world.getWorldInfo() == null)
            return -10;
        if(world.getWorldInfo().getVanillaDimension()==0 && world.provider!=null)
        {
            return world.provider.dimensionId;
        }
        else
        {
            return world.getWorldInfo().getVanillaDimension();
        }
    }
    
    private boolean isValidExtender(World world, int x, int y, int z, int facing, boolean advanced)
    {
        if(world.getBlockId(x, y, z) != IC2NuclearControl.instance.blockNuclearControlMain.blockID)
            return false;
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(!(tileEntity instanceof TileEntityInfoPanelExtender))
            return false;
        if(advanced ^ (tileEntity instanceof TileEntityAdvancedInfoPanelExtender))
            return false;
        if(((TileEntityInfoPanelExtender)tileEntity).facing != facing)
            return false;
        if(((IScreenPart)tileEntity).getScreen() != null)
            return false;
        return true;
    }
    
    private void updateScreenBound(Screen screen, int dx, int dy, int dz, World world, boolean advanced)
    {
        if(dx == 0 && dy == 0 && dz == 0)
            return;
        boolean isMin = dx + dy + dz < 0;
        int dir = isMin?1:-1;
        int steps = 0;
        while(steps<20)
        {
            int x, rx;
            int y, ry;
            int z, rz;
            if(isMin)
            {
                x = screen.minX + dx;
                y = screen.minY + dy;
                z = screen.minZ + dz;
            }
            else
            {
                x = screen.maxX + dx;
                y = screen.maxY + dy;
                z = screen.maxZ + dz;
            }
            rx = dx!=0?0:(screen.maxX-screen.minX);
            ry = dy!=0?0:(screen.maxY-screen.minY);
            rz = dz!=0?0:(screen.maxZ-screen.minZ);
            boolean allOk = true;
            for(int interX=0;interX<=rx && allOk;interX++)
            {
                for(int interY=0;interY<=ry && allOk;interY++)
                {
                    for(int interZ=0;interZ<=rz && allOk;interZ++)
                    {
                        TileEntityInfoPanel core = screen.getCore(world); 
                        allOk = core!=null && isValidExtender(world, x+dir*interX, y+dir*interY, z+dir*interZ, core.facing, advanced);
                    }
                }
            }
            if(!allOk)
                break;
            if(isMin)
            {
                screen.minX+=dx;
                screen.minY+=dy;
                screen.minZ+=dz;
            }
            else
            {
                screen.maxX+=dx;
                screen.maxY+=dy;
                screen.maxZ+=dz;
            }
            steps++;
        }
    }
    
    public Screen loadScreen(TileEntityInfoPanel panel)
    {
        if(panel.screenData!=null)
        {
            NBTTagCompound tag = panel.screenData;
            Screen screen = new Screen();

            screen.minX = tag.getInteger("minX");
            screen.minY = tag.getInteger("minY");
            screen.minZ = tag.getInteger("minZ");

            screen.maxX = tag.getInteger("maxX");
            screen.maxY = tag.getInteger("maxY");
            screen.maxZ = tag.getInteger("maxZ");

            screen.setCore(panel);

            if(FMLCommonHandler.instance().getEffectiveSide().isServer())
            {
                Integer key = getWorldKey(panel.worldObj);
                if(!screens.containsKey(key))
                    screens.put(key, new ArrayList<Screen>());
                if(!unusedPanels.containsKey(key))
                    unusedPanels.put(key, new ArrayList<TileEntityInfoPanel>());
                if(!screens.get(key).contains(screen))
                {
                    screens.get(key).add(screen);
                }
            }
            return screen;
        }
        return null;
    }
    
    public Screen loadScreen(World world, int coreX, int coreY, int coreZ)
    {
        TileEntity core = world.getBlockTileEntity(coreX, coreY, coreZ);
        if(core != null && core instanceof TileEntityInfoPanel)
            return loadScreen((TileEntityInfoPanel)core);
        return null;
    }
    
    private Screen tryBuildFromPanel(TileEntityInfoPanel panel)
    {
        boolean advanced = panel instanceof TileEntityAdvancedInfoPanel;
        Screen screen = new Screen();
        screen.maxX = screen.minX = panel.xCoord; 
        screen.maxY = screen.minY = panel.yCoord; 
        screen.maxZ = screen.minZ = panel.zCoord;
        screen.setCore(panel);
        int dx = Facing.offsetsXForSide[panel.facing]!=0?0:-1;
        int dy = Facing.offsetsYForSide[panel.facing]!=0?0:-1;
        int dz = Facing.offsetsZForSide[panel.facing]!=0?0:-1;
        updateScreenBound(screen, dx, 0, 0, panel.worldObj, advanced);
        updateScreenBound(screen, -dx, 0, 0, panel.worldObj, advanced);
        updateScreenBound(screen, 0, dy, 0, panel.worldObj, advanced);
        updateScreenBound(screen, 0, -dy, 0, panel.worldObj, advanced);
        updateScreenBound(screen, 0, 0, dz, panel.worldObj, advanced);
        updateScreenBound(screen, 0, 0, -dz, panel.worldObj, advanced);
        screen.init(false, panel.worldObj);
        panel.updateData();
        return screen;
    }
    
    private void destroyScreen(Screen screen, World world)
    {
        screens.get(getWorldKey(world)).remove(screen);
        screen.destroy(false, world);
    }
    
    public void unregisterScreenPart(TileEntity part)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;
        if(!screens.containsKey(getWorldKey(part.worldObj)))
            return;
        if(!unusedPanels.containsKey(getWorldKey(part.worldObj)))
            return;
        if(!(part instanceof IScreenPart))
            return;
        IScreenPart screenPart = (IScreenPart)part;
        Screen screen = screenPart.getScreen(); 
        if(screen==null)
        {
            if(part instanceof TileEntityInfoPanel &&
               unusedPanels.get(getWorldKey(part.worldObj)).contains(part))
                unusedPanels.get(getWorldKey(part.worldObj)).remove(part);
            return;
        }
        TileEntityInfoPanel core = screen.getCore(part.worldObj);
        destroyScreen(screen, part.worldObj);
        boolean isCoreDestroyed = part instanceof TileEntityInfoPanel;
        if(!isCoreDestroyed && core != null)
        {
            Screen newScreen = tryBuildFromPanel(core);
            if(newScreen == null)
                unusedPanels.get(getWorldKey(core.worldObj)).add(core);
            else
                screens.get(getWorldKey(core.worldObj)).add(newScreen);
        }
        
    }
    
    public void registerInfoPanel(TileEntityInfoPanel panel)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;
        if(!screens.containsKey(getWorldKey(panel.worldObj)))
            screens.put(getWorldKey(panel.worldObj), new ArrayList<Screen>());
        if(!unusedPanels.containsKey(getWorldKey(panel.worldObj)))
            unusedPanels.put(getWorldKey(panel.worldObj), new ArrayList<TileEntityInfoPanel>());
        for (Screen screen : screens.get(getWorldKey(panel.worldObj)))
        {
            if(screen.isBlockPartOf(panel))
            {
                //occurs on chunk unloading/loading
                destroyScreen(screen, panel.worldObj);
                break;
            }
        }        
        Screen screen = tryBuildFromPanel(panel);
        if(screen!=null)
            screens.get(getWorldKey(panel.worldObj)).add(screen);
        else
            unusedPanels.get(getWorldKey(panel.worldObj)).add(panel);
    }
    
    public void registerInfoPanelExtender(TileEntityInfoPanelExtender extender)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;
        if(!screens.containsKey(getWorldKey(extender.worldObj)))
            screens.put(getWorldKey(extender.worldObj), new ArrayList<Screen>());
        if(!unusedPanels.containsKey(getWorldKey(extender.worldObj)))
            unusedPanels.put(getWorldKey(extender.worldObj), new ArrayList<TileEntityInfoPanel>());
        
        List<TileEntityInfoPanel> rebuildPanels = new ArrayList<TileEntityInfoPanel>();
        List<Screen> screensToDestroy = new ArrayList<Screen>();

        for (Screen screen : screens.get(getWorldKey(extender.worldObj)))
        {
            TileEntityInfoPanel core = screen.getCore(extender.worldObj);
            if(screen.isBlockNearby(extender) && core!=null && extender.facing == core.facing)
            {
                rebuildPanels.add(core);
                screensToDestroy.add(screen);
            }
            else if(screen.isBlockPartOf(extender))
            {
                // block is already part of the screen
                // shouldn't be registered twice
                return;
            }
        }
        for(Screen screen : screensToDestroy)
        {
            destroyScreen(screen, extender.worldObj);
        }
        for (TileEntityInfoPanel panel : unusedPanels.get(getWorldKey(extender.worldObj)))
        {
            if(((panel.xCoord == extender.xCoord && panel.yCoord  == extender.yCoord && (panel.zCoord == extender.zCoord+1 || panel.zCoord == extender.zCoord-1)) ||
                (panel.xCoord == extender.xCoord && (panel.yCoord  == extender.yCoord+1 || panel.yCoord  == extender.yCoord-1) && panel.zCoord == extender.zCoord) ||
                ((panel.xCoord == extender.xCoord+1 || panel.xCoord == extender.xCoord-1) && panel.yCoord  == extender.yCoord && panel.zCoord == extender.zCoord)) &&
                extender.facing == panel.facing
                )
            {
                rebuildPanels.add(panel);
            }
        }
        for (TileEntityInfoPanel panel : rebuildPanels)
        {
            Screen screen = tryBuildFromPanel(panel);
            if(screen!=null)
            {
                screens.get(getWorldKey(extender.worldObj)).add(screen);
                if(unusedPanels.get(getWorldKey(extender.worldObj)).contains(panel))
                    unusedPanels.get(getWorldKey(extender.worldObj)).remove(panel);
            }
            else
            {
                if(!unusedPanels.get(getWorldKey(extender.worldObj)).contains(panel))
                    unusedPanels.get(getWorldKey(extender.worldObj)).add(panel);
            }
        }
    }
    
    public void clearWorld(World world)
    {
        if(screens.containsKey(getWorldKey(world)))
        {
            screens.get(getWorldKey(world)).clear();
        }
    }

}
