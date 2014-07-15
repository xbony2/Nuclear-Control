/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IC2NCCreativeTabs extends CreativeTabs{
  private static ItemStack itemToolThermometer;
  
  public IC2NCCreativeTabs(){
    super("IC2 NuclearControl 2");
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack getIconItemStack(){
    if (itemToolThermometer == null) {
    	itemToolThermometer = new ItemStack(IC2NuclearControl.instance.itemToolThermometer);
    }
    return itemToolThermometer;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public Item getTabIconItem(){
    return IC2NuclearControl.instance.itemToolThermometer;
  }
  
  @Override
  public String getTranslatedTabLabel()
  {
    return "IC2 NuclearControl 2";
  }
  
}