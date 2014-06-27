package shedar.mods.ic2.nuclearcontrol.items;


import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.reactor.IReactor;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemToolDigitalThermometer extends ItemToolThermometer
    implements IElectricItem{

    public int tier;
    public int ratio;
    public int transfer;

    public ItemToolDigitalThermometer(int k, int l, int i1){
        super();
        setMaxDamage(101);
        tier = k;
        ratio = l;
        transfer = i1;
        
    }

    @Override
    public void registerIcons(IIconRegister iconRegister){
        itemIcon = iconRegister.registerIcon(TextureResolver.getItemTexture("thermometerDigital"));
    }    

    @Override
    protected boolean canTakeDamage(ItemStack itemstack, int i){
        i *= 50;
        return ElectricItem.manager.discharge(itemstack, i, 0x7fffffff, true, true) == i;
    }
    
    @Override
    protected void messagePlayer(EntityPlayer entityplayer, IReactor reactor) {
        int heat = reactor.getHeat();
        int maxHeat = reactor.getMaxHeat();
        NuclearNetworkHelper.chatMessage(entityplayer, 
                "ThermoDigital:" + heat + ":" +((maxHeat * 50) / 100) + 
                ":"+ ((maxHeat * 85) / 100));
    }
    
    @Override
    protected void damage(ItemStack itemstack, int i, EntityPlayer entityplayer)
    {
        ElectricItem.manager.use(itemstack, 50 * i, entityplayer);
    }
    
	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack) {
		return 12000;
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return tier;
	}

	@Override
	public int getTransferLimit(ItemStack itemStack) {
		return 250;
	}

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList){
        ItemStack itemstack = new ItemStack(this, 1);
        ElectricItem.manager.charge(itemstack, 0x7fffffff, 0x7fffffff, true, false);
        itemList.add(itemstack);
        itemList.add(new ItemStack(this, 1, getMaxDamage()));
    }

	@Override
	public Item getChargedItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public Item getEmptyItem(ItemStack itemStack) {
		return this;
	}
}
