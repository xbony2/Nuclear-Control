package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridNode;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

/**
 * Created by David on 4/4/2015.
 */
public class TileEntityNetworkLink extends AENetworkInvTile {
    @Override
    public IInventory getInternalInventory() {
        return null;
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {

    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection forgeDirection) {
        return new int[0];
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord( this );
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }
    @TileEvent( TileEventType.TICK )
    public void tickingTile(){
        GridNode grid = new GridNode(this.getProxy().getNode().getGridBlock());
        NCLog.fatal(grid.getUsedChannels());
    }
}