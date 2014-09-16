package shedar.mods.ic2.nuclearcontrol.renderers;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.IScreenPart;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.panel.Screen;
import shedar.mods.ic2.nuclearcontrol.renderers.model.ModelInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

public class TileEntityInfoPanelRenderer extends TileEntitySpecialRenderer
{

	private static String implodeArray(String[] inputArray, String glueString) 
	{
		String output = "";
		if (inputArray.length > 0) 
		{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < inputArray.length; i++) {
				if(inputArray[i] == null || inputArray[i].isEmpty())
					continue;
				sb.append(glueString);
				sb.append(inputArray[i]);
			}
			output = sb.toString();
			if(output.length() > 1)
				output = output.substring(1);
		}
		return output;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
	{
		boolean isPanel = tileEntity instanceof TileEntityInfoPanel;
		if (!isPanel && tileEntity instanceof IScreenPart)
		{
			Screen scr = ((IScreenPart)tileEntity).getScreen();
			if (scr != null)
			{
				TileEntity core = scr.getCore(tileEntity.getWorldObj());
				if (core != null)
				{
					x += core.xCoord - tileEntity.xCoord;
					y += core.yCoord - tileEntity.yCoord;
					z += core.zCoord - tileEntity.zCoord;
					tileEntity = core;
					isPanel = tileEntity instanceof TileEntityInfoPanel;
				}
			}
		}

		if (isPanel)
		{
			TileEntityInfoPanel panel = (TileEntityInfoPanel)tileEntity;
			if (!panel.getPowered())
			{
				return;
			}
			List<ItemStack> cards = panel.getCards();
			boolean anyCardFound = false;
			List<PanelString> joinedData = new LinkedList<PanelString>();
			for (ItemStack card : cards)
			{
				if (card == null || !(card.getItem() instanceof IPanelDataSource))
				{
					continue;
				}
				int displaySettings = panel.getDisplaySettingsByCard(card);
				if (displaySettings == 0)
				{
					continue;
				}
				CardWrapperImpl helper = new CardWrapperImpl(card, -1);
				CardState state = helper.getState();
				List<PanelString> data;
				if (state != CardState.OK && state != CardState.CUSTOM_ERROR)
				{
					data = StringUtils.getStateMessage(state);
				}
				else
				{
					data = panel.getCardData(displaySettings, card, helper);
				}
				if (data == null)
				{
					continue;
				}
				joinedData.addAll(data);
				anyCardFound = true;
			}
			if (!anyCardFound)
			{
				return;
			}

			GL11.glPushMatrix();
			GL11.glPolygonOffset( -10, -10 );
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			short side = (short)Facing.oppositeSide[panel.getFacing()];
			Screen screen = panel.getScreen();
			float dx = 1F/16;
			float dz = 1F/16;
			float displayWidth = 1 - 2F/16;
			float displayHeight = 1 - 2F/16;
			if (screen != null)
			{
				y -= panel.yCoord - screen.maxY;
				if(side == 0 || side == 1 || side == 2 || side == 3 || side == 5)
				{
					z -= panel.zCoord - screen.minZ;
				}
				else
				{
					z -= panel.zCoord - screen.maxZ;
				}

				if(side == 0 || side == 2 || side == 4)
				{
					x -= panel.xCoord - screen.minX;
				}
				else
				{
					x -= panel.xCoord - screen.maxX;
				}
			}
			GL11.glTranslatef((float)x, (float)y, (float)z);
			switch (side)
			{
			case 0:
				if (screen != null)
				{
					displayWidth += screen.maxX - screen.minX;
					displayHeight += screen.maxZ - screen.minZ;
				}
				break;
			case 1:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(180, 0, 1, 0);
				if (screen != null)
				{
					displayWidth += screen.maxX - screen.minX;
					displayHeight += screen.maxZ - screen.minZ;
				}
				break;
			case 2:
				GL11.glTranslatef(0, 1, 0);
				GL11.glRotatef(0, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				if (screen != null)
				{
					displayWidth += screen.maxX - screen.minX;
					displayHeight += screen.maxY - screen.minY;
				}
				break;
			case 3:
				GL11.glTranslatef(1, 1, 1);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				if (screen != null)
				{
					displayWidth += screen.maxX - screen.minX;
					displayHeight += screen.maxY - screen.minY;
				}
				break;
			case 4:
				GL11.glTranslatef(0, 1, 1);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				if (screen != null)
				{
					displayWidth += screen.maxZ - screen.minZ;
					displayHeight += screen.maxY - screen.minY;
				}
				break;
			case 5:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				if (screen != null)
				{
					displayWidth += screen.maxZ - screen.minZ;
					displayHeight += screen.maxY - screen.minY;
				}
				break;
			}
			float thickness = 1;
			double angleHor = 0;
			double angleVert = 0;
			double[] deltas = null;
			if (panel instanceof TileEntityAdvancedInfoPanel && screen!=null)
			{
				TileEntityAdvancedInfoPanel advPanel = (TileEntityAdvancedInfoPanel)panel;
				ModelInfoPanel model = new ModelInfoPanel();
				deltas = model.getDeltas(advPanel, screen);
				thickness = (float) (advPanel.thickness/16F - (deltas[0]+deltas[1]+deltas[2]+deltas[3])/4);
			}

			GL11.glTranslatef(dx+displayWidth/2, thickness, dz+displayHeight/2);
			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			switch (panel.rotation)
			{
			case 0:
				break;
			case 1:
				GL11.glRotatef(-90, 0, 0, 1);
				float t = displayHeight;
				displayHeight = displayWidth;
				displayWidth = t;
				break;
			case 2:
				GL11.glRotatef(90, 0, 0, 1);
				float tm = displayHeight;
				displayHeight = displayWidth;
				displayWidth = tm;
				break;
			case 3:
				GL11.glRotatef(180, 0, 0, 1);
				break;
			}
			if (deltas != null)
			{
				if(deltas[0] == 0)
				{
					//+1,-2
					angleHor = 180/Math.PI*Math.atan(deltas[1]/(displayWidth+2F/16));
					angleVert = -180/Math.PI*Math.atan(deltas[2]/(displayHeight+2F/16));
				}
				else if (deltas[1] == 0)
				{
					//-0,-3
					angleHor = -180/Math.PI*Math.atan(deltas[0]/(displayWidth+2F/16));
					angleVert = -180/Math.PI*Math.atan(deltas[3]/(displayHeight+2F/16));
				}
				else if (deltas[2] == 0)
				{
					//+3,+0
					angleHor = 180/Math.PI*Math.atan(deltas[3]/(displayWidth+2F/16));
					angleVert = 180/Math.PI*Math.atan(deltas[0]/(displayHeight+2F/16));
				}
				else
				{
					//-2,+1
					angleHor = -180/Math.PI*Math.atan(deltas[2]/(displayWidth+2F/16));
					angleVert = 180/Math.PI*Math.atan(deltas[1]/(displayHeight+2F/16));
				}
			}
			GL11.glRotatef((float)-angleVert, 1, 0, 0);
			GL11.glRotatef((float)angleHor, 0, 1, 0);
			FontRenderer fontRenderer = this.func_147498_b();

			int maxWidth = 1;
			for (PanelString panelString : joinedData)
			{
				String currentString = implodeArray(new String[]{panelString.textLeft, panelString.textCenter, panelString.textRight}," ");
				maxWidth = Math.max(fontRenderer.getStringWidth(currentString), maxWidth);
			}
			maxWidth += 4;

			int lineHeight = fontRenderer.FONT_HEIGHT + 2;
			int requiredHeight = lineHeight * joinedData.size();
			float scaleX = displayWidth/maxWidth;
			float scaleY = displayHeight/requiredHeight;
			float scale = Math.min(scaleX, scaleY);
			GL11.glScalef(scale, -scale, scale);
			GL11.glDepthMask(false);

			int offsetX;
			int offsetY;

			int realHeight = (int)Math.floor(displayHeight/scale);
			int realWidth = (int)Math.floor(displayWidth/scale);

			if (scaleX < scaleY)
			{
				offsetX = 2;
				offsetY = (realHeight - requiredHeight) / 2;
			}
			else
			{
				offsetX = (realWidth - maxWidth) / 2 + 2;
				offsetY = 0;
			}
			Block block = panel.getWorldObj().getBlock(panel.xCoord, panel.yCoord, panel.zCoord);
			if (block == null)
			{
				block = Blocks.stone;
			}

			GL11.glDisable(GL11.GL_LIGHTING);

			int row = 0;
			for (PanelString panelString : joinedData)
			{
				if (panelString.textLeft != null)
				{
					fontRenderer.drawString(panelString.textLeft, offsetX-realWidth / 2, 1+offsetY-realHeight / 2 + row * lineHeight, panelString.colorLeft != 0?panelString.colorLeft:panel.getColorTextHex());
				}
				if (panelString.textCenter != null)
				{
					fontRenderer.drawString(panelString.textCenter, -fontRenderer.getStringWidth(panelString.textCenter) / 2, offsetY - realHeight/2  + row * lineHeight, panelString.colorCenter !=0 ?panelString.colorCenter:panel.getColorTextHex());
				}
				if (panelString.textRight != null)
				{
					fontRenderer.drawString(panelString.textRight, realWidth / 2-fontRenderer.getStringWidth(panelString.textRight), offsetY - realHeight/2  + row * lineHeight, panelString.colorRight != 0?panelString.colorRight:panel.getColorTextHex());
				}
				row++;
			}

			GL11.glEnable(GL11.GL_LIGHTING);

			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL );
			GL11.glPopMatrix();
		}
	}
}