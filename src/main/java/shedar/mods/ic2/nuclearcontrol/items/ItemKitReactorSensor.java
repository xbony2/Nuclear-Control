package shedar.mods.ic2.nuclearcontrol.items;

import ic2.api.item.IC2Items;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.ItemStackUtils;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearHelper;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

public class ItemKitReactorSensor extends ItemSensorKitBase {

	public ItemKitReactorSensor() {
		super("kitReactor");
	}

	@Override
	protected ChunkCoordinates getTargetCoordinates(World world, int x, int y, int z, ItemStack stack) {
		IReactor reactor = NuclearHelper.getReactorAt(world, x, y, z);
		if (reactor == null) {
			IReactorChamber chamber = NuclearHelper.getReactorChamberAt(world, x, y, z);
			if (chamber != null) {
				reactor = chamber.getReactor();
			}
		}
		if (reactor != null)
			return reactor.getPosition();
		return null;
	}

	
	protected ItemStack getItemStackByDamage(int cardID) {
		if(cardID == 0)
			return new ItemStack(IC2NuclearControl.itemSensorLocationCard, 1, 0);
		if(cardID == 1)
			return new ItemStack(IC2NuclearControl.item55ReactorCard, 1, 0);
		else
			return null;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (player == null)
			return false;
		boolean isServer = player instanceof EntityPlayerMP;
		if (!isServer)
			return false;
		
		ChunkCoordinates position = this.getTargetCoordinates(world, x, y, z, stack);
		int sendInt = 0;
		if (position != null) {
			
			ItemStack sensorLocationCard = getItemStackByDamage(sendInt);
			setCoordinates(sensorLocationCard, position.posX, position.posY, position.posZ);
			player.inventory.mainInventory[player.inventory.currentItem] = sensorLocationCard;
			if (!world.isRemote) {
				NuclearNetworkHelper.chatMessage(player, "SensorKit");
			}
			return true;
		} else if(position == null){
			Block check = world.getBlock(x, y, z);
			if(check == Block.getBlockFromItem(IC2Items.getItem("reactorRedstonePort").getItem())|| check == Block.getBlockFromItem(Ic2Items.reactorvessel.getItem())|| check == Block.getBlockFromItem(Ic2Items.reactorAccessHatch.getItem())|| check == Block.getBlockFromItem(Ic2Items.reactorFluidPort.getItem())){
				sendInt = 1;
				ItemStack sensorLocationCard = getItemStackByDamage(sendInt);
				setCoordinates(sensorLocationCard, x, y, z);
				player.inventory.mainInventory[player.inventory.currentItem] = sensorLocationCard;
				if (!world.isRemote) {
					NuclearNetworkHelper.chatMessage(player, "SensorKit");
				}
				return true;
			}
		}
		return false;
	}
	
	private void setCoordinates(ItemStack itemStack, int x, int y, int z) {
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(itemStack);
		nbtTagCompound.setInteger("x", x);
		nbtTagCompound.setInteger("y", y);
		nbtTagCompound.setInteger("z", z);
	}
}
