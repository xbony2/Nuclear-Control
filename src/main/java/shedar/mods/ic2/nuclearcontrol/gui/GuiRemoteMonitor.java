package shedar.mods.ic2.nuclearcontrol.gui;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardText;
import shedar.mods.ic2.nuclearcontrol.items.ItemTimeCard;
import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.network.message.PacketServerUpdate;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class GuiRemoteMonitor extends GuiContainer{

	public static final int REMOTEMONITOR_GUI = 17;
	private InventoryItem inv;
	private EntityPlayer e;

	public GuiRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem inventoryItem, EntityPlayer player){
		super(new ContainerRemoteMonitor(inv, stack, inventoryItem));
		this.inv = inventoryItem;
		this.e = player;
	}

	@Override
	public void initGui(){
		super.initGui();
		this.xSize += 50;
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
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
		
		if (inv.getStackInSlot(0) != null && itemInv.getStackInSlot(0) != null && inv.getStackInSlot(0).getItem() instanceof IPanelDataSource) {
			IPanelDataSource card = (IPanelDataSource) inv.getStackInSlot(0).getItem();
			CardWrapperImpl helper = new CardWrapperImpl(itemInv.getStackInSlot(0), 0);
			joinedData.clear();
			ChannelHandler.network.sendToServer(new PacketServerUpdate(inv.getStackInSlot(0)));
				
			if(helper.getState() != CardState.OK)
				if(helper.getState().equals(CardState.CUSTOM_ERROR))
					if(card instanceof ItemCardText || card instanceof ItemTimeCard)
						joinedData = card.getStringData(Integer.MAX_VALUE, helper, true);
					else
						joinedData = this.getRemoteCustomMSG();
				else
					joinedData = StringUtils.getStateMessage(helper.getState());
			else
				joinedData = card.getStringData(Integer.MAX_VALUE, helper, true);
		}
		
		drawCardStuff(anyCardFound, joinedData);
	}

	private List<PanelString> getRemoteCustomMSG(){
		PanelString line = new PanelString();
		List<PanelString> result = new LinkedList<PanelString>();
		line.textCenter = LangHelper.translate("nc.msg.notValid");
		result.add(line);
		line = new PanelString();
		line.textCenter = LangHelper.translate("nc.msg.notValid2");
		result.add(line);
		line = new PanelString();
		line.textCenter = "";
		result.add(line);
		line = new PanelString();
		line.textCenter = LangHelper.translate("nc.msg.notValid3");
		result.add(line);
		return result;
	}
	
	private void drawCardStuff(Boolean anyCardFound, List<PanelString> joinedData){
		if (!anyCardFound){
			NCLog.fatal("This should never happen. If you see this report immediately to NC2 repo. Include GuiRemoteMonitorError-123 in the report!");
			return;
		}

		int row = 0;
		for (PanelString panelString : joinedData) {
			if (panelString.textLeft != null)
				fontRendererObj.drawString(panelString.textLeft, 9, (row * 10) + 20, 0x06aee4);
			
			if (panelString.textCenter != null)
				fontRendererObj.drawString(panelString.textCenter,(168 - fontRendererObj.getStringWidth(panelString.textCenter)) /2,(row * 10) + 20, 0x06aee4);
			
			if (panelString.textRight != null)
				this.fontRendererObj.drawString(panelString.textRight, 168 - fontRendererObj.getStringWidth(panelString.textRight), ((row-1) * 10) + 20, 0x06aee4);
			
			row++;
		}
	}

	public void updateScreen() {
		super.updateScreen();
		
		if(this.e.getHeldItem() == null)
			this.mc.thePlayer.closeScreen();
	}
}
