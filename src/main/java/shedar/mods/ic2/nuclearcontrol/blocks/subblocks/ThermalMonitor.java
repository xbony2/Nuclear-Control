package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerEmpty;
import shedar.mods.ic2.nuclearcontrol.gui.GuiIC2Thermo;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;

public class ThermalMonitor extends Subblock {
	private static final int DAMAGE = BlockDamages.DAMAGE_THERMAL_MONITOR;
	private static final float[] BOUNDS = { 0.0625F, 0, 0.0625F, 0.9375F,
			0.4375F, 0.9375F };

	public static final byte I_BACK = 0;
	public static final byte I_SIDES_HOR = 1;
	public static final byte I_SIDES_VERT = 2;
	public static final byte I_FACE_GREEN = 3;
	public static final byte I_FACE_RED = 4;
	public static final byte I_FACE_GRAY = 5;

	private static final byte[][] mapping = {
			{ I_BACK, I_FACE_GREEN, I_SIDES_HOR, I_SIDES_HOR, I_SIDES_HOR,
					I_SIDES_HOR },
			{ I_FACE_GREEN, I_BACK, I_SIDES_HOR, I_SIDES_HOR, I_SIDES_HOR,
					I_SIDES_HOR },
			{ I_SIDES_HOR, I_SIDES_HOR, I_BACK, I_FACE_GREEN, I_SIDES_VERT,
					I_SIDES_VERT },
			{ I_SIDES_HOR, I_SIDES_HOR, I_FACE_GREEN, I_BACK, I_SIDES_VERT,
					I_SIDES_VERT },
			{ I_SIDES_VERT, I_SIDES_VERT, I_SIDES_VERT, I_SIDES_VERT, I_BACK,
					I_FACE_GREEN },
			{ I_SIDES_VERT, I_SIDES_VERT, I_SIDES_VERT, I_SIDES_VERT,
					I_FACE_GREEN, I_BACK } };

	private IIcon[] icons = new IIcon[6];
	

	public ThermalMonitor() {
		super(DAMAGE, "tile.blockThermalMonitor");
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileEntityThermo();
	}

	@Override
	public boolean isSolidBlockRequired() {
		return true;
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
		return new ContainerEmpty(tileEntity);
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player) {
		return new GuiIC2Thermo((TileEntityThermo) tileEntity);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icons[I_BACK] = iconRegister
				.registerIcon("nuclearcontrol:thermalMonitor/back");
		icons[I_SIDES_HOR] = iconRegister
				.registerIcon("nuclearcontrol:thermalMonitor/sidesHor");
		icons[I_SIDES_VERT] = iconRegister
				.registerIcon("nuclearcontrol:thermalMonitor/sidesVert");
		icons[I_FACE_RED] = iconRegister
				.registerIcon("nuclearcontrol:thermalMonitor/faceRed");
		icons[I_FACE_GREEN] = iconRegister
				.registerIcon("nuclearcontrol:thermalMonitor/faceGreen");
		icons[I_FACE_GRAY] = iconRegister
				.registerIcon("nuclearcontrol:thermalMonitor/faceGray");
	}

	@Override
	public IIcon getIcon(int index) {
		return icons[index];
	}

	@Override
	protected byte[][] getMapping() {
		return mapping;
	}

}
