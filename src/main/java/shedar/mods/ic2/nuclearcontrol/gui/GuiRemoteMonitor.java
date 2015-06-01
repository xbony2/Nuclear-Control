package shedar.mods.ic2.nuclearcontrol.gui;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

import java.util.*;

public class GuiRemoteMonitor extends GuiContainer{

    public static final int REMOTEMONITOR_GUI = 17;
    private InventoryItem inv;

    public GuiRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem inventoryItem){
        super(new ContainerRemoteMonitor(inv, stack, inventoryItem));
        this.inv = inventoryItem;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(new ResourceLocation("nuclearcontrol", "textures/gui/GUIRemoteMonitor.png"));
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, 204, ySize);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){

        TileEntityInfoPanel panel = new TileEntityInfoPanel();
        if(inv.getStackInSlot(0) != null) {
            List<ItemStack> cards = new ArrayList<ItemStack>();
            cards.add(inv.getStackInSlot(0));
            boolean anyCardFound = false;
            List<PanelString> joinedData = new LinkedList<PanelString>();
            for (ItemStack card : cards) {
                if (card == null || !(card.getItem() instanceof IPanelDataSource)) {
                    continue;
                }
                int displaySettings = panel.getDisplaySettingsByCard(card);
                if (displaySettings == 0) {
                    continue;
                }
                CardWrapperImpl helper = new CardWrapperImpl(card, -1);
                CardState state = helper.getState();
                List<PanelString> data;
                if (state != CardState.OK && state != CardState.CUSTOM_ERROR) {
                    data = StringUtils.getStateMessage(state);
                } else {
                    data = panel.getCardData(displaySettings, card, helper);
                }
                if (data == null) {
                    continue;
                }
                joinedData.addAll(data);
                anyCardFound = true;
            }
            if (!anyCardFound) {
                NCLog.fatal("HERE?");
                return;
            }

            //MIND THE COPYPASTA...
            int maxWidth = 1;
            float displayWidth = 1 - 2F / 16;
            float displayHeight = 1 - 2F / 16;
            for (PanelString panelString : joinedData) {
                String currentString = implodeArray(new String[] {panelString.textLeft, panelString.textCenter, panelString.textRight }, " ");
                maxWidth = Math.max(fontRendererObj.getStringWidth(currentString), maxWidth);
            }
            maxWidth += 4;

            int lineHeight = fontRendererObj.FONT_HEIGHT + 2;
            int requiredHeight = lineHeight * joinedData.size();
            float scaleX = displayWidth / maxWidth;
            float scaleY = displayHeight / requiredHeight;
            float scale = Math.min(scaleX, scaleY);
            //GL11.glScalef(scale, -scale, scale);
            //GL11.glDepthMask(false);

            int offsetX;
            int offsetY;

            int realHeight = (int) Math.floor(displayHeight / scale);
            int realWidth = (int) Math.floor(displayWidth / scale);

            if (scaleX < scaleY) {
                offsetX = 2;
                offsetY = (realHeight - requiredHeight) / 2;
            } else {
                offsetX = (realWidth - maxWidth) / 2 + 2;
                offsetY = 0;
            }
            //NCLog.fatal("HERE");
            //GL11.glDisable(GL11.GL_LIGHTING);

            int row = 0;
            for (PanelString panelString : joinedData) {
                if (panelString.textLeft != null) {
                    //NCLog.fatal("HERE1");
                    fontRendererObj.drawString(panelString.textLeft,( offsetX - realWidth / 2) + 53,( 1 + offsetY - realHeight / 2 + row * lineHeight) + 30, 0x06aee4);
                }
                if (panelString.textCenter != null) {
                    //NCLog.fatal("HERE2");
                    fontRendererObj.drawString(panelString.textCenter, -fontRendererObj.getStringWidth(panelString.textCenter) / 2, offsetY - realHeight / 2 + row * lineHeight, 0x06aee4);
                }
                if (panelString.textRight != null) {
                    //NCLog.fatal("HERE3");
                    this.fontRendererObj.drawString(panelString.textRight, (offsetX - realWidth / 2) + 120, (1 + offsetY - realHeight / 2 + row * lineHeight) + 20, 0x06aee4);
                }
                row++;
            }

            //GL11.glEnable(GL11.GL_LIGHTING);
        }
        //fontRendererObj.drawString("BHATODKK", 8, ySize - 96 + 2, 4210752);
    }
    private static String implodeArray(String[] inputArray, String glueString) {
        String output = "";
        if (inputArray.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i] == null || inputArray[i].isEmpty())
                    continue;
                sb.append(glueString);
                sb.append(inputArray[i]);
            }
            output = sb.toString();
            if (output.length() > 1)
                output = output.substring(1);
        }
        return output;
    }
}
    class GuiRemoteMonitorHelper {
        private final Map<Integer, List<PanelString>> cardData = new HashMap<Integer, List<PanelString>>();
        private boolean showLabels = true;
        private boolean prevShowLabels;

        public List<PanelString> getCardData(int settings, ItemStack cardStack, ICardWrapper helper) {
            IPanelDataSource card = (IPanelDataSource) cardStack.getItem();
            int slot = 0;
            List<PanelString> data = cardData.get(slot);
            if (data == null) {
                data = card.getStringData(settings, helper, getShowLabels());
                String title = helper.getTitle();
                if (data != null && title != null && !title.isEmpty()) {
                    PanelString titleString = new PanelString();
                    titleString.textCenter = title;
                    data.add(0, titleString);
                }
                cardData.put(slot, data);
            }
            return data;
        }
        public void setShowLabels(boolean p) {
            showLabels = p;
            if (prevShowLabels != p) {
                //IC2.network.get().updateTileEntityField(this, "showLabels");
            }
            prevShowLabels = showLabels;
        }

        public boolean getShowLabels() {
            return showLabels;
        }
    }

