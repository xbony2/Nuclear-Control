package shedar.mods.ic2.nuclearcontrol.gui;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.network.message.PacketServerUpdate;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class GuiRemoteMonitor extends GuiContainer{

    public static final int REMOTEMONITOR_GUI = 17;
    private InventoryItem inv;
    private EntityPlayer e;
    public TileEntityInfoPanel panel;

    public GuiRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem inventoryItem, EntityPlayer player, TileEntityInfoPanel panel, World world){
        super(new ContainerRemoteMonitor(inv, stack, inventoryItem, panel, world));
        this.inv = inventoryItem;
        this.e = player;
        this.panel = panel;
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
        boolean anyCardFound = false;
        InventoryItem ivz = new InventoryItem(e.getCurrentEquippedItem());
       // NCLog.fatal("GUI RECEIVE: " + ItemStackUtils.getTagCompound(ivz.getStackInSlot(0)).getInteger("energyL"));
        if (ivz.getStackInSlot(0) != null) {
             ivz.markDirty();

            ItemStack card = ivz.getStackInSlot(0);
            ChannelHandler.network.sendToServer(new PacketServerUpdate(card));
            //this.processCard(card, 10, 0, panel);

            if (card == null || !(card.getItem() instanceof IPanelDataSource)) {
                drawCardStuff(anyCardFound, joinedData);
            }
            int displaySettings = panel.getDisplaySettingsByCard(card);
            if (displaySettings == 0) {
                drawCardStuff(anyCardFound, joinedData);
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
                drawCardStuff(anyCardFound, joinedData);
            }
            joinedData.addAll(data);
            anyCardFound = true;
            drawCardStuff(anyCardFound, joinedData);
        } else {
            inv.markDirty();
            anyCardFound = false;
        }
    }
    private void drawCardStuff(Boolean anyCardFound, List<PanelString> joinedData){
       // NCLog.error("wat?");
            if (!anyCardFound) {
                NCLog.fatal("HERE?");
                return;
            }

            //NCLog.fatal("HERE");
            //GL11.glDisable(GL11.GL_LIGHTING);

            int row = 0;
            for (PanelString panelString : joinedData) {
                if (panelString.textLeft != null) {
                    //NCLog.fatal("HERE1");
                    fontRendererObj.drawString(panelString.textLeft, (int) (this.guiTop + (row * 0.5)), this.ySize + 30, 0x06aee4);
                }
                if (panelString.textCenter != null) {
                    //NCLog.fatal("HERE2");
                    fontRendererObj.drawString(panelString.textCenter,this.xSize, this.ySize + 30, 0x06aee4);
                }
                if (panelString.textRight != null) {
                    //NCLog.fatal("HERE3");
                    this.fontRendererObj.drawString(panelString.textRight, this.xSize, this.ySize + 30, 0x06aee4);
                }
                row++;
            }

        }


   /* public void processCard(ItemStack card, int upgradeCountRange, int slot, TileEntity panel) {
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
                CardState state = ((IPanelDataSource) item).update(panel, cardHelper, range);
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
            }
            //cardHelper.commit(this);
        }

    }*/
}


