package shedar.mods.ic2.nuclearcontrol.gui;

import java.lang.reflect.Method;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.containers.ContainerEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnergyCounter extends GuiContainer {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIEnergyCounter.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
			TEXTURE_FILE);

	private String name;
	private ContainerEnergyCounter container;

	public GuiEnergyCounter(Container container) {
		super(container);
		this.container = (ContainerEnergyCounter) container;
		name = StatCollector.translateToLocal("tile.blockEnergyCounter.name");
	}

	@SuppressWarnings("unchecked")
	private void initControls() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, guiLeft + 35, guiTop + 42, 127, 20,
				StatCollector.translateToLocal("msg.nc.Reset")));
	}

	@Override
	public void initGui() {
		super.initGui();
		initControls();
	};

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj
				.drawString(name,
						(xSize - fontRendererObj.getStringWidth(name)) / 2, 6,
						0x404040);
		fontRendererObj.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				(ySize - 96) + 2, 0x404040);
		String value = StringUtils.getFormatted("",
				container.energyCounter.counter, false);
		fontRendererObj.drawString(value,
				(xSize - fontRendererObj.getStringWidth(value)) / 2, 22,
				0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE_LOCATION);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
		super.mouseMovedOrUp(mouseX, mouseY, which);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		initControls();
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.id == 0) {
			NetworkHelper nh = new NetworkHelper();
			try {
				Method m1 = nh.getClass().getDeclaredMethod("initiateClientTileEntityEvent");
				m1.setAccessible(true);
				m1.invoke(container.energyCounter, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// NetworkHelper.initiateClientTileEntityEvent(container.energyCounter, 0);
		}
	}
}
