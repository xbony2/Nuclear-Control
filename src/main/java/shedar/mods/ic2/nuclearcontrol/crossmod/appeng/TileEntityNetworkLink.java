package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.api.AEApi;
import appeng.api.implementations.tiles.IChestOrDrive;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import appeng.tile.storage.TileChest;
import appeng.tile.storage.TileDrive;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 4/4/2015.
 */
public class TileEntityNetworkLink extends AENetworkTile {

    private static int TOTALBYTES = 0;
    private static int USEDBYTES = 0;
    private static int ITEMTYPETOTAL = 0;
    private static int USEDITEMTYPE = 0;
    private static AENetworkProxy gridProxys;

    public TileEntityNetworkLink(){
        this.gridProxy.setFlags( GridFlags.REQUIRE_CHANNEL );
        gridProxys = this.gridProxy;
    }


    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord( this );
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @TileEvent(TileEventType.TICK)
    public static void updateNetworkCache(){
        int CacheByteT = 0;
        int CacheByte = 0;
        int CacheItemT = 0;
        int CacheItem = 0;
        List<TileEntity> tileEntity = getTiles();
        NCLog.fatal("SIZE: " + tileEntity.size());
        for(int i = 0; i < tileEntity.size(); i++){
            TileEntity tile = tileEntity.get(i);
            //NCLog.error(tile);
            //NCLog.fatal("x: " + tile.xCoord + " y: " + tile.yCoord + " z: " + tile.zCoord);
            //NCLog.fatal(tileEntity.get(1).xCoord +"."+tileEntity.get(1).yCoord +"."+tileEntity.get(1).zCoord);
            if(tile instanceof TileDrive){
                TileDrive drive = (TileDrive) tile;
                //NCLog.fatal("DRIVENULL: " + drive == null);
                for( int x = 0; x < drive.getInternalInventory().getSizeInventory(); x++ ){
                    ItemStack is = drive.getInternalInventory().getStackInSlot(x);
                    //NCLog.fatal("IS NULL: " + is == null);
                    //NCLog.error(is.getItem());
                    //NCLog.error(is.getItem().getClass());
                    if(is != null){
                        IMEInventoryHandler inventory = AEApi.instance().registries().cell().getCellInventory( is, null, StorageChannel.ITEMS );
                        if(inventory instanceof ICellInventoryHandler){
                            ICellInventoryHandler handler = (ICellInventoryHandler) inventory;
                            ICellInventory cellInventory = handler.getCellInv();
                            //ICellInventory inv = (ICellInventory) is.getItem();
                            if( cellInventory != null ) {

                                CacheByteT += cellInventory.getTotalBytes();
                                CacheByte += cellInventory.getUsedBytes();
                                CacheItemT += cellInventory.getTotalItemTypes();
                                CacheItem += cellInventory.getStoredItemTypes();
                            }
                        }
                    }
                }
            } else if(tile instanceof TileChest){
                TileChest chest = (TileChest) tile;
                ItemStack is = chest.getInternalInventory().getStackInSlot(0);
                if(is != null){
                    IMEInventoryHandler inventory = AEApi.instance().registries().cell().getCellInventory( is, null, StorageChannel.ITEMS );
                    if(inventory instanceof ICellInventoryHandler){
                        ICellInventoryHandler handler = (ICellInventoryHandler) inventory;
                        ICellInventory cellInventory = handler.getCellInv();
                        if(cellInventory != null){
                            CacheByteT += cellInventory.getTotalBytes();
                            CacheByte += cellInventory.getUsedBytes();
                            CacheItemT += cellInventory.getTotalItemTypes();
                            CacheItem += cellInventory.getStoredItemTypes();
                        }
                    }
                }
            }
        }
        if(CacheByteT != TOTALBYTES)
            TOTALBYTES = CacheByteT;
        if(CacheByte != USEDBYTES)
            USEDBYTES = CacheByte;
        if(CacheItemT != ITEMTYPETOTAL)
            ITEMTYPETOTAL = CacheItemT;
        if(CacheItem != USEDITEMTYPE)
            USEDITEMTYPE = CacheItem;
        NCLog.fatal("Total: " + TOTALBYTES);
    }

    private static List<TileEntity> getTiles(){
        //List<ICellContainer> list = new ArrayList<ICellContainer>();
        List<TileEntity> list = new ArrayList<TileEntity>();

        //IGridNode gridNode = this.getGridNode(ForgeDirection.UNKNOWN);
        try {
            //IGrid grid = gridNode.getGrid();
            IGrid grid = gridProxys.getNode().getGrid();
            for (Class<? extends IGridHost> clazz : grid.getMachinesClasses()) {
                for (Class clazz2 : clazz.getInterfaces()) {
                    //NCLog.fatal("Passed Class 2");
                    //NCLog.fatal(clazz2);
                    if (clazz2 == IChestOrDrive.class) {
                        //NCLog.fatal("Passed If is IChestorDrive");
                        //NCLog.fatal(grid.getMachines(TileDrive.class));
                        for (IGridNode con : grid.getMachines(TileDrive.class)) {
                            //list.add((ICellContainer) con.getMachine());
                            list.add(getBaseTileEntity(con.getGridBlock().getLocation()));//.getMachine().getGridNode(ForgeDirection.UNKNOWN)
                        }
                    }
                }
            }
        }catch (Exception e){}
        return list;
    }
    private static TileEntity getBaseTileEntity(DimensionalCoord coord){
	    if(coord == null) {
            NCLog.fatal("Coord is null");
            return null;
        }
		World world = coord.getWorld();
		if(world == null) {
            NCLog.fatal("World is null?");
            return null;
        }
        NCLog.fatal("RETURNED Safely");
 		return world.getTileEntity(coord.x, coord.y, coord.z);
 	}

}
