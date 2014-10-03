package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerInfoPanel;
import shedar.mods.ic2.nuclearcontrol.gui.GuiInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;

public class InfoPanel extends Subblock {
	private static final int DAMAGE = BlockDamages.DAMAGE_INFO_PANEL;
	private static final float[] BOUNDS = { 0, 0, 0, 1, 1, 1 };

	public static final byte I_PANEL_BACK = 0;
	public static final byte I_PANEL_SIDE = 1;
	public static final byte I_PANEL_ADV_SIDE = 2;

	public static final byte I_EXTENDER_BACK = 3;
	public static final byte I_EXTENDER_SIDE = 4;
	public static final byte I_EXTENDER_ADV_SIDE = 5;

	public static final byte I_COLORS_OFFSET = 6;
	public static final int I_COLOR_DEFAULT = I_COLORS_OFFSET + 16 * 3 - 1;

	private static final byte[][] mapping = {
			{ I_PANEL_BACK, I_COLOR_DEFAULT, I_PANEL_SIDE, I_PANEL_SIDE,
					I_PANEL_SIDE, I_PANEL_SIDE },
			{ I_COLOR_DEFAULT, I_PANEL_BACK, I_PANEL_SIDE, I_PANEL_SIDE,
					I_PANEL_SIDE, I_PANEL_SIDE },
			{ I_PANEL_SIDE, I_PANEL_SIDE, I_PANEL_BACK, I_COLOR_DEFAULT,
					I_PANEL_SIDE, I_PANEL_SIDE },
			{ I_PANEL_SIDE, I_PANEL_SIDE, I_COLOR_DEFAULT, I_PANEL_BACK,
					I_PANEL_SIDE, I_PANEL_SIDE },
			{ I_PANEL_SIDE, I_PANEL_SIDE, I_PANEL_SIDE, I_PANEL_SIDE,
					I_PANEL_BACK, I_COLOR_DEFAULT },
			{ I_PANEL_SIDE, I_PANEL_SIDE, I_PANEL_SIDE, I_PANEL_SIDE,
					I_COLOR_DEFAULT, I_PANEL_BACK } };

	protected IIcon[] icons = new IIcon[486];

	public InfoPanel() {
		super(DAMAGE, "tile.blockInfoPanel");
	}

	public InfoPanel(int damage, String name) {
		super(damage, name);
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileEntityInfoPanel();
	}

	@Override
	public boolean isSolidBlockRequired() {
		return false;
	}

	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	public float[] getBlockBounds(TileEntity tileEntity) {
		return BOUNDS;
	}

	@Override
	public Container getServerGuiElement(TileEntity tileEntity,
			EntityPlayer player) {
		return new ContainerInfoPanel(player, (TileEntityInfoPanel) tileEntity);
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player) {
		ContainerInfoPanel containerPanel = new ContainerInfoPanel(player,
				(TileEntityInfoPanel) tileEntity);
		return new GuiInfoPanel(containerPanel);
	}

	@Override
	public IIcon getIcon(int index) {
		return icons[index];
	}

	@Override
	protected byte[][] getMapping() {
		return mapping;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icons[I_PANEL_BACK] = iconRegister
				.registerIcon("nuclearcontrol:infoPanel/panelBack");
		icons[I_PANEL_SIDE] = iconRegister
				.registerIcon("nuclearcontrol:infoPanel/panelSide");
		icons[I_PANEL_ADV_SIDE] = iconRegister
				.registerIcon("nuclearcontrol:infoPanel/panelAdvancedSide");

		icons[I_EXTENDER_BACK] = iconRegister
				.registerIcon("nuclearcontrol:infoPanel/extenderBack");
		icons[I_EXTENDER_SIDE] = iconRegister
				.registerIcon("nuclearcontrol:infoPanel/extenderSide");
		icons[I_EXTENDER_ADV_SIDE] = iconRegister
				.registerIcon("nuclearcontrol:infoPanel/extenderAdvancedSide");

		for (int i = 0; i <= 14; i++) {
			for (int j = 0; j <= 15; j++) {
				icons[i * 16 + j + I_COLORS_OFFSET] = iconRegister
						.registerIcon("nuclearcontrol:infoPanel/off/" + i + "/"
								+ j);
			}
		}

		for (int i = 0; i <= 14; i++) {
			for (int j = 0; j <= 15; j++) {
				icons[i * 16 + j + I_COLORS_OFFSET + 240] = iconRegister
						.registerIcon("nuclearcontrol:infoPanel/on/" + i + "/"
								+ j);
			}
		}
	}
}