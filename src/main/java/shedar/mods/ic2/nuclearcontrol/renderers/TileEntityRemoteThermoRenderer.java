package shedar.mods.ic2.nuclearcontrol.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo;

public class TileEntityRemoteThermoRenderer extends TileEntitySpecialRenderer {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/blocks/remoteThermo/scale.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
			TEXTURE_FILE);

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float f) {
		boolean isThermo = tileEntity instanceof TileEntityRemoteThermo;
		if (isThermo) {
			GL11.glPushMatrix();
			GL11.glPolygonOffset(-10, -10);
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			TileEntityRemoteThermo thermo = (TileEntityRemoteThermo) tileEntity;
			short side = (short) Facing.oppositeSide[thermo.getFacing()];
			float var12 = 0.016666668F;
			int heat = thermo.getHeatLevel();
			String text = Integer.toString(heat);
			GL11.glTranslatef((float) x, (float) y, (float) z);
			bindTexture(TEXTURE_LOCATION);
			switch (side) {
			case 0:
				break;
			case 1:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(180, 0, 1, 0);
				break;
			case 2:
				GL11.glTranslatef(0, 1f, 0);
				GL11.glRotatef(0, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 3:
				GL11.glTranslatef(1, 1, 1);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 4:
				GL11.glTranslatef(0, 1, 1f);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 5:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				break;

			}
			GL11.glTranslatef(0.5F, 1F, 0.5F);
			GL11.glRotatef(-90, 1, 0, 0);
			switch (thermo.rotation) {
			case 0:
				break;
			case 1:
				GL11.glRotatef(-90, 0, 0, 1);
				break;
			case 2:
				GL11.glRotatef(90, 0, 0, 1);
				break;
			case 3:
				GL11.glRotatef(180, 0, 0, 1);
				break;
			}

			Block block = thermo.getWorldObj().getBlock(thermo.xCoord,
					thermo.yCoord, thermo.zCoord);
			if (block == null) {
				block = Blocks.stone;
			}
			int fire = thermo.getOnFire();
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setNormal(0, 0, 1);
			if (fire > -2) {
				tessellator.setBrightness(block.getMixedBrightnessForBlock(
						thermo.getWorldObj(), thermo.xCoord, thermo.yCoord,
						thermo.zCoord));
				tessellator.setColorOpaque_F(1, 1, 1);
				double left = -0.4375;
				double top = -0.3125;
				double width = 0.875;
				double height = 0.25;
				double deltaU = 1;
				double deltaV = 1;
				double u = 1D / 16;
				double v;
				double middle;
				if (fire == -1) {
					v = 0;
					middle = width;
				} else {
					double heatLevel = ((double) thermo.getOnFire())
							/ thermo.getHeatLevel();
					if (heatLevel > 1)
						heatLevel = 1;
					middle = heatLevel * width;
					v = 4D / 16;
				}
				tessellator.addVertexWithUV(left, top, 0, u, v);
				tessellator.addVertexWithUV(left + middle, top, 0, u + middle
						* deltaU, v);
				tessellator.addVertexWithUV(left + middle, top + height, 0, u
						+ middle * deltaU, v + height * deltaV);
				tessellator.addVertexWithUV(left, top + height, 0, u, v
						+ height * deltaV);

				if (middle != width) {
					v = 0.5;
					tessellator.addVertexWithUV(left + middle, top, 0, u, v);
					tessellator.addVertexWithUV(left + width, top, 0, u + width
							* deltaU, v);
					tessellator.addVertexWithUV(left + width, top + height, 0,
							u + width * deltaU, v + height * deltaV);
					tessellator.addVertexWithUV(left + middle, top + height, 0,
							u, v + height * deltaV);
				}
			}
			tessellator.draw();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			FontRenderer fontRenderer = this.func_147498_b();
			GL11.glDepthMask(false);
			GL11.glScalef(var12, -var12, var12);
			fontRenderer.drawString(text,
					-fontRenderer.getStringWidth(text) / 2,
					-fontRenderer.FONT_HEIGHT, 0);
			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glPopMatrix();
		}
	}
}
