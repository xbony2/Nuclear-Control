package shedar.mods.ic2.nuclearcontrol.gui;


import ic2.core.IC2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.api.*;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.CrossRF;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.CrossTE;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.ItemCardRFEnergyLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardBase;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.network.message.PacketServerUpdate;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

import java.util.*;

public class GuiRemoteMonitor extends GuiContainer{

    public static final int REMOTEMONITOR_GUI = 17;
    private InventoryItem inv;
    private EntityPlayer e;
    private World world;
    public TileEntityInfoPanel panel;
    int tick;

    public GuiRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem inventoryItem, EntityPlayer player, TileEntityInfoPanel panel, World world){
        super(new ContainerRemoteMonitor(inv, stack, inventoryItem, panel, Minecraft.getMinecraft().theWorld));
        this.inv = inventoryItem;
        this.e = player;
        this.panel = panel;
        this.world = world;
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
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        List<PanelString> joinedData = new LinkedList<PanelString>();
        boolean anyCardFound = true;
        InventoryItem itemInv = new InventoryItem(e.getHeldItem());
            if (inv.getStackInSlot(0) != null) {
                if (inv.getStackInSlot(0).getItem() instanceof IPanelDataSource) {
                    IPanelDataSource card = (IPanelDataSource) inv.getStackInSlot(0).getItem();
                    CardWrapperImpl helper = new CardWrapperImpl(itemInv.getStackInSlot(0), -1);
                    joinedData.clear();
                    ChannelHandler.network.sendToServer(new PacketServerUpdate(inv.getStackInSlot(0)));
                    // this.processCard(inv.getStackInSlot(0),7, 0, null);
                    if(helper.getState() != CardState.OK){
                        joinedData = StringUtils.getStateMessage(helper.getState());
                    }else {
                        joinedData = card.getStringData(Integer.MAX_VALUE, helper, true);
                    }
                }
            }
        drawCardStuff(anyCardFound, joinedData);
    }
    private void drawCardStuff(Boolean anyCardFound, List<PanelString> joinedData){
       // NCLog.error("wat?");
            if (!anyCardFound) {
                NCLog.fatal("This should never happen. If you see this report immediately to NC2 repo. Include GuiRemoteMonitorError-123 in the report!");
                return;
            }

            //MIND THE COPYPASTA...
            int maxWidth = 40;
            float displayWidth = this.width;
            float displayHeight = this.height;
            for (PanelString panelString : joinedData) {
                String currentString = implodeArray(new String[] {panelString.textLeft, panelString.textCenter, panelString.textRight }, " ");
                maxWidth = Math.max(fontRendererObj.getStringWidth(currentString), maxWidth);
            }

            //NCLog.fatal("HERE");
            //GL11.glDisable(GL11.GL_LIGHTING);

            int row = 0;
            for (PanelString panelString : joinedData) {
                if (panelString.textLeft != null) {
                    //NCLog.fatal("HERE1");
                    fontRendererObj.drawString(panelString.textLeft, 9, (row * 10) + 20, 0x06aee4);
                }
                if (panelString.textCenter != null) {
                    //NCLog.fatal("HERE2");
                    fontRendererObj.drawString(panelString.textCenter,(168 - fontRendererObj.getStringWidth(panelString.textCenter)) /2,(row * 10) + 20, 0x06aee4);
                }
                if (panelString.textRight != null) {
                    //NCLog.fatal("HERE3");
                    this.fontRendererObj.drawString(panelString.textRight, 168 - fontRendererObj.getStringWidth(panelString.textRight), ((row-1) * 10) + 20, 0x06aee4);
                }
                row++;
            }

            //GL11.glEnable(GL11.GL_LIGHTING);
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

    public void processCard(ItemStack card, int upgradeCountRange, int slot, TileEntity panel) {
        if (card == null) {
            return;
        }
        Item item = card.getItem();
        if (item instanceof IPanelDataSource) {
            boolean needUpdate = true;
            if (upgradeCountRange > 7) {
                upgradeCountRange = 7;
            }
            int range = 100 * (int) Math.pow(2, upgradeCountRange);
            CardWrapperImpl cardHelper = new CardWrapperImpl(card, slot);

            if (item instanceof IRemoteSensor) {
                ChunkCoordinates target = cardHelper.getTarget();
                if (target == null) {
                    needUpdate = false;
                    cardHelper.setState(CardState.INVALID_CARD);
                } else {
                    int dx = target.posX - (int) e.posX;
                    int dy = 0;// target.posY - yCoord;
                    int dz = target.posZ - (int) e.posZ;
                    if (Math.abs(dx) > range || Math.abs(dy) > range
                            || Math.abs(dz) > range) {
                        needUpdate = false;
                        cardHelper.setState(CardState.OUT_OF_RANGE);
                    }
                }
            }
            if (needUpdate) {
                CardState state = null;
                    state = ((IPanelDataSource) item).update(world, cardHelper, range);
                cardHelper.setInt("state", state.getIndex());

            }

            //cardHelper.getUpdateSet();
            /*for (Map.Entry<String, Object> entry : cardHelper.getUpdateSet().entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Long) {
                    NCLog.fatal("LONG: "+ name + (Long) value);
                    //helper.setLong(name, (Long) value);
                } else if (value instanceof Double) {
                    NCLog.fatal("DOUBLE: "+ name + (Double) value);
                    //helper.setDouble(name, (Double) value);
                } else if (value instanceof Integer) {
                    NCLog.fatal("INT: "+ name + (Integer) value);
                    //helper.setInt(name, (Integer) value);
                } else if (value instanceof String) {
                    NCLog.fatal("String: "+ name + (String) value);
                    //helper.setString(name, (String) value);
                } else if (value instanceof Boolean) {
                    NCLog.fatal("Bool: "+ name + (Boolean) value);
                    //helper.setBoolean(name, (Boolean) value);
                } else if (value instanceof NBTTagCompound) {
                    NCLog.fatal("NBT: "+ name + (NBTTagCompound) value);
                    //helper.setTag(name, (NBTTagCompound) value);
                } else if (value == null) {
                    NCLog.fatal("Null: "+ name);
                    //helper.clearField(name);
                }
            }*/
            //cardHelper.commit(this);
        }

    }
}


