package shedar.mods.ic2.nuclearcontrol.gui.controls;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInfoPanelShowLabels extends GuiButton{
    private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIInfoPanel.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private TileEntityInfoPanel panel;
    private boolean checked;

    public GuiInfoPanelShowLabels(int id, int x, int y, TileEntityInfoPanel panel){
        super(id, x, y, 0, 0, "");
        height  = 9;
        width = 18;
        this.panel = panel;
        checked = panel.getShowLabels();
    }

    @Override
    public void drawButton(Minecraft minecraft, int par2, int par3){
        if (this.visible){
            minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int delta = checked?12:21;
            drawTexturedModalRect(xPosition, yPosition+1, 176, delta, 18, 9);
        }
    }

    @Override
	public int getHoverState(boolean flag){
        return 0;
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int i, int j){
        if (super.mousePressed(minecraft, i, j)){
            checked = !checked;
            int value = checked?-1:-2;
            panel.setShowLabels(checked);
            NetworkHelper.initiateClientTileEntityEvent(panel, value);
            return true;
        }else{
            return false;
        }
    }

}
