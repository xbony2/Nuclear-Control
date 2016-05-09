package shedar.mods.ic2.nuclearcontrol.gui.controls;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHowlerAlarmListBox extends GuiButton {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIHowlerAlarm.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

	private static final int BASIC_X_OFFSET = 2;
	private static final int BASIC_Y_OFFSET = 2;
	private static final int SCROLL_WIDTH = 10;
	private static final int SCROLL_BUTTON_HEIGHT = 8;

	public int fontColor;
	public int selectedColor;
	public int selectedFontColor;
	private int scrollTop;
	private List<String> items;
	private TileEntityHowlerAlarm alarm;
	public int lineHeight;
	private int sliderHeight;
	public boolean dragging;
	private int sliderY;
	private int dragDelta;

	public GuiHowlerAlarmListBox(int id, int left, int top, int width, int height, List<String> items, TileEntityHowlerAlarm alarm) {
		super(id, left, top, width, height, "");
		this.items = items;
		this.alarm = alarm;
		fontColor = 0x404040;
		selectedColor = 0xff404040;
		selectedFontColor = 0xA0A0A0;
		scrollTop = 0;
		lineHeight = 0;
		sliderHeight = 0;
		dragging = false;
		dragDelta = 0;
	}

	private void scrollTo(int pos) {
		scrollTop = pos;
		if (scrollTop < 0)
			scrollTop = 0;
		
		int max = lineHeight * items.size() + BASIC_Y_OFFSET - height;
		
		if (max < 0)
			max = 0;
		
		if (scrollTop > max)
			scrollTop = max;
	}

	public void scrollUp() {
		scrollTop -= 8;
		
		if (scrollTop < 0)
			scrollTop = 0;
	}

	public void scrollDown() {
		scrollTop += 8;
		int max = lineHeight * items.size() + BASIC_Y_OFFSET - height;
		
		if (max < 0)
			max = 0;
		
		if (scrollTop > max)
			scrollTop = max;
	}

	@Override
	public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
		if (dragging) {
			int pos = (cursorY - yPosition - SCROLL_BUTTON_HEIGHT - dragDelta) * (lineHeight * items.size() + BASIC_Y_OFFSET - height) / 
					Math.max(height - 2 * SCROLL_BUTTON_HEIGHT - sliderHeight, 1);
			
			scrollTo(pos);
		}

		FontRenderer fontRenderer = minecraft.fontRenderer;
		String currentItem = alarm.getSoundName();
		if (lineHeight == 0) {
			lineHeight = fontRenderer.FONT_HEIGHT + 2;
			if (scrollTop == 0) {
				int rowsPerHeight = height / lineHeight;
				int currentIndex = items.indexOf(currentItem);
				if (currentIndex >= rowsPerHeight)
					scrollTop = (currentIndex + 1) * lineHeight + BASIC_Y_OFFSET - height;
			}
			float scale = height / ((float) lineHeight * items.size() + BASIC_Y_OFFSET);
			
			if (scale > 1)
				scale = 1;
			
			sliderHeight = Math.round(scale * (height - 2 * SCROLL_BUTTON_HEIGHT));
			
			if (sliderHeight < 4)
				sliderHeight = 4;
		}
		
		int rowTop = BASIC_Y_OFFSET;
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Minecraft mc = FMLClientHandler.instance().getClient();
		ScaledResolution scaler = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		GL11.glScissor(xPosition * scaler.getScaleFactor(), mc.displayHeight - (yPosition + height) * scaler.getScaleFactor(), (width - SCROLL_WIDTH) * scaler.getScaleFactor(), height * scaler.getScaleFactor());

		for (String row : items) {
			if(row.equals(currentItem)){
				drawRect(xPosition, yPosition + rowTop - scrollTop - 1, xPosition + width - SCROLL_WIDTH, yPosition + rowTop - scrollTop + lineHeight - 1, selectedColor);
				fontRenderer.drawString(row, xPosition + BASIC_X_OFFSET, yPosition + rowTop - scrollTop, selectedFontColor);
			}else
				fontRenderer.drawString(row, xPosition + BASIC_X_OFFSET, yPosition + rowTop - scrollTop, fontColor);
			
			rowTop += lineHeight;
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		// Slider
		int sliderX = xPosition + width - SCROLL_WIDTH + 1;
		sliderY = yPosition
				+ SCROLL_BUTTON_HEIGHT
				+ ((height - 2 * SCROLL_BUTTON_HEIGHT - sliderHeight) * scrollTop)
				/ (lineHeight * items.size() + BASIC_Y_OFFSET - height);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine
				.getTexture(TEXTURE_LOCATION).getGlTextureId());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawTexturedModalRect(sliderX, sliderY, 131, 16, SCROLL_WIDTH - 1, 1);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((sliderX), sliderY
				+ sliderHeight - 1, zLevel,
				131 / 256F, (18) / 256F);
		tessellator.addVertexWithUV(sliderX + SCROLL_WIDTH - 1,
				sliderY + sliderHeight - 1, zLevel,
				(131 + SCROLL_WIDTH - 1) / 256F,
				(18) / 256F);
		tessellator.addVertexWithUV(sliderX + SCROLL_WIDTH - 1,
				sliderY + 1, zLevel,
				(131 + SCROLL_WIDTH - 1) / 256F,
				(17) / 256F);
		tessellator.addVertexWithUV((sliderX), sliderY + 1,
				zLevel, 131 / 256F,
				(17) / 256F);
		tessellator.draw();

		drawTexturedModalRect(sliderX, sliderY + sliderHeight - 1, 131, 19,
				SCROLL_WIDTH - 1, 1);
	}

	private void setCurrent(int targetY) {
		if (lineHeight == 0)
			return;

		int itemIndex = (targetY - BASIC_Y_OFFSET - yPosition + scrollTop) / lineHeight;
		if (itemIndex >= items.size())
			itemIndex = items.size() - 1;
		
		String newSound = items.get(itemIndex);
		if (!newSound.equals(alarm.getSoundName())) {
			if (alarm.getWorldObj().isRemote)
				NuclearNetworkHelper.setNewAlarmSound(alarm.xCoord, alarm.yCoord, alarm.zCoord, (byte) 0, newSound);
			
			alarm.setSoundName(newSound);
		}
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int targetX, int targetY) {
		if(super.mousePressed(minecraft, targetX, targetY)){
			if (targetX > xPosition + width - SCROLL_WIDTH) {// scroll click

				if (targetY - yPosition < SCROLL_BUTTON_HEIGHT)
					scrollUp();
				else if (height + yPosition - targetY < SCROLL_BUTTON_HEIGHT)
					scrollDown();
				else if (targetY >= sliderY && targetY <= sliderY + sliderHeight) {
					dragging = true;
					dragDelta = targetY - sliderY;
				}
			}else {
				setCurrent(targetY);

			return true;
			}
		}else {
			return false;
		}
		return false;
	}

	@Override
	public void mouseReleased(int i, int j) {
		super.mouseReleased(i, j);
		dragging = false;
	}
}
