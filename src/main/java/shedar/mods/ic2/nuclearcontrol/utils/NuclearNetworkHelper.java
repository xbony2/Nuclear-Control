//Thanks to Zuxelus, I'm glad one of us knows what we're doing :D
package shedar.mods.ic2.nuclearcontrol.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import shedar.mods.ic2.nuclearcontrol.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.Packet;
import shedar.mods.ic2.nuclearcontrol.PacketHandler;
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

public class NuclearNetworkHelper
{
	public static final int FIELD_LONG = 1;
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeShort(PacketHandler.PACKET_ECOUNTER);
			output.writeInt(counter.xCoord);
			output.writeInt(counter.yCoord);
			output.writeInt(counter.zCoord);
			output.writeLong(counter.counter);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToPlayer(new Packet(stream.toByteArray()),(EntityPlayerMP)crafter);
//        player.playerNetServerHandler.sendPacketToPlayer(packet);
	}

	//server
	public static void sendAverageCounterValue(TileEntityAverageCounter counter, ICrafting crafter, int average)
	{
		if (counter == null || !(crafter instanceof EntityPlayerMP))
			return;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeShort(PacketHandler.PACKET_ACOUNTER);
			output.writeInt(counter.xCoord);
			output.writeInt(counter.yCoord);
			output.writeInt(counter.zCoord);
			output.writeInt(average);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToPlayer(new Packet(stream.toByteArray()),(EntityPlayerMP)crafter);
//        player.playerNetServerHandler.sendPacketToPlayer(packet);
	}

	//server
	private static void sendPacketToAllAround(int x, int y, int z, int dist, World world, Packet packet)
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
				ChannelHandler.instance.sendToPlayer(packet, player);
				//player.playerNetServerHandler.sendPacketToPlayer(packet);
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

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeShort(PacketHandler.PACKET_SENSOR);
			output.writeInt(panel.xCoord);
			output.writeInt(panel.yCoord);
			output.writeInt(panel.zCoord);
			output.writeByte(slot);
			output.writeShort(fields.size());
			for (Map.Entry<String, Object> entry : fields.entrySet())
			{
				output.writeUTF(entry.getKey());
				Object value = entry.getValue();
				if(value instanceof Long)
				{
					output.writeByte(FIELD_LONG);
					output.writeLong((Long)value);
				}
				else if(value instanceof Integer)
				{
					output.writeByte(FIELD_INT);
					output.writeInt((Integer)value);
				}
				else if(value instanceof String)
				{
					output.writeByte(FIELD_STRING);
					output.writeUTF((String)value);
				}
				else if(value instanceof Boolean)
				{
					output.writeByte(FIELD_BOOLEAN);
					output.writeBoolean((Boolean)value);
				}
				else if(value instanceof NBTTagCompound)
				{
					output.writeByte(FIELD_TAG);
					try
					{
						CompressedStreamTools.writeCompressed((NBTTagCompound)value,output);
					} 
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else if ( value == null)
				{
					output.writeByte(FIELD_NULL);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		sendPacketToAllAround(panel.xCoord, panel.yCoord, panel.zCoord, 64, panel.getWorldObj(), new Packet(stream.toByteArray()));
	}

	//client
	public static void setDisplaySettings(TileEntityInfoPanel panel, byte slot, int settings)
	{
		if (panel == null)
			return;

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			return;

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeByte(PacketHandler.PACKET_CLIENT_DISPLAY_SETTINGS);
			output.writeInt(panel.xCoord);
			output.writeInt(panel.yCoord);
			output.writeInt(panel.zCoord);
			output.writeByte(slot);
			output.writeInt(settings);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToServer(new Packet(stream.toByteArray()));
//        FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(packet);
	}

	//client
	public static void setCardSettings(ItemStack card, TileEntity panelTE, Map<String, Object> fields, int slot)
	{
		if (card == null || fields == null || fields.isEmpty() || panelTE == null || !(panelTE instanceof TileEntityInfoPanel))
			return;

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			return;

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeByte(PacketHandler.PACKET_CLIENT_SENSOR);
			output.writeInt(panelTE.xCoord);
			output.writeInt(panelTE.yCoord);
			output.writeInt(panelTE.zCoord);
			output.writeByte(slot);
			output.writeUTF(card.getItem().getClass().getName());
			output.writeShort(fields.size());
			for (Map.Entry<String, Object> entry : fields.entrySet())
			{
				output.writeUTF(entry.getKey());
				Object value = entry.getValue();
				if (value instanceof Long)
				{
					output.writeByte(FIELD_LONG);
					output.writeLong((Long)value);
				}
				else if (value instanceof Integer)
				{
					output.writeByte(FIELD_INT);
					output.writeInt((Integer)value);
				}
				else if (value instanceof String)
				{
					output.writeByte(FIELD_STRING);
					output.writeUTF((String)value);
				}
				else if (value instanceof Boolean)
				{
					output.writeByte(FIELD_BOOLEAN);
					output.writeBoolean((Boolean)value);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToServer(new Packet(stream.toByteArray()));
//        FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(packet);
	}

	//server
	public static void setSensorCardTitle(TileEntityInfoPanel panel, byte slot, String title)
	{
		if (title == null || panel == null)
			return;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeShort(PacketHandler.PACKET_SENSOR_TITLE);
			output.writeInt(panel.xCoord);
			output.writeInt(panel.yCoord);
			output.writeInt(panel.zCoord);
			output.writeByte(slot);
			output.writeUTF(title);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		sendPacketToAllAround(panel.xCoord, panel.yCoord, panel.zCoord, 64, panel.getWorldObj(), new Packet(stream.toByteArray()));
	}

	public static void chatMessage(EntityPlayer player, String message)
	{
		if (player instanceof EntityPlayerMP)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(stream);
			try
			{
				output.writeShort(PacketHandler.PACKET_CHAT);
				output.writeUTF(message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			ChannelHandler.instance.sendToPlayer(new Packet(stream.toByteArray()),(EntityPlayerMP)player);
//            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
		}
	}

	//client
	public static void setNewAlarmSound(int x, int y, int z, byte slot, String soundName)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeByte(PacketHandler.PACKET_CLIENT_SOUND);
			output.writeInt(x);
			output.writeInt(y);
			output.writeInt(z);
			output.writeByte(slot);
			output.writeUTF(soundName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToServer(new Packet(stream.toByteArray()));
//        NetClientHandler netHandler = FMLClientHandler.instance().getClient().getNetHandler();
	}

	//client
	public static void setRangeTrigger(int x, int y, int z, long value, boolean isEnd)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeByte(PacketHandler.PACKET_CLIENT_RANGE_TRIGGER);
			output.writeInt(x);
			output.writeInt(y);
			output.writeInt(z);
			output.writeLong(value);
			output.writeBoolean(isEnd);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToServer(new Packet(stream.toByteArray()));
//        FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(packet);*/
	}

	//client
	public static void setScreenColor(int x, int y, int z, int back, int text)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeByte(PacketHandler.PACKET_CLIENT_COLOR);
			output.writeInt(x);
			output.writeInt(y);
			output.writeInt(z);
			output.writeInt((back<<4) | text);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToServer(new Packet(stream.toByteArray()));
//        FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(packet);
	}

	//client
	public static void requestDisplaySettings(TileEntityInfoPanel panel)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeByte(PacketHandler.PACKET_CLIENT_REQUEST);
			output.writeInt(panel.xCoord);
			output.writeInt(panel.yCoord);
			output.writeInt(panel.zCoord);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToServer(new Packet(stream.toByteArray()));
//        FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(packet);
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeShort(PacketHandler.PACKET_DISP_SETTINGS_ALL);
			output.writeInt(x);
			output.writeInt(y);
			output.writeInt(z);
			output.writeByte(settings.size());
			for (Map.Entry<Byte, Map<UUID, Integer>> slotData : settings.entrySet())
			{
				output.writeByte(slotData.getKey());
				output.writeShort(slotData.getValue().size());
				for(Map.Entry<UUID, Integer> item: slotData.getValue().entrySet())
				{
					UUID key = item.getKey(); 
					if (key == null)
						continue;
					output.writeLong(key.getMostSignificantBits());
					output.writeLong(key.getLeastSignificantBits());
					output.writeInt(item.getValue());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToPlayer(new Packet(stream.toByteArray()),(EntityPlayerMP)player);
//        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
	}

	//server
	public static void sendDisplaySettingsUpdate(TileEntityInfoPanel panel, byte slot, UUID key, int value)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try
		{
			output.writeShort(PacketHandler.PACKET_DISP_SETTINGS_UPDATE);
			output.writeInt(panel.xCoord);
			output.writeInt(panel.yCoord);
			output.writeInt(panel.zCoord);
			output.writeByte(slot);
			output.writeLong(key.getMostSignificantBits());
			output.writeLong(key.getLeastSignificantBits());
			output.writeInt(value);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		sendPacketToAllAround(panel.xCoord, panel.yCoord, panel.zCoord, 64, panel.getWorldObj(), new Packet(stream.toByteArray()));
	}
}