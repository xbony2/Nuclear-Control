package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanelExtender;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class InfoPanelExtender extends InfoPanel {
	private static final int DAMAGE = Damages.DAMAGE_INFO_PANEL_EXTENDER;
	private static final float[] BOUNDS = { 0, 0, 0, 1, 1, 1 };

	private static final byte[][] mapping = {
			{ I_EXTENDER_BACK, I_COLOR_DEFAULT, I_EXTENDER_SIDE,
					I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE },
			{ I_COLOR_DEFAULT, I_EXTENDER_BACK, I_EXTENDER_SIDE,
					I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE },
			{ I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_BACK,
					I_COLOR_DEFAULT, I_EXTENDER_SIDE, I_EXTENDER_SIDE },
			{ I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_COLOR_DEFAULT,
					I_EXTENDER_BACK, I_EXTENDER_SIDE, I_EXTENDER_SIDE },
			{ I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE,
					I_EXTENDER_SIDE, I_EXTENDER_BACK, I_COLOR_DEFAULT },
			{ I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE,
					I_EXTENDER_SIDE, I_COLOR_DEFAULT, I_EXTENDER_BACK } };

	public InfoPanelExtender() {
		super(DAMAGE, "tile.blockInfoPanelExtender");
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileEntityInfoPanelExtender();
	}

	@Override
	public boolean isSolidBlockRequired() {
		return false;
	}

	@Override
	public boolean hasGui() {
		return false;
	}

	@Override
	public float[] getBlockBounds(TileEntity tileEntity) {
		return BOUNDS;
	}

	@Override
	public Container getServerGuiElement(TileEntity tileEntity,
			EntityPlayer player) {
		return null;
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player) {
		return null;
	}

	@Override
	protected byte[][] getMapping() {
		return mapping;
	}
}