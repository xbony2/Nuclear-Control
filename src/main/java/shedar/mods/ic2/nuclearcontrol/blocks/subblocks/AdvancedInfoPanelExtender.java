package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.IScreenPart;
import shedar.mods.ic2.nuclearcontrol.panel.Screen;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanelExtender;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class AdvancedInfoPanelExtender extends InfoPanel
{
    private static final int DAMAGE = Damages.DAMAGE_ADVANCED_EXTENDER;
    private static final float[] BOUNDS = {0, 0, 0, 1, 1, 1};
    
    private static final byte[][] mapping = {
        {I_EXTENDER_ADV_SIDE, I_COLOR_DEFAULT, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE},
        {I_COLOR_DEFAULT, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE},
        {I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_COLOR_DEFAULT, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE},
        {I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_COLOR_DEFAULT, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE},
        {I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_COLOR_DEFAULT},
        {I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_EXTENDER_ADV_SIDE, I_COLOR_DEFAULT, I_EXTENDER_ADV_SIDE}
    };

    public AdvancedInfoPanelExtender(){
        super(DAMAGE, "tile.blockAdvancedInfoPanelExtender");
    }

    @Override
    public TileEntity getTileEntity(){
        return new TileEntityAdvancedInfoPanelExtender();
    }

    @Override
    public boolean isSolidBlockRequired(){
        return false;
    }

    @Override
    public boolean hasGui(){
        return false;
    }

    @Override
    public float[] getBlockBounds(TileEntity tileEntity){
        if(tileEntity == null)
            return BOUNDS;
        float[] bounds = BOUNDS.clone();
        if(tileEntity!=null)
        {
            Screen screen = ((IScreenPart)tileEntity).getScreen();
            if(screen!=null && screen.getCore(tileEntity.getWorldObj()) instanceof TileEntityAdvancedInfoPanel)
            {
                TileEntityAdvancedInfoPanel core = (TileEntityAdvancedInfoPanel)screen.getCore(tileEntity.getWorldObj());
                if(core!=null)
                {
                    int thickness = core.thickness;
                    if(thickness!=16)
                    {
                        bounds[4] = Math.max(thickness,1)/16F;
                    }
                    else
                    {
                        bounds[4] = 0.98F;
                    }
                }
            }        
        }
        return bounds;
    }

    @Override
    public Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player)
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player)
    {
        return null;
    }
    
    @Override
    protected byte[][] getMapping()
    {
        return mapping;
    }
}
