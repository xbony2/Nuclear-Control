package shedar.mods.ic2.nuclearcontrol.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.ItemStackUtils;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;

public abstract class ItemSensorKitBase extends Item {
    private String textureItemName;
    
    public ItemSensorKitBase(String textureItemName)
    {
        super();
        this.textureItemName = textureItemName;
        setMaxStackSize(1);
        setCreativeTab(IC2NuclearControl.tabIC2NC);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister){
        itemIcon = iconRegister.registerIcon(TextureResolver.getItemTexture(textureItemName));
    }   
    
    abstract protected ChunkCoordinates getTargetCoordinates(World world, int x, int y, int z, ItemStack stack);
    
    abstract protected ItemStack getItemStackByDamage(int damage);

    private void setCoordinates(ItemStack itemStack, int x, int y, int z){
        NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(itemStack);
        nbtTagCompound.setInteger("x", x);
        nbtTagCompound.setInteger("y", y);
        nbtTagCompound.setInteger("z", z);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
        if(player == null)
            return false;
        boolean isServer = player instanceof EntityPlayerMP;
        if(!isServer)
            return false;
        ChunkCoordinates position = getTargetCoordinates(world, x, y, z, stack);
        
        if(position != null){
            ItemStack sensorLocationCard = getItemStackByDamage(stack.getItemDamage());
            setCoordinates(sensorLocationCard, position.posX, position.posY, position.posZ);
            player.inventory.mainInventory[player.inventory.currentItem] = sensorLocationCard;
        	if(!world.isRemote)
        	{
        	    NuclearNetworkHelper.chatMessage(player, "SensorKit");
        	}
        	return true;
        }
        return false;
    }

}
