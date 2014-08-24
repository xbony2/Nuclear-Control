package shedar.mods.ic2.nuclearcontrol.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.network.message.*;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class NuclearNetworkHelper
{
	public static final int FIELD_DOUBLE = 1;
	public static final int FIELD_INT = 2;
	public static final int FIELD_STRING = 3;
	public static final int FIELD_BOOLEAN = 4;
	public static final int FIELD_TAG = 5;
	public static final int FIELD_NULL = 6;

	//server
	public static void sendEnergyCounterValue(TileEntityEnergyCounter counter, ICrafting crafter)
	{
		if (counter == null || !(crafter instanceof EntityPlayerMP))
			return;
		ChannelHandler.network.sendTo(new PacketEncounter(counter.xCoord, counter.yCoord, counter.zCoord, counter.counter), (EntityPlayerMP)crafter);
	}

	//server
	public static void sendAverageCounterValue(TileEntityAverageCounter counter, ICrafting crafter, int average)
	{
		if (counter == null || !(crafter instanceof EntityPlayerMP))
			return;
		ChannelHandler.network.sendTo(new PacketAcounter(counter.xCoord, counter.yCoord, counter.zCoord, average), (EntityPlayerMP)crafter);
	}

	//server
	private static void sendPacketToAllAround(int x, int y, int z, int dist, World world, IMessage packet)
	{
		@SuppressWarnings("unchecked")
		List<EntityPlayerMP> players = world.playerEntities;
		for (EntityPlayerMP player : players)
		{
			double dx = x - player.posX;
			double dy = y - player.posY;
			double dz = z - player.posZ;

			if (dx*dx + dy*dy + dz*dz < dist * dist)
			{
				ChannelHandler.network.sendTo(packet, player);
			}
		}

	}

	//server
	public static void setSensorCardField(TileEntity panel, byte slot, Map<String, Object> fields)
	{
		if (fields == null || fields.isEmpty() || panel == null || !(panel instanceof TileEntityInfoPanel) || slot == -1)
			return;

		if (panel.getWorldObj().isRemote)
			return;

		sendPacketToAllAround(panel.xCoord, panel.yCoord, panel.zCoord, 64, panel.getWorldObj(),
				new PacketSensor(panel.xCoord, panel.yCoord, panel.zCoord, slot, fields));
	}

	//client
	public static void setDisplaySettings(TileEntityInfoPanel panel, byte slot, int settings)
	{
		if (panel == null)
			return;

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			return;

		ChannelHandler.network.sendToServer(new PacketClientDisplaySettings(panel.xCoord, panel.yCoord, panel.zCoord, slot, settings));
	}

	//client
	public static void setCardSettings(ItemStack card, TileEntity panelTE, Map<String, Object> fields, int slot)
	{
		if (card == null || fields == null || fields.isEmpty() || panelTE == null || !(panelTE instanceof TileEntityInfoPanel))
			return;

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			return;

		ChannelHandler.network.sendToServer(new PacketClientSensor(panelTE.xCoord, panelTE.yCoord, panelTE.zCoord, slot, card.getItem().getClass().getName(), fields));
	}

	//server
	public static void setSensorCardTitle(TileEntityInfoPanel panel, byte slot, String title)
	{
		if (title == null || panel == null)
			return;

		sendPacketToAllAround(panel.xCoord, panel.yCoord, panel.zCoord, 64, panel.getWorldObj(), new PacketSensorTitle(panel.xCoord, panel.yCoord, panel.zCoord, slot, title));
	}

	public static void chatMessage(EntityPlayer player, String message)
	{
		if (player instanceof EntityPlayerMP)
		{
			ChannelHandler.network.sendTo(new PacketChat(message), (EntityPlayerMP)player);
		}
	}

	//client
	public static void setNewAlarmSound(int x, int y, int z, byte slot, String soundName)
	{
		ChannelHandler.network.sendToServer(new PacketClientSound(x, y, z, slot, soundName));
	}

	//client
	public static void setRangeTrigger(int x, int y, int z, double value, boolean isEnd)
	{
		ChannelHandler.network.sendToServer(new PacketClientRangeTrigger(x, y, z, value, isEnd));
	}

	//client
	public static void setScreenColor(int x, int y, int z, int back, int text)
	{
		ChannelHandler.network.sendToServer(new PacketClientColor(x, y, z, (back<<4) | text));
	}

	//client
	public static void requestDisplaySettings(TileEntityInfoPanel panel)
	{
		ChannelHandler.network.sendToServer(new PacketClientRequest(panel.xCoord, panel.yCoord, panel.zCoord));
	}

	//server
	public static void sendDisplaySettingsToPlayer(int x, int y, int z, EntityPlayerMP player)
	{
		TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);
		if (!(tileEntity instanceof TileEntityInfoPanel))
			return;
		Map<Byte, Map<UUID, Integer>> settings = ((TileEntityInfoPanel)tileEntity).getDisplaySettings();
		if (settings == null)
			return;
		ChannelHandler.network.sendTo(new PacketDispSettingsAll(x, y, z, settings), (EntityPlayerMP)player);
	}

	//server
	public static void sendDisplaySettingsUpdate(TileEntityInfoPanel panel, byte slot, UUID key, int value)
	{
		sendPacketToAllAround(panel.xCoord, panel.yCoord, panel.zCoord, 64, panel.getWorldObj(), new PacketDispSettingsUpdate(panel.xCoord, panel.yCoord, panel.zCoord, slot, key, value));
	}
}