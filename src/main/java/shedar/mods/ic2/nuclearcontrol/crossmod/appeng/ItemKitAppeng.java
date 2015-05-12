package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.block.crafting.BlockCraftingMonitor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitEnergySensor;
import shedar.mods.ic2.nuclearcontrol.utils.ItemStackUtils;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

public class ItemKitAppeng extends ItemKitEnergySensor{

    public ItemKitAppeng() {
        this.setTextureName("nuclearcontrol:kitAEMonitor");
        this.setUnlocalizedName("AppengKit");
    }

    @Override
    protected ItemStack getItemStackByDamage(int damage) {
        return new ItemStack(CrossAppeng.cardAppeng, 1, 0);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player == null)
            return false;
        boolean isServer = player instanceof EntityPlayerMP;
        if (!isServer)
            return false;

        Block theBlock = world.getBlock(x, y, z);
        if (theBlock == CrossAppeng.networklink) {
            ItemStack sensorLocationCard = getItemStackByDamage(stack.getItemDamage());

            NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(sensorLocationCard);
            nbtTagCompound.setInteger("x", x);
            nbtTagCompound.setInteger("y", y);
            nbtTagCompound.setInteger("z", z);
            nbtTagCompound.setInteger("targetType", 1);

            player.inventory.mainInventory[player.inventory.currentItem] = sensorLocationCard;
            if (!world.isRemote) {
                NuclearNetworkHelper.chatMessage(player, "SensorKit");
            }
            return true;
        } else if(theBlock instanceof BlockCraftingMonitor/*AEApi.instance().definitions().blocks().craftingMonitor()*/){
            ItemStack sensorLocationCard = getItemStackByDamage(stack.getItemDamage());

            NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(sensorLocationCard);
            nbtTagCompound.setInteger("x", x);
            nbtTagCompound.setInteger("y", y);
            nbtTagCompound.setInteger("z", z);
            nbtTagCompound.setInteger("targetType", 2);

            player.inventory.mainInventory[player.inventory.currentItem] = sensorLocationCard;
            if (!world.isRemote) {
                NuclearNetworkHelper.chatMessage(player, "SensorKit");
            }
            return true;
        }

        return false;
    }
}