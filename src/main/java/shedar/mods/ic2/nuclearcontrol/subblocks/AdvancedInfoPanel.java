package shedar.mods.ic2.nuclearcontrol.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.IScreenPart;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.gui.GuiAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.panel.Screen;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class AdvancedInfoPanel extends InfoPanel{
    private static final int DAMAGE = Damages.DAMAGE_ADVANCED_PANEL;
    private static final float[] BOUNDS = {0, 0, 0, 1, 1, 1};

    private static final byte[][] mapping = {
        {I_PANEL_ADV_SIDE, I_COLOR_DEFAULT, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE},
        {I_COLOR_DEFAULT, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE},
        {I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_COLOR_DEFAULT, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE},
        {I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_COLOR_DEFAULT, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE},
        {I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_COLOR_DEFAULT},
        {I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_PANEL_ADV_SIDE, I_COLOR_DEFAULT, I_PANEL_ADV_SIDE}
    };

    public AdvancedInfoPanel(){
        super(DAMAGE, "tile.blockAdvancedInfoPanel");
    }

    @Override
    public TileEntity getTileEntity(){
        return new TileEntityAdvancedInfoPanel();
    }

    @Override
    public boolean isSolidBlockRequired(){
        return false;
    }

    @Override
    public boolean hasGui(){
        return true;
    }

    @Override
    public float[] getBlockBounds(TileEntity tileEntity){
        if(tileEntity == null)
            return BOUNDS;
        float[] bounds = BOUNDS.clone();
        if(tileEntity!=null)
        {
            Screen screen = ((IScreenPart)tileEntity).getScreen();
            if(screen!=null)
            {
                TileEntityAdvancedInfoPanel core = (TileEntityAdvancedInfoPanel)screen.getCore(tileEntity.worldObj);
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
    public Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player){
        return new ContainerAdvancedInfoPanel(player, (TileEntityAdvancedInfoPanel)tileEntity);
    }

    @Override
    public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player){
        ContainerAdvancedInfoPanel containerAdvancedPanel = new ContainerAdvancedInfoPanel(player, (TileEntityAdvancedInfoPanel)tileEntity);
        return new GuiAdvancedInfoPanel(containerAdvancedPanel);
    }
    /*
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
    }*/

    @Override
    protected byte[][] getMapping(){
        return mapping;
    }
}
