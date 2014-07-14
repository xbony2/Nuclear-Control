package shedar.mods.ic2.nuclearcontrol.crossmod.ic2;

import java.lang.reflect.Method;
import java.util.Arrays;

import ic2.api.item.IC2Items;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.crossmod.data.EnergyStorageData;

public class CrossIndustrialCraft2{

    private ItemStack IC2Wrench;
    private ItemStack IC2ElectricWrench;
    
    private Method getMaxDamageEx; 
    private Method getDamageOfStack; 
    private Class gradItemInt;
    
    private boolean isApiAvailable = false;
    private boolean isIdInitialized = false;
    
    private ItemStack[] fuelItems = null;
    
    private void initIds(){
        if(!isApiAvailable || isIdInitialized)
            return;
        fuelItems = new ItemStack[6];
        fuelItems[0] = IC2Items.getItem("reactorUraniumSimple");
        fuelItems[1] = IC2Items.getItem("reactorUraniumDual");
        fuelItems[2] = IC2Items.getItem("reactorUraniumQuad");

        fuelItems[3] = IC2Items.getItem("reactorMOXSimple");
        fuelItems[4] = IC2Items.getItem("reactorMOXDual");
        fuelItems[5] = IC2Items.getItem("reactorMOXQuad");

        //Arrays.sort(fuelItems); //TODO cannot do this (thanks to @Zuxelus for the pointer)
        
        IC2Wrench = IC2Items.getItem("wrench");
        IC2ElectricWrench = IC2Items.getItem("electricWrench");
        isIdInitialized = true;
    }
    
    public boolean isApiAvailable(){
        return isApiAvailable;
    }
    
    public boolean isWrench(ItemStack itemStack){
        initIds();
        return isApiAvailable && (itemStack == IC2Wrench || itemStack ==IC2ElectricWrench);
    }
    
    @SuppressWarnings("unchecked")
    public CrossIndustrialCraft2(){
        try{
            Class.forName("ic2.api.tile.IEnergyStorage", false, this.getClass().getClassLoader());
            gradItemInt = Class.forName("ic2.core.item.ItemGradualInt", false, this.getClass().getClassLoader());
            getMaxDamageEx = gradItemInt.getMethod("getMaxDamageEx");
            getDamageOfStack = gradItemInt.getMethod("getDamageOfStack", ItemStack.class);
            isApiAvailable = true;
        }catch (Exception e){
            isApiAvailable = false;
        }
    }

    public EnergyStorageData getStorageData(TileEntity target){
        if(!isApiAvailable || target == null)
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
        if(!isApiAvailable || stack == null)
            return -1;
        initIds();
        if(Arrays.binarySearch(fuelItems, stack)>=0){
            int delta;
            try{
                int maxDamage = (Integer)getMaxDamageEx.invoke(stack.getItem());
                int damage = (Integer)getDamageOfStack.invoke(stack.getItem(), stack);
                delta = maxDamage - damage;
            }catch(Exception e){
                delta = stack.getMaxDamage() - stack.getItemDamage(); 
            }
            return delta;
        }
        return -1;
    }
    
}
