package shedar.mods.ic2.nuclearcontrol.renderers.model;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.panel.Screen;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelInfoPanel {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/blocks/infoPanel/panelAdvancedSide.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

	private double[] coordinates = new double[24];

	private void assignWithRotation(int rotation, int offset, int sign, int tl,
			int tr, int br, int bl, double dtl, double dtr, double dbr,
			double dbl) {
		switch (rotation) {
		case 0:
			coordinates[tl * 3 + offset] += sign * dtl;
			coordinates[tr * 3 + offset] += sign * dtr;
			coordinates[br * 3 + offset] += sign * dbr;
			coordinates[bl * 3 + offset] += sign * dbl;
			break;
		case 1:
			coordinates[tl * 3 + offset] += sign * dbl;
			coordinates[tr * 3 + offset] += sign * dtl;
			coordinates[br * 3 + offset] += sign * dtr;
			coordinates[bl * 3 + offset] += sign * dbr;
			break;
		case 2:
			coordinates[tl * 3 + offset] += sign * dtr;
			coordinates[tr * 3 + offset] += sign * dbr;
			coordinates[br * 3 + offset] += sign * dbl;
			coordinates[bl * 3 + offset] += sign * dtl;
			break;
		case 3:
			coordinates[tl * 3 + offset] += sign * dbr;
			coordinates[tr * 3 + offset] += sign * dbl;
			coordinates[br * 3 + offset] += sign * dtl;
			coordinates[bl * 3 + offset] += sign * dtr;
			break;

		default:
			break;
		}
	}

	public double[] getDeltas(TileEntityAdvancedInfoPanel panel, Screen screen) {
		boolean isTopBottom = panel.rotateVert != 0;
		boolean isLeftRight = panel.rotateHor != 0;
		double dTopLeft = 0;
		double dTopRight = 0;
		double dBottomLeft = 0;
		double dBottomRight = 0;
		int height = screen.getHeight(panel);
		int width = screen.getWidth(panel);
		double maxDelta = 0;
		if (isTopBottom) {
			if (panel.rotateVert > 0) // |\
			{ // | \
				dBottomRight = dBottomLeft = height
						* Math.tan(Math.PI * panel.rotateVert / 180);
				maxDelta = dBottomLeft;
			} else {
				dTopRight = dTopLeft = height
						* Math.tan(Math.PI * -panel.rotateVert / 180);
				maxDelta = dTopRight;
			}
		}
		if (isLeftRight) {
			if (panel.rotateHor > 0) // -------
			{ // | . '
				dTopRight = dBottomRight = width
						* Math.tan(Math.PI * panel.rotateHor / 180);
				maxDelta = dTopRight;
			} else {
				dTopLeft = dBottomLeft = width
						* Math.tan(Math.PI * -panel.rotateHor / 180);
				maxDelta = dBottomLeft;
			}
		}
		if (isTopBottom && isLeftRight) {
			if (dTopLeft == 0) {
				maxDelta = dBottomRight = dBottomLeft + dTopRight;
			} else if (dTopRight == 0) {
				maxDelta = dBottomLeft = dTopLeft + dBottomRight;
			} else if (dBottomLeft == 0) {
				maxDelta = dTopRight = dTopLeft + dBottomRight;
			} else {
				maxDelta = dTopLeft = dBottomLeft + dTopRight;
			}
		}
		double thickness = panel.thickness / 16D;
		if (maxDelta > thickness) {
			double scale = thickness / maxDelta;
			dTopLeft = scale * dTopLeft;
			dTopRight = scale * dTopRight;
			dBottomLeft = scale * dBottomLeft;
			dBottomRight = scale * dBottomRight;
		}
		double[] res = { dTopLeft, dTopRight, dBottomLeft, dBottomRight };
		return res;
	}

	private void addSlopes(TileEntityAdvancedInfoPanel panel, Screen screen,
			double[] deltas) {
		// if (panel.rotateVert == 0 && panel.rotateHor == 0)
		// return;
		double dTopLeft = deltas[0];
		double dTopRight = deltas[1];
		double dBottomLeft = deltas[2];
		double dBottomRight = deltas[3];
		int facing = panel.facing;
		int rotation = panel.getRotation();
		switch (facing) {
		case 0:
			assignWithRotation(rotation, 1, -1, 4, 7, 6, 5, dTopLeft,
					dTopRight, dBottomRight, dBottomLeft);
			break;
		case 1:
			assignWithRotation(rotation, 1, 1, 3, 0, 1, 2, dTopLeft, dTopRight,
					dBottomRight, dBottomLeft);
			break;
		case 2:
			assignWithRotation(rotation, 2, -1, 5, 6, 2, 1, dTopLeft,
					dTopRight, dBottomRight, dBottomLeft);
			break;
		case 3:
			assignWithRotation(rotation, 2, 1, 7, 4, 0, 3, dTopLeft, dTopRight,
					dBottomRight, dBottomLeft);
			break;
		case 4:
			assignWithRotation(rotation, 0, -1, 6, 7, 3, 2, dTopLeft,
					dTopRight, dBottomRight, dBottomLeft);
			break;
		case 5:
			assignWithRotation(rotation, 0, 1, 4, 5, 1, 0, dTopLeft, dTopRight,
					dBottomRight, dBottomLeft);
			break;
		}
	}

	private void initCoordinates(Block block, Screen screen) {

		// 5o ----- o6
		// 4o ----- o7
		// / | /
		// / 1o / o2
		// 0o ----- o3
		double blockMinX = block.getBlockBoundsMinX();
		double blockMinY = block.getBlockBoundsMinY();
		double blockMinZ = block.getBlockBoundsMinZ();

		double blockMaxX = block.getBlockBoundsMaxX();
		double blockMaxY = block.getBlockBoundsMaxY();
		double blockMaxZ = block.getBlockBoundsMaxZ();

		/* 0 */
		coordinates[0] = screen.minX + blockMinX;
		coordinates[1] = screen.minY + blockMinY;
		coordinates[2] = screen.minZ + blockMinZ;
		/* 1 */
		coordinates[3] = screen.minX + blockMinX;
		coordinates[4] = screen.minY + blockMinY;
		coordinates[5] = screen.maxZ + blockMaxZ;
		/* 2 */
		coordinates[6] = screen.maxX + blockMaxX;
		coordinates[7] = screen.minY + blockMinY;
		coordinates[8] = screen.maxZ + blockMaxZ;
		/* 3 */
		coordinates[9] = screen.maxX + blockMaxX;
		coordinates[10] = screen.minY + blockMinY;
		coordinates[11] = screen.minZ + blockMinZ;
		/* 4 */
		coordinates[12] = screen.minX + blockMinX;
		coordinates[13] = screen.maxY + blockMaxY;
		coordinates[14] = screen.minZ + blockMinZ;
		/* 5 */
		coordinates[15] = screen.minX + blockMinX;
		coordinates[16] = screen.maxY + blockMaxY;
		coordinates[17] = screen.maxZ + blockMaxZ;
		/* 6 */
		coordinates[18] = screen.maxX + blockMaxX;
		coordinates[19] = screen.maxY + blockMaxY;
		coordinates[20] = screen.maxZ + blockMaxZ;
		/* 7 */
		coordinates[21] = screen.maxX + blockMaxX;
		coordinates[22] = screen.maxY + blockMaxY;
		coordinates[23] = screen.minZ + blockMinZ;
	}

	private void addPoint(int point, double u, double v) {
		Tessellator.instance.addVertexWithUV(coordinates[point * 3],
				coordinates[point * 3 + 1], coordinates[point * 3 + 2], u, v);
	}

	private void drawFacing(int facing, int rotation, Screen screen, TileEntityAdvancedInfoPanel panel, Block block, Tessellator tess) {
		// TODO: refactor here
		int point = 0;
		int pointR = 0;
		int pointB = 0;
		int pointRB = 0;

		int offsetH = 0;
		int offsetV = 0;
		int offsetD = 0;

		boolean ccw = false;
		switch (facing) {
		case 0:
			tess.setNormal(0, -1, 0);
			point = 3;
			pointR = 0;
			pointB = 2;
			pointRB = 1;

			offsetH = 0;
			offsetV = 2;
			offsetD = 1;
			ccw = true;
			break;
		case 1:
			tess.setNormal(0, 1, 0);
			point = 4;
			pointR = 7;
			pointB = 5;
			pointRB = 6;

			offsetH = 0;
			offsetV = 2;
			offsetD = 1;
			break;
		case 2:
			tess.setNormal(0, 0, -1);
			point = 7;
			pointR = 4;
			pointB = 3;
			pointRB = 0;

			offsetH = 0;
			offsetV = 1;
			offsetD = 2;
			ccw = rotation == 1 || rotation == 2;
			break;
		case 3:
			tess.setNormal(0, 0, 1);
			point = 5;
			pointR = 6;
			pointB = 1;
			pointRB = 2;

			offsetH = 0;
			offsetV = 1;
			offsetD = 2;
			break;
		case 4:
			tess.setNormal(-1, 0, 0);
			point = 4;
			pointR = 5;
			pointB = 0;
			pointRB = 1;

			offsetH = 2;
			offsetV = 1;
			offsetD = 0;
			break;
		case 5:
			tess.setNormal(1, 0, 0);
			point = 6;
			pointR = 7;
			pointB = 2;
			pointRB = 3;

			offsetH = 2;
			offsetV = 1;
			offsetD = 0;
			ccw = rotation == 1 || rotation == 2;
			break;
		}
		switch (rotation) {
		case 1:
			int tmp = offsetH;
			offsetH = offsetV;
			offsetV = tmp;

			pointB = point;
			point = pointR;
			pointR = pointRB;
			break;
		case 2:
			tmp = offsetH;
			offsetH = offsetV;
			offsetV = tmp;

			pointR = point;
			point = pointB;
			pointB = pointRB;
			break;
		case 3:
			point = pointRB;
			tmp = pointR;
			pointR = pointB;
			pointB = tmp;
			break;
		}

		int stepsHor = screen.getWidth(panel);
		int stepsVert = screen.getHeight(panel);
		int sh = 0;
		double dh = (coordinates[pointR * 3 + offsetH] - coordinates[point * 3
				+ offsetH])
				/ stepsHor;
		double dv = (coordinates[pointB * 3 + offsetV] - coordinates[point * 3
				+ offsetV])
				/ stepsVert;
		double ddh = (coordinates[pointR * 3 + offsetD] - coordinates[point * 3
				+ offsetD])
				/ stepsHor;
		double ddv = (coordinates[pointB * 3 + offsetD] - coordinates[point * 3
				+ offsetD])
				/ stepsVert;
		double[] base = new double[] { coordinates[point * 3],
				coordinates[point * 3 + 1], coordinates[point * 3 + 2] };
		double[] midpoint = new double[3];
		while (sh < stepsHor) {
			int sv = 0;
			while (sv < stepsVert) {
				double[] p = base.clone();
				p[offsetH] += dh * sh;
				p[offsetV] += dv * sv;
				p[offsetD] += ddh * sh + ddv * sv;

				midpoint[offsetH] = p[offsetH] + dh / 2;
				midpoint[offsetV] = p[offsetV] + dv / 2;
				midpoint[offsetD] = p[offsetD] + (ddh + ddv) / 2;

				IIcon texture = block.getIcon(panel.getWorldObj(),
						(int) Math.floor(midpoint[0]),
						(int) Math.floor(midpoint[1]),
						(int) Math.floor(midpoint[2]), facing);

				double u1 = texture.getMinU();
				double u2 = texture.getMaxU();
				double v1 = texture.getMinV();
				double v2 = texture.getMaxV();
				if (ccw) {
					double tu = u1;
					u1 = u2;
					u2 = tu;
				}
				tess.addVertexWithUV(p[0], p[1], p[2], u1, v1);
				p[offsetV] += dv;
				p[offsetD] += ddv;
				tess.addVertexWithUV(p[0], p[1], p[2], u1, v2);
				p[offsetH] += dh;
				p[offsetD] += ddh;
				tess.addVertexWithUV(p[0], p[1], p[2], u2, v2);
				p[offsetV] -= dv;
				p[offsetD] -= ddv;
				tess.addVertexWithUV(p[0], p[1], p[2], u2, v1);

				sv++;
			}
			sh++;
		}
	}

	public void renderScreen(Block block, TileEntityAdvancedInfoPanel panel, double x, double y, double z, RenderBlocks renderer) {
		Screen screen = panel.getScreen();
		if (screen == null)
			return;
		initCoordinates(block, screen);
		double[] deltas = getDeltas(panel, screen);
		addSlopes(panel, screen, deltas);

		int facing = panel.getFacing();
		Tessellator tess = Tessellator.instance;

		tess.setBrightness(block.getMixedBrightnessForBlock(panel.getWorldObj(), panel.xCoord, panel.yCoord, panel.zCoord));
		tess.setColorOpaque_F(0.5F, 0.5F, 0.5F);
		drawFacing(facing, panel.getRotation(), screen, panel, block, tess);
        //
		tess.draw();
		
		//SIDES
		Tessellator.instance.startDrawingQuads();
        renderer.minecraftRB.renderEngine.bindTexture(TEXTURE_LOCATION);
		Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(panel.getWorldObj(), panel.xCoord, panel.yCoord, panel.zCoord));
		Tessellator.instance.setColorOpaque_F(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int dx = screen.getDx() + 1;
		int dy = screen.getDy() + 1;
		int dz = screen.getDz() + 1;

		// bottom
		if (facing != 0) {
			Tessellator.instance.setNormal(0, -1, 0);
			addPoint(0, 0, 0);
			addPoint(3, dx, 0);
			addPoint(2, dx, dz);
			addPoint(1, 0, dz);
		}

		if (facing != 1) {
			Tessellator.instance.setNormal(0, 1, 0);
			addPoint(4, 0, 0);
			addPoint(5, dz, 0);
			addPoint(6, dz, dx);
			addPoint(7, 0, dx);
		}

		if (facing != 2) {
			Tessellator.instance.setNormal(0, 0, -1);
			addPoint(0, 0, 0);
			addPoint(4, dy, 0);
			addPoint(7, dy, dx);
			addPoint(3, 0, dx);
		}

		if (facing != 3) {
			Tessellator.instance.setNormal(0, 0, 1);
			addPoint(6, 0, 0);
			addPoint(5, dx, 0);
			addPoint(1, dx, dy);
			addPoint(2, 0, dy);
		}

		if (facing != 4) {
			Tessellator.instance.setNormal(-1, 0, 0);
			addPoint(5, 0, 0);
			addPoint(4, dz, 0);
			addPoint(0, dz, dy);
			addPoint(1, 0, dy);
		}

		if (facing != 5) {
			Tessellator.instance.setNormal(1, 0, 0);
			addPoint(2, 0, 0);
			addPoint(3, dz, 0);
			addPoint(7, dz, dy);
			addPoint(6, 0, dy);
		}
		Tessellator.instance.draw();
		
		//RETURN TO MC DRAWING
		Tessellator.instance.startDrawingQuads();
		renderer.minecraftRB.renderEngine.bindTexture(TextureMap.locationBlocksTexture/* blocks texture atlas*/);
	}
}