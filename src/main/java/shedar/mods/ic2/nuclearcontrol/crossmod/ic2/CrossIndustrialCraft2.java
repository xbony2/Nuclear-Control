package shedar.mods.ic2.nuclearcontrol.crossmod.ic2;

import java.lang.reflect.Method;
import java.util.Arrays;

import ic2.api.item.IC2Items;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.crossmod.data.EnergyStorageData;

public class CrossIndustrialCraft2{

    private ItemStack _IC2WrenchId;
    private ItemStack _IC2ElectricWrenchId;
    
    private Method _getMaxDamageEx; 
    private Method _getDamageOfStack; 
    private Class _gradItemInt;
    
    private boolean _isApiAvailable = false;
    private boolean _isIdInitialized = false;
    
    private ItemStack[] _fuelIds = null;
    
    private void initIds(){
        if(!_isApiAvailable || _isIdInitialized)
            return;
        _fuelIds = new ItemStack[6];
        _fuelIds[0] = IC2Items.getItem("reactorUraniumSimple");
        _fuelIds[1] = IC2Items.getItem("reactorUraniumDual");
        _fuelIds[2] = IC2Items.getItem("reactorUraniumQuad");

        _fuelIds[3] = IC2Items.getItem("reactorMOXSimple");
        _fuelIds[4] = IC2Items.getItem("reactorMOXDual");
        _fuelIds[5] = IC2Items.getItem("reactorMOXQuad");

        Arrays.sort(_fuelIds);
        
        _IC2WrenchId = IC2Items.getItem("wrench");
        _IC2ElectricWrenchId = IC2Items.getItem("electricWrench");
        _isIdInitialized = true;
    }
    
    public boolean isApiAvailable(){
        return _isApiAvailable;
    }
    
    public boolean isWrench(ItemStack itemStack){
        initIds();
        return _isApiAvailable && (itemStack == _IC2WrenchId || itemStack ==_IC2ElectricWrenchId);
    }
    
    @SuppressWarnings("unchecked")
    public CrossIndustrialCraft2(){
        try{
            Class.forName("ic2.api.tile.IEnergyStorage", false, this.getClass().getClassLoader());
            _gradItemInt = Class.forName("ic2.core.item.ItemGradualInt", false, this.getClass().getClassLoader());
            _getMaxDamageEx = _gradItemInt.getMethod("getMaxDamageEx");
            _getDamageOfStack = _gradItemInt.getMethod("getDamageOfStack", ItemStack.class);
            _isApiAvailable = true;
        } catch (Exception e){
            _isApiAvailable = false;
        }
    }

    public EnergyStorageData getStorageData(TileEntity target){
        if(!_isApiAvailable || target == null)
            return null;
        if(target instanceof IEnergyStorage){
            IEnergyStorage storage = (IEnergyStorage)target;
            EnergyStorageData result = new EnergyStorageData();
            result.capacity = storage.getCapacity();
            result.stored = storage.getStored();
            result.units = "EU";
            result.type = EnergyStorageData.TARGET_TYPE_IC2;
            return result;
        }
        return null;
    }
    
    public int getNuclearCellTimeLeft(ItemStack stack){
        if(!_isApiAvailable || stack == null)
            return -1;
        initIds();
        if(Arrays.binarySearch(_fuelIds, stack)>=0){
            int delta;
            try{
                int maxDamage = (Integer)_getMaxDamageEx.invoke( stack.getItem());
                int damage = (Integer)_getDamageOfStack.invoke(stack.getItem(), stack);
                delta = maxDamage - damage;
            }catch(Exception e){
                delta = stack.getMaxDamage() - stack.getItemDamage(); 
            }
            return delta;
        }
        return -1;
    }
    
}
