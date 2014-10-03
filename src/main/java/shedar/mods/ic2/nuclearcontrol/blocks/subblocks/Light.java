package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityLightOff;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityLightOn;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class Light extends Subblock {

	private static final int DAMAGE1 = BlockDamages.DAMAGE_LIGHT_ON;
	private static final int DAMAGE2 = BlockDamages.DAMAGE_LIGHT_OFF;
	private static final float[] BOUNDS = { 0, 0, 0, 1, 1, 1 };

	boolean isOn;

	public static final byte I_SIDE = 0;

	private static final byte[][] mapping = {
			{ I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE },
			{ I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE },
			{ I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE },
			{ I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE },
			{ I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE },
			{ I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE } };

	private IIcon[] icons = new IIcon[1];

	public Light(boolean on) {
		super(on ? DAMAGE1 : DAMAGE2, on ? "tile.blockLightOn" : "tile.blockLightOff");
		isOn = on;
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
		if (isOn) {
			icons[0] = iconRegister.registerIcon("nuclearcontrol:light/on/white");
		} else {
			icons[0] = iconRegister.registerIcon("nuclearcontrol:light/off/white");
		}
	}

	@Override
	public TileEntity getTileEntity() {
		TileEntity entity = (isOn ? new TileEntityLightOn()
				: new TileEntityLightOff());
		return entity;
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

}
