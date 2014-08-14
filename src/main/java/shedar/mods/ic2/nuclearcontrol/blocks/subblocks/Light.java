package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityLight;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class Light extends Subblock{

	private static final int DAMAGE = Damages.DAMAGE_LIGHT;
	
	public Light() {
		super(DAMAGE, "tile.blockLight");
		
	}

	@Override
	public IIcon getIcon(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected byte[][] getMapping() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TileEntity getTileEntity(){
		TileEntity entity = new TileEntityLight();
		return entity;
	}

	@Override
	public boolean isSolidBlockRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasGui() {
		return false;
	}

	@Override
	public float[] getBlockBounds(TileEntity tileEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getServerGuiElement(TileEntity tileEntity,
			EntityPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

}
