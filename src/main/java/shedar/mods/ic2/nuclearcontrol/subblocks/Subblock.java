package shedar.mods.ic2.nuclearcontrol.subblocks;

import ic2.api.tile.IWrenchable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import shedar.mods.ic2.nuclearcontrol.ITextureHelper;

public abstract class Subblock
{
	protected int damage;
	protected String name;

	public Subblock(int damage, String name)
	{
		this.damage = damage;
		this.name = name;
	}

	public int getDamage()
	{
		return damage;
	}

	public String getName()
	{
		return name;
	}

	public IIcon getBlockTextureFromSide(int side)
	{
		return getIcon(getMapping()[0][side]);
	}

	public IIcon getBlockTexture(IBlockAccess blockaccess, int x, int y, int z, int side)
	{
		TileEntity tileentity = blockaccess.getTileEntity(x, y, z);
		int metaSide = 0;
		if (tileentity instanceof IWrenchable)
		{
			metaSide = Facing.oppositeSide[((IWrenchable)tileentity).getFacing()];
		}
		int texture = getMapping()[metaSide][side];

		if (tileentity instanceof ITextureHelper)
		{
			texture = ((ITextureHelper)tileentity).modifyTextureIndex(texture); 
		}
		return getIcon(texture);
	}

	public abstract IIcon getIcon(int index);
	protected abstract byte[][] getMapping();
	public abstract void registerIcons(IIconRegister iconRegister);
	public abstract TileEntity getTileEntity();
	public abstract boolean isSolidBlockRequired();
	public abstract boolean hasGui();
	public abstract float[] getBlockBounds(TileEntity tileEntity);
	public abstract Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player);
	public abstract Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player);
}