package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.block.AEBaseBlock;
import net.minecraft.block.material.Material;


public class BlockNetworkLink extends AEBaseBlock {

    public BlockNetworkLink() {
        super(BlockNetworkLink.class, Material.iron);
        this.setTileEntity(TileEntityNetworkLink.class);
    }
}
