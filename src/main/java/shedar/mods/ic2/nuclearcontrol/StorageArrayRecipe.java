package shedar.mods.ic2.nuclearcontrol;

import ic2.api.item.IC2Items;

import java.util.Vector;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergyArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardLiquidArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardMultipleSensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitMultipleSensor;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;

public class StorageArrayRecipe implements IRecipe
{

	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		return getCraftingResult(inventory) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory)
	{
		int inventoryLength = inventory.getSizeInventory();
		boolean fail = false;
		int cardCount = 0;
		int arrayCount = 0;
		int cardCountLiquid = 0;
		int arrayCountLiquid = 0;
		ItemStack array = null;
		Vector<ItemStack> cards = new Vector<ItemStack>();
		for (int i = 0; i < inventoryLength; i++)
		{
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null)
				continue;
			if (itemStack.getItem() instanceof ItemCardEnergySensorLocation)
			{
				cards.add(itemStack);
				cardCount++;
			} 
			else if (itemStack.getItem() instanceof ItemCardMultipleSensorLocation && itemStack.getItemDamage() == ItemKitMultipleSensor.TYPE_LIQUID)
			{
				cards.add(itemStack);
				cardCountLiquid++;
			}
			else if (itemStack.getItem() instanceof ItemCardEnergyArrayLocation)
			{
				array = itemStack;
				arrayCount++;
			}
			else if (itemStack.getItem() instanceof ItemCardLiquidArrayLocation)
			{
				array = itemStack;
				arrayCountLiquid++;
			}
			else
			{
				fail = true;
				break;
			}
		}
		if (fail || (cardCount != 0 && cardCountLiquid != 0) || (arrayCount != 0 && arrayCountLiquid != 0) ||
				(cardCount != 0 && arrayCountLiquid != 0) || (arrayCount != 0 && cardCountLiquid != 0))
		{
			return null;
		}
		if (cardCount >= 2 && cardCount <= 6 && arrayCount == 0)
		{
			ItemStack itemStack = new ItemStack(IC2NuclearControl.instance.itemEnergyArrayLocationCard, 1, 0);
			ItemCardEnergyArrayLocation.initArray(new CardWrapperImpl(itemStack, -1), cards);
			return itemStack;
		}
		else if (cardCount == 0 && arrayCount == 1)
		{
			int cnt = ItemCardEnergyArrayLocation.getCardCount(new CardWrapperImpl(array, -1));
			if (cnt > 0)
			{
				return new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2*cnt, 0);
			}
		}
		else if (arrayCount == 1 && cardCount > 0)
		{
			int cnt = ItemCardEnergyArrayLocation.getCardCount(new CardWrapperImpl(array, -1));
			if (cnt + cardCount <= 6)
			{
				ItemStack itemStack = new ItemStack(IC2NuclearControl.instance.itemEnergyArrayLocationCard, 1, 0);
				itemStack.setTagCompound((NBTTagCompound)array.getTagCompound().copy());
				ItemCardEnergyArrayLocation.initArray(new CardWrapperImpl(itemStack, -1), cards);
				return itemStack;
			}
		}
		
		if (cardCountLiquid >= 2 && cardCountLiquid <= 6 && arrayCountLiquid == 0)
		{
			ItemStack itemStack = new ItemStack(IC2NuclearControl.instance.itemLiquidArrayLocationCard, 1, 0);
			ItemCardLiquidArrayLocation.initArray(new CardWrapperImpl(itemStack, -1), cards);
			return itemStack;
		}
		else if (cardCountLiquid == 0 && arrayCountLiquid == 1)
		{
			int cnt = ItemCardLiquidArrayLocation.getCardCount(new CardWrapperImpl(array, -1));
			if (cnt > 0)
			{
				return new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), cnt, 0);
			}
		}
		else if (arrayCountLiquid == 1 && cardCountLiquid > 0)
		{
			int cnt = ItemCardLiquidArrayLocation.getCardCount(new CardWrapperImpl(array, -1));
			if (cnt + cardCount <= 6)
			{
				ItemStack itemStack = new ItemStack(IC2NuclearControl.instance.itemLiquidArrayLocationCard, 1, 0);
				itemStack.setTagCompound((NBTTagCompound)array.getTagCompound().copy());
				ItemCardLiquidArrayLocation.initArray(new CardWrapperImpl(itemStack, -1), cards);
				return itemStack;
			}
		}
		return null;
	}

	@Override
	public int getRecipeSize()
	{
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
}