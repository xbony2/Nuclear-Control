package shedar.mods.ic2.nuclearcontrol.gui;

import java.lang.reflect.Method;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.containers.ContainerAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAverageCounter extends GuiContainer{
    private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIEnergyCounter.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private String name;
    private ContainerAverageCounter container;

    public GuiAverageCounter(Container container){
        super(container);
        this.container = (ContainerAverageCounter)container; 
        name = StatCollector.translateToLocal("tile.blockAverageCounter.name");
    }
    
    @SuppressWarnings("unchecked")
    private void initControls(){
        buttonList.clear();
        buttonList.add(new GuiButton(1, guiLeft+35, guiTop+42, 30, 20, StatCollector.translateToLocal("1")));
        buttonList.add(new GuiButton(2, guiLeft+35+30, guiTop+42, 30, 20, StatCollector.translateToLocal("3")));
        buttonList.add(new GuiButton(3, guiLeft+35+60, guiTop+42, 30, 20, StatCollector.translateToLocal("5")));
        buttonList.add(new GuiButton(4, guiLeft+35+90, guiTop+42, 30, 20, StatCollector.translateToLocal("10")));
    }
    
    @Override
    public void initGui() {
        super.initGui();
        initControls();
    };

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        fontRendererObj.drawString(name, (xSize - fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        String key = container.averageCounter.powerType == TileEntityAverageCounter.POWER_TYPE_EU?"msg.nc.InfoPanelOutput":"msg.nc.InfoPanelOutputMJ";
        String value = StringUtils.getFormatted(key, container.averageCounter.getClientAverage(), true);
        fontRendererObj.drawString(value, (xSize - fontRendererObj.getStringWidth(value)) / 2, 22, 0x404040);
        value = StringUtils.getFormatted("msg.nc.AverageCounterPeriod", container.averageCounter.period, true);
        fontRendererObj.drawString(value, (xSize - fontRendererObj.getStringWidth(value)) / 2, 32, 0x404040);
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }
    
    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which){
        super.mouseMovedOrUp(mouseX, mouseY, which);
    }

    @Override
    public void updateScreen(){
        super.updateScreen();
        initControls();
    }
    
    @Override
    protected void actionPerformed(GuiButton guiButton){
        int event = 0;
        switch(guiButton.id){
        case 1:
            event = 1; break;
        case 2:
            event = 3; break;
        case 3:
            event = 5; break;
        case 4:
            event = 10; break;
        }
        NetworkHelper nh = new NetworkHelper();
        try{
        Method m1 = nh.getClass().getDeclaredMethod("initiateClientTileEntityEvent");
        m1.setAccessible(true);
        m1.invoke(container.averageCounter, event);
        }catch(Exception e){
            e.printStackTrace();
        }
        //NetworkHelper.initiateClientTileEntityEvent(container.averageCounter, event);
    }
}
