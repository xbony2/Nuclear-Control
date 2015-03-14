package shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erogenousbeef.bigreactors.common.multiblock.MultiblockReactor;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;

public class BlockActiveReactorInfoFetch extends BlockContainer{

	private static IIcon[] blockStates = new IIcon[2];//Reference to 1.8...
	private static String  textureName = "RFReactorPart";
	
	public BlockActiveReactorInfoFetch() {
		super(Material.iron);
		this.setHardness(2.0F);
		this.setCreativeTab(IC2NuclearControl.tabIC2NC);
		this.setBlockName("blockBRPart");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBlockFetcher();
	}

	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockStates[0] = iconRegister.registerIcon("nuclearcontrol:BRBlock/blockReactorPart"); //This is the Default from Big Reactors
        this.blockStates[1] = iconRegister.registerIcon("nuclearcontrol:BRBlock/" + textureName); //This is the Mod's Version of the block
    }
	
	/*
	 * Everything written beyond this point is a modified version of E-beef's Code
	 * In order to be proper: https://github.com/erogenousbeef/BigReactors/blob/master/src/main/java/erogenousbeef/bigreactors/common/multiblock/block/BlockReactorPart.java#L100
	 * 
	 */
	
	
	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		IIcon icon = null;
		int metadata = blockAccess.getBlockMetadata(x,y,z);
		
		icon = getMagicIcon(blockAccess, x, y, z, side);
		return icon != null ? icon : getIcon(side, metadata);

	}
	private IIcon getMagicIcon(IBlockAccess blockAccess, int x, int y, int z, int side){
		TileEntity te = blockAccess.getTileEntity(x, y, z);
		if(te instanceof TileEntityBlockFetcher) {
			TileEntityBlockFetcher part = (TileEntityBlockFetcher)te;
			if(!isReactorAssembled(part) || isOutwardsSide(part, side)) {
				return this.blockStates[1];
			}
		}
		return this.blockStates[0];
	}
	
	@Override
	public IIcon getIcon(int side, int metadata){
		if(side > 1&& (metadata >= 0 && metadata < blockStates.length)) {
			return this.blockStates[1];
		}
		return this.blockStates[0];
	}
	
	private boolean isOutwardsSide(TileEntityBlockFetcher part, int side) {
		ForgeDirection outDir = part.getOutwardsDir();
		return outDir.ordinal() == side;
	}
	
	private boolean isReactorAssembled(TileEntityBlockFetcher part) {
		MultiblockReactor reactor = part.getReactorController();
		return reactor != null && reactor.isAssembled();
	}
}
