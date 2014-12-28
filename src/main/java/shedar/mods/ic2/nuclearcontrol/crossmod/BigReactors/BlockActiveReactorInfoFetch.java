package shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;

public class BlockActiveReactorInfoFetch extends BlockContainer{

	protected BlockActiveReactorInfoFetch() {
		super(Material.iron);
		this.setCreativeTab(IC2NuclearControl.tabIC2NC);
		this.setBlockName("blockBRPart");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBlockFetcher();
	}



}
