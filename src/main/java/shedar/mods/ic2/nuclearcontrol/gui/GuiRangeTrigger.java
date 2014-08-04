package shedar.mods.ic2.nuclearcontrol.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.containers.ContainerRangeTrigger;
import shedar.mods.ic2.nuclearcontrol.gui.controls.CompactButton;
import shedar.mods.ic2.nuclearcontrol.gui.controls.GuiRangeTriggerInvertRedstone;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRangeTrigger extends GuiContainer{
    private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIRangeTrigger.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private String name;
    private ContainerRangeTrigger container;
    private ItemStack prevCard;

    public GuiRangeTrigger(Container container){
        super(container);
        ySize = 190;
        this.container = (ContainerRangeTrigger)container; 
        name = StatCollector.translateToLocal("tile.blockRangeTrigger.name");
    }
    
    @SuppressWarnings("unchecked")
    private void initControls(){
        ItemStack card = container.getSlot(TileEntityRangeTrigger.SLOT_CARD).getStack();
        if(card!=null  && card.equals(prevCard))
            return;
        buttonList.clear();
        prevCard = card;
        // ten digits, up to 10 billions
        for(int i=0; i<10; i++){
            buttonList.add(new CompactButton(i*10, guiLeft + 30 + i*12 + (i+2)/3*6, guiTop + 20, 12, 12, "-"));
            buttonList.add(new CompactButton(i*10+1, guiLeft + 30 + i*12 + (i+2)/3*6, guiTop + 42, 12, 12, "+"));
        }
        for(int i=0; i<10; i++){
            buttonList.add(new CompactButton(100+i*10, guiLeft + 30 + i*12 + (i+2)/3*6, guiTop + 57, 12, 12, "-"));
            buttonList.add(new CompactButton(100+i*10+1, guiLeft + 30 + i*12 + (i+2)/3*6, guiTop + 79, 12, 12, "+"));
        }
        buttonList.add(new GuiRangeTriggerInvertRedstone(10, guiLeft + 8, guiTop + 62, container.trigger));
    }
    
    @Override
    public void initGui() {
        super.initGui();
        initControls();
    };

    private void renderValue(double value, int x, int y){
        x+=114;
        for( int i=0; i<10; i++){
            byte digit = (byte)(value % 10);
            String str = Byte.toString(digit);
            fontRendererObj.drawString(str, x - 12*i - fontRendererObj.getCharWidth(str.charAt(0))/2 + (9-i+2)/3*6, y , 0x404040);
            value /= 10;
        }
        
    }

    @Override 
    protected void actionPerformed(GuiButton button){
        int id = button.id;
        boolean isPlus = id % 2 == 1;
        id /= 10;
        int power = 9 - (id % 10);
        id /= 10;
        boolean isEnd = id % 2 == 1;
        double initValue = isEnd ? container.trigger.levelEnd:container.trigger.levelStart;
        double newValue = initValue;
        double delta = (long)Math.pow(10, power);
        double digit = (initValue / delta) % 10;
        if(isPlus && digit<9){
            newValue += delta;
        }else if(!isPlus && digit > 0){
            newValue -= delta;
        }
        if(newValue != initValue){
            TileEntityRangeTrigger trigger = container.trigger;
            NuclearNetworkHelper.setRangeTrigger(trigger.xCoord, trigger.yCoord, trigger.zCoord, newValue, isEnd);
            if(isEnd)
                trigger.setLevelEnd(newValue);
            else
                trigger.setLevelStart(newValue);
        }
    };
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        fontRendererObj.drawString(name, (xSize - fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        
        renderValue(container.trigger.levelStart, 30, 33);
        renderValue(container.trigger.levelEnd, 30, 70);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }
    
}
