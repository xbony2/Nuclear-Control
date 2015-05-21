package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;

import appeng.tile.networking.TileCableBus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockNetworkLink extends BlockContainer {

    private IIcon face, side;

    public BlockNetworkLink() {
        super(Material.iron);
        this.setBlockName("NetworkLink");
        this.setBlockHardness(1.0F);
        this.setCreativeTab(IC2NuclearControl.tabIC2NC);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityNetworkLink();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registrar){
        this.face = registrar.registerIcon("nuclearcontrol:aeMonitor/monitorFace");
        this.side = registrar.registerIcon("nuclearcontrol:aeMonitor/monitorSide");
        this.blockIcon = registrar.registerIcon("nuclearcontrol:aeMonitor/monitorSide");
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        TileEntity tile = world.getTileEntity( x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
        //NCLog.fatal((x+dir.offsetX) + " " + (y+dir.offsetY) + " " + (z+dir.offsetZ));
        if(tile instanceof TileCableBus)
            return this.face;
        return this.side;
    }

}
