package shedar.mods.ic2.nuclearcontrol.gui.controls;

import java.lang.reflect.Method;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIC2Thermo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRemoteThermoSlider extends GuiButton {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIRemoteThermo.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
			TEXTURE_FILE);

	private static final int ARROW_WIDTH = 6;
	private static final float TEMP_RANGE = 16000;
	private static final int HEAT_STEP = 100;

	public float sliderValue;
	public boolean dragging;
	private String label;
	private TileEntityIC2Thermo thermo;
	private float effectiveWidth;
	private double sliderValueStep;

	public GuiRemoteThermoSlider(int id, int x, int y, String label,
			TileEntityIC2Thermo thermo) {
		super(id, x, y, 181, 16, label);
		this.thermo = thermo;
		dragging = false;
		this.label = label;
		sliderValue = (thermo.getHeatLevel()) / TEMP_RANGE;
		displayString = String.format(label, getNormalizedHeatLevel());
		effectiveWidth = width - 8 - 2 * ARROW_WIDTH;
		sliderValueStep = HEAT_STEP / TEMP_RANGE;
	}

	public void checkMouseWheel(int mouseX, int mouseY) {
		boolean isHover = mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
		if (isHover) {
			int delta = Mouse.getEventDWheel();
			if (delta != 0) {
				int multiplier = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
						|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : 3;
				if (delta > 0) {
					setSliderPos(xPosition + 1, multiplier);
				} else {
					setSliderPos(xPosition + width - 1, multiplier);
				}
			}
		}
	}

	private int getNormalizedHeatLevel() {
		return ((int) Math.floor(TEMP_RANGE * sliderValue)) / 100 * 100;
	}

	private void setSliderPos(int targetX, int multiplier) {
		if (targetX < xPosition + ARROW_WIDTH) {// left arrow{
			sliderValue -= sliderValueStep * multiplier;
		} else if (targetX > xPosition + width - ARROW_WIDTH) {// right arrow
			sliderValue += sliderValueStep * multiplier;
		} else {
			sliderValue = (targetX - (xPosition + 4 + ARROW_WIDTH))
					/ effectiveWidth;
		}

		if (sliderValue < 0.0F) {
			sliderValue = 0.0F;
		}
		if (sliderValue > 1.0F) {
			sliderValue = 1.0F;
		}
		int newHeatLevel = getNormalizedHeatLevel();
		if (thermo.getHeatLevel() != newHeatLevel) {
			thermo.setHeatLevel(newHeatLevel);
			NetworkHelper nh = new NetworkHelper();
			try {
				Method m1 = nh.getClass().getDeclaredMethod("initiateClientTileEntityEvent");
				m1.setAccessible(true);
				m1.invoke(thermo, newHeatLevel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// NetworkHelper.initiateClientTileEntityEvent(thermo, newHeatLevel);
		}
		displayString = String.format(label, newHeatLevel);
	}

	@Override
	public void drawButton(Minecraft minecraft, int targetX, int targetY) {
		if (visible) {
			minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if (dragging && (targetX >= xPosition + ARROW_WIDTH)
					&& (targetX <= xPosition + width - ARROW_WIDTH)) {
				setSliderPos(targetX, 1);
			}
			drawTexturedModalRect(xPosition + ARROW_WIDTH
					+ (int) (sliderValue * effectiveWidth), yPosition, 0, 166,
					8, 16);
			minecraft.fontRenderer.drawString(displayString, xPosition,
					yPosition - 12, 0x404040);
		}
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int targetX, int j) {
		if (super.mousePressed(minecraft, targetX, j)) {
			setSliderPos(targetX, 1);
			dragging = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void mouseReleased(int i, int j) {
		super.mouseReleased(i, j);
		dragging = false;
	}
}
