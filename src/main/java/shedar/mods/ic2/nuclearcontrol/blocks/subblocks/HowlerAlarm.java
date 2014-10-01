package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerEmpty;
import shedar.mods.ic2.nuclearcontrol.gui.GuiHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class HowlerAlarm extends Subblock {
	private static final int DAMAGE = Damages.DAMAGE_HOWLER_ALARM;
	private static final float[] BOUNDS = { 0.125F, 0, 0.125F, 0.875F, 0.4375F,
			0.875F };

	public static final byte I_BACK = 0;
	public static final byte I_FACE = 1;
	public static final byte I_SIDE_HOR = 2;
	public static final byte I_SIDE_VERT = 3;

	private static final byte[][] mapping = {
			{ I_BACK, I_FACE, I_SIDE_HOR, I_SIDE_HOR, I_SIDE_HOR, I_SIDE_HOR },
			{ I_FACE, I_BACK, I_SIDE_HOR, I_SIDE_HOR, I_SIDE_HOR, I_SIDE_HOR },
			{ I_SIDE_HOR, I_SIDE_HOR, I_BACK, I_FACE, I_SIDE_VERT, I_SIDE_VERT },
			{ I_SIDE_HOR, I_SIDE_HOR, I_FACE, I_BACK, I_SIDE_VERT, I_SIDE_VERT },
			{ I_SIDE_VERT, I_SIDE_VERT, I_SIDE_VERT, I_SIDE_VERT, I_BACK,
					I_FACE },
			{ I_SIDE_VERT, I_SIDE_VERT, I_SIDE_VERT, I_SIDE_VERT, I_FACE,
					I_BACK } };

	private IIcon[] icons = new IIcon[4];

	public HowlerAlarm() {
		super(DAMAGE, "tile.blockHowlerAlarm");
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileEntityHowlerAlarm();
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
		return new GuiHowlerAlarm((TileEntityHowlerAlarm) tileEntity);
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
		icons[I_BACK] = iconRegister
				.registerIcon("nuclearcontrol:howlerAlarm/back");
		icons[I_FACE] = iconRegister
				.registerIcon("nuclearcontrol:howlerAlarm/face");
		icons[I_SIDE_HOR] = iconRegister
				.registerIcon("nuclearcontrol:howlerAlarm/sidesHor");
		icons[I_SIDE_VERT] = iconRegister
				.registerIcon("nuclearcontrol:howlerAlarm/sidesVert");
	}

}
