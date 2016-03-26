package shedar.mods.ic2.nuclearcontrol.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CompactButton extends GuiButton {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIThermalMonitor.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

	public CompactButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}

	@Override
	public void drawButton(Minecraft minecraft, int par2, int par3) {
		if (this.visible) {
			FontRenderer fontRenderer = minecraft.fontRenderer;
			minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean var5 = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			int var6 = this.getHoverState(var5);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 64 + var6 * 12, this.width / 2 + width % 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2 + width % 2, this.yPosition, 200 - this.width / 2, 64 + var6 * 12, this.width / 2, this.height);
			this.mouseDragged(minecraft, par2, par3);
			fontRenderer.drawString(displayString, xPosition + (width - fontRenderer.getStringWidth(displayString)) / 2, yPosition + 2, 0x303030);
		}
	}

}
