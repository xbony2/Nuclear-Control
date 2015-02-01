package shedar.mods.ic2.nuclearcontrol.gui.controls;

import java.lang.reflect.Method;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRangeTriggerInvertRedstone extends GuiButton {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIRangeTrigger.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
			TEXTURE_FILE);

	TileEntityRangeTrigger trigger;
	private boolean checked;

	public GuiRangeTriggerInvertRedstone(int id, int x, int y, TileEntityRangeTrigger trigger) {
		super(id, x, y, 0, 0, "");
		height = 15;
		width = 18;
		this.trigger = trigger;
		checked = trigger.isInvertRedstone();
	}

	@Override
	public void drawButton(Minecraft minecraft, int par2, int par3) {
		if (this.visible) {
			minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int delta = checked ? 15 : 0;
			drawTexturedModalRect(xPosition, yPosition + 1, 176, delta, 18, 15);
		}
	}

	@Override
	public int getHoverState(boolean flag) {
		return 0;
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		if (super.mousePressed(minecraft, i, j)) {
			checked = !checked;
			int value = checked ? -2 : -1;
			trigger.setInvertRedstone(checked);
			NetworkHelper nh = new NetworkHelper();
			try {
				Method m1 = nh.getClass().getDeclaredMethod("initiateClientTileEntityEvent");
				m1.setAccessible(true);
				m1.invoke(trigger, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*System.out.println("DEBUG STUFF");
			try { TODO: didn't work so well
				Method m1 = nh.getClass().getMethod("initiateClientTileEntityEvent", TileEntity.class, int.class);
				m1.setAccessible(true);
				m1.invoke(nh, trigger, value);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			return true;
		} else {
			return false;
		}
	}

}
