package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.block.AEBaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;


public class BlockNetworkLink extends AEBaseBlock {

    public BlockNetworkLink() {
        super(BlockNetworkLink.class, Material.iron);
        this.setTileEntity(TileEntityNetworkLink.class);
        this.setBlockName("NetworkLink");
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta){
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof TileEntityNetworkLink){
            NCLog.error("WORKED?");
            TileEntityNetworkLink networkLink = (TileEntityNetworkLink) tile;
            networkLink.updateNetworkCache();
        }
    }
}
