package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.block.AEBaseBlock;
import net.minecraft.block.material.Material;

/**
 * Created by David on 4/4/2015.
 */
public class BlockNetworkLink extends AEBaseBlock {

    public BlockNetworkLink() {
        super(BlockNetworkLink.class, Material.iron);
        this.setTileEntity(TileEntityNetworkLink.class);
    }
}
