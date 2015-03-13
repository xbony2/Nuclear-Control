package shedar.mods.ic2.nuclearcontrol.crossmod.RF;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CrossRF {

	public boolean _RFModPresent = false;
	
	
	public CrossRF(){
		if(Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("BuildCraft")){
			_RFModPresent = true;
			registerTiles();
		} else {
			_RFModPresent = false;
		}
	}
	
	public void registerTiles(){
		GameRegistry.registerTileEntity(RFTileEntityAverageCounter.class, "IC2NCAverageCounterRF");
		GameRegistry.registerTileEntity(RFTileEntityEnergyCounter.class, "IC2NCEnergyCounterRF");
	}
	
    public TileEntityAverageCounter getAverageCounter(){
        if(_RFModPresent){
            try{
                return (TileEntityAverageCounter)Class.forName("shedar.mods.ic2.nuclearcontrol.crossmod.RF.RFTileEntityAverageCounter").newInstance();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public TileEntityEnergyCounter getEnergyCounter(){
        if(_RFModPresent){
            try{
                return (TileEntityEnergyCounter)Class.forName("shedar.mods.ic2.nuclearcontrol.crossmod.RF.RFTileEntityEnergyCounter").newInstance();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public EnergyStorageData getStorageData(TileEntity target)
    {
        if(!_RFModPresent || target == null)
            return null;
        RFTileEntityAverageCounter tile = new RFTileEntityAverageCounter();
        if(tile.storage == null)
            return null;
        EnergyStorageData result = new EnergyStorageData();
        result.capacity = tile.storage.getMaxEnergyStored();
        result.stored = tile.storage.getEnergyStored();
        result.units = EnergyStorageData.UNITS_RF;
        result.type = EnergyStorageData.TARGET_TYPE_RF;
        return result;
    }
}
