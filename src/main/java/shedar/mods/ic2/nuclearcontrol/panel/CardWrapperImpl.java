package shedar.mods.ic2.nuclearcontrol.panel;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.IRemoteSensor;
import shedar.mods.ic2.nuclearcontrol.utils.ItemStackUtils;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

public class CardWrapperImpl implements ICardWrapper
{
	private ItemStack card;
	private Map<String, Object> updateSet;
	private byte slot;

	public CardWrapperImpl(ItemStack card, int slot)
	{
		if (!(card.getItem() instanceof IPanelDataSource))
		{
			IC2NuclearControl.logger.error("CardHelper should be used for IPanelDataSource items.");
		}
		this.card = card;
		this.slot = (byte)slot;
		updateSet = new HashMap<String, Object>();
	}

	@Override
	public void setTarget(int x, int y, int z)
	{
		if (!(card.getItem() instanceof IRemoteSensor))
		{
			IC2NuclearControl.logger.warn("Trying to set coordinates [%d, %d, %d] for item which is not RemoteSensor.", x, y, z);
			return;
		}
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		nbtTagCompound.setInteger("x", x);
		nbtTagCompound.setInteger("y", y);
		nbtTagCompound.setInteger("z", z);    
	}

	@Override
	public ChunkCoordinates getTarget()
	{
		NBTTagCompound nbtTagCompound = card.getTagCompound();
		if (nbtTagCompound == null)
		{
			return null;
		}
		ChunkCoordinates coordinates  = new ChunkCoordinates();
		coordinates.posX = nbtTagCompound.getInteger("x");
		coordinates.posY = nbtTagCompound.getInteger("y");
		coordinates.posZ = nbtTagCompound.getInteger("z");  
		return coordinates;
	}

	@Override
	public void setInt(String name, Integer value)
	{
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		if (nbtTagCompound.hasKey(name))
		{
			Integer prevValue = nbtTagCompound.getInteger(name);
			if (prevValue==null || !prevValue.equals(value))
				updateSet.put(name, value);
		}
		else
		{
			updateSet.put(name, value);
		}
		nbtTagCompound.setInteger(name, value);
	}

	@Override
	public Integer getInt(String name)
	{
		NBTTagCompound nbtTagCompound = card.getTagCompound();
		if (nbtTagCompound == null)
			return 0;
		return nbtTagCompound.getInteger(name);
	}

	@Override
	public void setDouble(String name, Double value)
	{
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		if (nbtTagCompound.hasKey(name))
		{
			Double prevValue = nbtTagCompound.getDouble(name);
			if (prevValue == null || prevValue != value)
				updateSet.put(name, value);
		}
		else
		{
			updateSet.put(name, value);
		}
		nbtTagCompound.setDouble(name, value);
	}

	@Override
	public Double getDouble(String name)
	{
		NBTTagCompound nbtTagCompound = card.getTagCompound();
		if (nbtTagCompound == null)
			return 0.0;
		return nbtTagCompound.getDouble(name);
	}

	@Override
	public void setString(String name, String value)
	{
		if (name == null)
			return;
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		if (nbtTagCompound.hasKey(name))
		{
			String prevValue = nbtTagCompound.getString(name);
			if (prevValue==null || !prevValue.equals(value))
				updateSet.put(name, value);
		}
		else
		{
			updateSet.put(name, value);
		}
		nbtTagCompound.setString(name, value);
	}

	@Override
	public String getString(String name)
	{
		NBTTagCompound nbtTagCompound = card.getTagCompound();
		if (nbtTagCompound == null)
			return "";
		return nbtTagCompound.getString(name);
	}

	@Override
	public void setBoolean(String name, Boolean value)
	{
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		if (nbtTagCompound.hasKey(name))
		{
			Boolean prevValue = nbtTagCompound.getBoolean(name);
			if (prevValue==null || !prevValue.equals(value))
				updateSet.put(name, value);
		}
		else
		{
			updateSet.put(name, value);
		}
		nbtTagCompound.setBoolean(name, value);
	}

	@Override
	public Boolean getBoolean(String name)
	{
		NBTTagCompound nbtTagCompound = card.getTagCompound();
		if (nbtTagCompound == null)
			return false;
		return nbtTagCompound.getBoolean(name);
	}

	@Override
	public void setTitle(String title)
	{
		setString("title", title);
	}

	@Override
	public String getTitle()
	{
		return getString("title");
	}

	@Override
	public CardState getState()
	{
		return CardState.fromInteger(getInt("state"));
	}

	@Override
	public void setState(CardState state)
	{
		setInt("state", state.getIndex());
	}

	@Override
	public ItemStack getItemStack()
	{
		return card;
	}

	@Override
	public boolean hasField(String field)
	{
		return ItemStackUtils.getTagCompound(card).hasKey(field);
	}

	@Override
	public void commit(TileEntity panel)
	{
		if (!updateSet.isEmpty())
		{
			NuclearNetworkHelper.setSensorCardField(panel, slot, updateSet);
		}
	}

	@Override
	public void setTag(String name, NBTTagCompound value)
	{
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		if (nbtTagCompound.hasKey(name))
		{
			NBTBase prevValue = nbtTagCompound.getTag(name);
			if (prevValue==null || !prevValue.equals(value))
				updateSet.put(name, value);
		}
		else
		{
			updateSet.put(name, value);
		}
		if (value == null)
		{
			nbtTagCompound.removeTag(name);
		}
		else
		{
			nbtTagCompound.setTag(name, value);    
		}
	}

	@Override
	public NBTTagCompound getTag(String name)
	{
		NBTTagCompound nbtTagCompound = card.getTagCompound();
		if (nbtTagCompound == null)
			return null;
		return (NBTTagCompound)nbtTagCompound.getTag(name);
	}

	public void clearField(String name)
	{
		NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(card);
		nbtTagCompound.removeTag(name);
	}
}