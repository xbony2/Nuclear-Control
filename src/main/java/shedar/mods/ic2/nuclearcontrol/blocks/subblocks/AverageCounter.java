package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerAverageCounter;
import shedar.mods.ic2.nuclearcontrol.gui.GuiAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class AverageCounter extends Subblock {
	private static final int DAMAGE = Damages.DAMAGE_AVERAGE_COUNTER;
	private static final float[] BOUNDS = { 0, 0, 0, 1, 1, 1 };

	public static final byte I_INPUT = 0;
	public static final byte I_OUTPUT = 1;

	private static final byte[][] mapping = {
			{ I_OUTPUT, I_INPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT },
			{ I_INPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT },
			{ I_OUTPUT, I_OUTPUT, I_OUTPUT, I_INPUT, I_OUTPUT, I_OUTPUT },
			{ I_OUTPUT, I_OUTPUT, I_INPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT },
			{ I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_INPUT },
			{ I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_INPUT, I_OUTPUT } };

	private IIcon[] icons = new IIcon[2];

	public AverageCounter() {
		super(DAMAGE, "tile.blockAverageCounter");
	}

	@Override
	public TileEntity getTileEntity() {
		TileEntity instance = IC2NuclearControl.instance.crossBC
				.getAverageCounter();
		if (instance == null)
			instance = new TileEntityAverageCounter();
		return instance;
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
		return new ContainerAverageCounter(player,
				(TileEntityAverageCounter) tileEntity);
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player) {
		ContainerAverageCounter containerAverageCounter = new ContainerAverageCounter(
				player, (TileEntityAverageCounter) tileEntity);
		return new GuiAverageCounter(containerAverageCounter);
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
		icons[I_INPUT] = iconRegister
				.registerIcon("nuclearcontrol:averageCounter/input");
		icons[I_OUTPUT] = iconRegister
				.registerIcon("nuclearcontrol:averageCounter/output");

	}

}
