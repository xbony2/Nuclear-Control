package shedar.mods.ic2.nuclearcontrol.gui.controls;

import java.lang.reflect.Method;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHowlerAlarmSlider extends GuiButton {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIHowlerAlarm.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
			TEXTURE_FILE);

	public float sliderValue;
	public boolean dragging;
	private int minValue = 0;
	private int maxValue = 256;
	private int step = 8;
	private String label;
	private TileEntityHowlerAlarm alarm;

	public GuiHowlerAlarmSlider(int id, int x, int y, String label,
			TileEntityHowlerAlarm alarm) {
		super(id, x, y, 107, 16, label);
		this.alarm = alarm;
		dragging = false;
		this.label = label;
		if (alarm.getWorldObj().isRemote)
			maxValue = IC2NuclearControl.instance.maxAlarmRange;
		int currentRange = alarm.getRange();
		if (alarm.getWorldObj().isRemote && currentRange > maxValue)
			currentRange = maxValue;
		sliderValue = ((float) currentRange - minValue) / (maxValue - minValue);
		displayString = String.format(label, getNormalizedValue());
	}

	private int getNormalizedValue() {
		return (minValue + (int) Math
				.floor((maxValue - minValue) * sliderValue)) / step * step;
	}

	private void setSliderPos(int targetX) {
		sliderValue = (float) (targetX - (xPosition + 4)) / (float) (width - 8);
		if (sliderValue < 0.0F) {
			sliderValue = 0.0F;
		}
		if (sliderValue > 1.0F) {
			sliderValue = 1.0F;
		}
		int newValue = getNormalizedValue();
		if (alarm.getRange() != newValue) {
			alarm.setRange(newValue);
			NetworkHelper nh = new NetworkHelper();
			try {
				Method m1 = nh.getClass().getDeclaredMethod(
						"initiateClientTileEntityEvent");
				m1.setAccessible(true);
				m1.invoke(alarm, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// NetworkHelper.initiateClientTileEntityEvent(alarm, newValue);
		}
		displayString = String.format(label, newValue);
	}

	@Override
	public void drawButton(Minecraft minecraft, int targetX, int targetY) {
		if (visible) {
			minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if (dragging) {
				setSliderPos(targetX);
			}
			drawTexturedModalRect(
					xPosition + (int) (sliderValue * (width - 8)), yPosition,
					131, 0, 8, 16);
			minecraft.fontRenderer.drawString(displayString, xPosition,
					yPosition - 12, 0x404040);
		}
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int targetX, int j) {
		if (super.mousePressed(minecraft, targetX, j)) {
			setSliderPos(targetX);
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
