package shedar.mods.ic2.nuclearcontrol.renderers;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo;

public class TileEntityIC2ThermoRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float f) {
		boolean isThermo = tileEntity instanceof TileEntityThermo;
		if (isThermo) {
			GL11.glPushMatrix();
			TileEntityThermo thermo = (TileEntityThermo) tileEntity;
			short side = (short) Facing.oppositeSide[thermo.getFacing()];
			float var12 = 0.014F;
			int heat = thermo.getHeatLevel();
			String text = Integer.toString(heat);
			GL11.glTranslatef((float) x, (float) y, (float) z);
			switch (side) {
			case 0:
				break;
			case 1:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(180, 0, 1, 0);
				break;
			case 2:
				GL11.glTranslatef(0, 1, 0);
				GL11.glRotatef(0, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 3:
				GL11.glTranslatef(1, 1, 1);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 4:
				GL11.glTranslatef(0, 1, 1);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 5:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;

			}
			GL11.glTranslatef(0.5F, 0.4375F, 0.6875F);

			FontRenderer fontRenderer = this.func_147498_b();
			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glScalef(var12, -var12, var12);
			GL11.glPolygonOffset(-10, -10);
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			fontRenderer.drawString(text,
					-fontRenderer.getStringWidth(text) / 2,
					-fontRenderer.FONT_HEIGHT, 0);
			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}
}