package shedar.mods.ic2.nuclearcontrol.items;

import java.util.List;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.IIcon;


public class ItemUpgrade extends Item
{
    public static final int DAMAGE_RANGE = 0;
    public static final int DAMAGE_COLOR = 1;
    public static final int DAMAGE_WEB = 2;
    
    private static final String TEXTURE_RANGE = "upgradeRange"; 
    private static final String TEXTURE_COLOR = "upgradeColor"; 
    private static final String TEXTURE_WEB = "upgradeWeb"; 
    
    private IIcon iconRange;
    private IIcon iconColor;
    private IIcon iconWeb;

    public ItemUpgrade(){
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister){
        iconRange = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_RANGE));
        iconColor = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_COLOR));
        iconWeb = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_WEB));
    }    
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack) 
    {
        int damage = itemStack.getItemDamage();
        switch (damage)
        {
        case DAMAGE_RANGE:
            return "item.ItemRangeUpgrade";
        case DAMAGE_COLOR:
            return "item.ItemColorUpgrade";
        case DAMAGE_WEB:
            return "item.ItemWebUpgrade";
        default:
            return "";
        }
    }
    
    @Override
    public IIcon getIconFromDamage(int damage)
    {
        switch (damage)
        {
        case DAMAGE_RANGE:
            return iconRange;
        case DAMAGE_COLOR:
            return iconColor;
        case DAMAGE_WEB:
            return iconWeb;
        default:
            return iconRange;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList)
    {
        itemList.add(new ItemStack(par1, 1, DAMAGE_RANGE));
        itemList.add(new ItemStack(par1, 1, DAMAGE_COLOR));
        if(IC2NuclearControl.instance.isHttpSensorAvailable)
            itemList.add(new ItemStack(par1, 1, DAMAGE_WEB));
    }
}
