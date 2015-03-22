package shedar.mods.ic2.nuclearcontrol.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

import cpw.mods.fml.client.FMLClientHandler;

public class GuiScreenColor extends GuiScreen {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIColors.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
			TEXTURE_FILE);

	private GuiInfoPanel parentGui;

	protected int xSize = 226;
	protected int ySize = 94;
	protected int guiLeft;
	protected int guiTop;
	private int colorText;
	private int colorBack;
	private TileEntityInfoPanel panel;

	public GuiScreenColor(GuiInfoPanel parentGui, TileEntityInfoPanel panel) {
		this.parentGui = parentGui;
		this.panel = panel;
		colorBack = panel.getColorBackground();
		colorText = panel.getColorText();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void mouseClicked(int x, int y, int par3) {
		super.mouseClicked(x, y, par3);
		x -= guiLeft;
		y -= guiTop;
		if (x >= 7 && x <= 212) {
			int index = (x - 7) / 14;
			int shift = (x - 7) % 14;
			if (y >= 32 && y <= 41 && shift <= 9) {// back
				colorBack = index;
				NuclearNetworkHelper.setScreenColor(panel.xCoord, panel.yCoord,
						panel.zCoord, colorBack, colorText);
				panel.setColorBackground(colorBack);
			} else if (y >= 63 && y <= 72 && shift <= 9) {// /text
				colorText = index;
				NuclearNetworkHelper.setScreenColor(panel.xCoord, panel.yCoord,
						panel.zCoord, colorBack, colorText);
				panel.setColorText(colorText);
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE_LOCATION);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		drawTexturedModalRect(left + 5 + colorBack * 14, top + 30, 226, 0, 14, 14);
		drawTexturedModalRect(left + 5 + colorText * 14, top + 61, 226, 0, 14, 14);
		fontRendererObj.drawString(LangHelper.translate("msg.nc.ScreenColor"), guiLeft + 8, guiTop + 20, 0x404040);
		fontRendererObj.drawString(LangHelper.translate("msg.nc.TextColor"), guiLeft + 8, guiTop + 52, 0x404040);

		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			parentGui.isColored = !panel.colored;
			FMLClientHandler.instance().getClient().displayGuiScreen(parentGui);
		} else {
			super.keyTyped(par1, par2);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		buttonList.clear();
	}

}
