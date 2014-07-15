/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger;
import shedar.mods.ic2.nuclearcontrol.utils.LanguageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<Packet> 
{
	public static ChannelHandler instance = new ChannelHandler();	

	public ChannelHandler() 
	{		
		addDiscriminator(0, Packet.class);
	}	

	@Override
	public void encodeInto(ChannelHandlerContext ctx, Packet msg, ByteBuf target) throws Exception 
	{
		target.writeBytes(msg.data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, Packet msg) 
	{
		switch (FMLCommonHandler.instance().getEffectiveSide()) 
		{
		case CLIENT:
			this.onPacketDataClient(source, ClientProxy.getPlayer());
			break;
		case SERVER:
			NetHandlerPlayServer netHandler = (NetHandlerPlayServer)(ctx.channel().attr(NetworkRegistry.NET_HANDLER).get());
			this.onPacketData(source, netHandler.playerEntity);
			break;
		}		
	}

	public static void sendToServer(Packet packet)
	{
		IC2NuclearControl.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		IC2NuclearControl.channels.get(Side.CLIENT).writeOutbound(packet);
	}

	public static void sendToPlayer(Packet packet, EntityPlayer player)
	{
		IC2NuclearControl.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		IC2NuclearControl.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		IC2NuclearControl.channels.get(Side.SERVER).writeOutbound(packet);
	}
/*
	public static void sendToAllPlayers(Packet packet)
	{
		IC2NuclearControl.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		IC2NuclearControl.channels.get(Side.SERVER).writeOutbound(packet);
	}	
*/
// Function onPacketData from CommonProxy.java
	public void onPacketData(ByteBuf source, EntityPlayer entityPlayer)
	{	
		try
		{
			DataInputStream dat = new DataInputStream(new ByteArrayInputStream(Arrays.copyOfRange(source.array(), 1, source.array().length)));
			byte packetId = dat.readByte();
			int x = dat.readInt();
			int y = dat.readInt();
			int z = dat.readInt();
			switch (packetId)
			{
			case PacketHandler.PACKET_CLIENT_SOUND:
				byte slot = dat.readByte();
				String soundName = dat.readUTF();
				TileEntity tileEntity = entityPlayer.worldObj.getTileEntity(x, y, z);
				if (tileEntity instanceof TileEntityHowlerAlarm)
				{
					((TileEntityHowlerAlarm) tileEntity).setSoundName(soundName);
				} 
				else if (tileEntity instanceof TileEntityInfoPanel)
				{
					ItemStack stack = ((TileEntityInfoPanel) tileEntity).getStackInSlot(slot);
					if (stack == null || !(stack.getItem() instanceof IPanelDataSource))
					{
						return;
					}
					new CardWrapperImpl(stack, -1).setTitle(soundName);
					NuclearNetworkHelper.setSensorCardTitle((TileEntityInfoPanel)tileEntity, slot, soundName);
				}
				break;
			case PacketHandler.PACKET_CLIENT_REQUEST:
				NuclearNetworkHelper.sendDisplaySettingsToPlayer(x, y, z, (EntityPlayerMP)entityPlayer);
				break;
			case PacketHandler.PACKET_CLIENT_RANGE_TRIGGER:
				long value = dat.readLong();
				boolean isEnd = dat.readBoolean();
				tileEntity = entityPlayer.worldObj.getTileEntity(x, y, z);
				if (tileEntity instanceof TileEntityRangeTrigger)
				{
					if (isEnd)
					{
						((TileEntityRangeTrigger)tileEntity).setLevelEnd(value);
					}
					else
					{
						((TileEntityRangeTrigger)tileEntity).setLevelStart(value);
					}
				}
				break;
			case PacketHandler.PACKET_CLIENT_COLOR:
				int colors= dat.readInt();
				tileEntity = entityPlayer.worldObj.getTileEntity(x, y, z);
				if (tileEntity instanceof TileEntityInfoPanel)
				{
					int back = colors >> 4;
					int text = colors & 0xf;
					((TileEntityInfoPanel)tileEntity).setColorBackground(back);
					((TileEntityInfoPanel)tileEntity).setColorText(text);
				}
				break;
			case PacketHandler.PACKET_CLIENT_DISPLAY_SETTINGS:
				slot = dat.readByte();
				int settings = dat.readInt();
				tileEntity = entityPlayer.worldObj.getTileEntity(x, y, z);
				if (tileEntity instanceof TileEntityInfoPanel)
				{
					((TileEntityInfoPanel)tileEntity).setDisplaySettings(slot, settings);
				}
				break;
			case PacketHandler.PACKET_CLIENT_SENSOR:
				tileEntity = entityPlayer.worldObj.getTileEntity(x, y, z);
				if (tileEntity instanceof TileEntityInfoPanel)
				{
					TileEntityInfoPanel panel = (TileEntityInfoPanel) tileEntity;
					slot = dat.readByte();
					ItemStack stack = panel.getStackInSlot(slot);
					if (stack == null || !(stack.getItem() instanceof IPanelDataSource))
					{
						return;
					}
					String className = dat.readUTF();
					if (!stack.getItem().getClass().getName().equals(className))
					{
						FMLLog.warning("Class mismatch: '%s'!='%s'", className, stack.getItem().getClass().getName());
						return;
					}
					CardWrapperImpl helper = new CardWrapperImpl(stack, slot);
					int fieldCount =  dat.readShort();
					for (int i = 0; i < fieldCount; i++)
					{
						String name = dat.readUTF();
						byte type = dat.readByte();
						switch (type)
						{
						case NuclearNetworkHelper.FIELD_INT:
							helper.setInt(name, dat.readInt());
							break;
						case NuclearNetworkHelper.FIELD_BOOLEAN:
							helper.setBoolean(name, dat.readBoolean());
							break;
						case NuclearNetworkHelper.FIELD_LONG:
							helper.setLong(name, dat.readLong());
							break;
						case NuclearNetworkHelper.FIELD_STRING:
							helper.setString(name, dat.readUTF());
							break;
						default:
							FMLLog.warning("Invalid field type: %d", type);
							break;
						}
					}
					helper.commit(panel);
				}
				break;

			default:
				break;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

// Function onPacketData from ClientProxy.java
	public void onPacketDataClient(ByteBuf source, EntityPlayer entityPlayer)
	{		
		World world;
		int x,y,z;
		TileEntity ent;
		TileEntityInfoPanel panel;
		try
		{
			DataInputStream dat = new DataInputStream(new ByteArrayInputStream(Arrays.copyOfRange(source.array(), 1, source.array().length)));        
			short packetType = dat.readShort();
			switch (packetType)
			{
			case PacketHandler.PACKET_CHAT:
				String message = dat.readUTF();
				String[] chunks = message.split(":");
				message = LanguageHelper.translate("msg.nc." + chunks[0]);
				if (chunks.length > 1)
				{
					List<String> list = new ArrayList<String>(Arrays.asList(chunks));
					list.remove(0);
					chunks = list.toArray(chunks);
					message = String.format(message, (Object[])chunks);
				}
				entityPlayer.addChatComponentMessage(new ChatComponentText(message));
				break;
			case PacketHandler.PACKET_ALARM:
				IC2NuclearControl.instance.maxAlarmRange = dat.readInt();
				IC2NuclearControl.instance.serverAllowedAlarms = new ArrayList<String>(Arrays.asList(dat.readUTF().split(",")));
				break;
			case PacketHandler.PACKET_SENSOR:
				world = FMLClientHandler.instance().getClient().theWorld;
				x = dat.readInt();
				y = dat.readInt();
				z = dat.readInt();
				ent = world.getTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityInfoPanel))
				{
					return;
				}
				panel = (TileEntityInfoPanel)ent;
				byte slot =  dat.readByte();
				ItemStack stack = panel.getStackInSlot(slot);
				if (stack == null || !(stack.getItem() instanceof IPanelDataSource))
				{
					return;
				}
				CardWrapperImpl helper = new CardWrapperImpl(stack, slot);
				int fieldCount =  dat.readShort();
				for (int i = 0; i < fieldCount; i++)
				{
					String name = dat.readUTF();
					byte type = dat.readByte();
					switch (type)
					{
					case NuclearNetworkHelper.FIELD_INT:
						helper.setInt(name, dat.readInt());
						break;
					case NuclearNetworkHelper.FIELD_BOOLEAN:
						helper.setBoolean(name, dat.readBoolean());
						break;
					case NuclearNetworkHelper.FIELD_LONG:
						helper.setLong(name, dat.readLong());
						break;
					case NuclearNetworkHelper.FIELD_STRING:
						helper.setString(name, dat.readUTF());
						break;
					case NuclearNetworkHelper.FIELD_TAG:
						NBTTagCompound tag;
						try
						{
							tag = CompressedStreamTools.readCompressed(dat);
							helper.setTag(name, tag);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						break;
					case NuclearNetworkHelper.FIELD_NULL:
						helper.clearField(name);
						break;
					default:
						FMLLog.warning("Invalid field type: %d", type);
						break;
					}
				}
				panel.resetCardData();
				break;
			case PacketHandler.PACKET_SENSOR_TITLE:
				world = FMLClientHandler.instance().getClient().theWorld;
				x = dat.readInt();
				y = dat.readInt();
				z = dat.readInt();
				ent = world.getTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityInfoPanel))
				{
					return;
				}
				panel = (TileEntityInfoPanel)ent;
				slot = dat.readByte();
				ItemStack itemStack = panel.getStackInSlot(slot);
				if(itemStack == null || !(itemStack.getItem() instanceof IPanelDataSource))
				{
					return;
				}
				new CardWrapperImpl(itemStack, slot).setTitle(dat.readUTF());
				panel.resetCardData();
				break;
			case PacketHandler.PACKET_ECOUNTER:
				world = FMLClientHandler.instance().getClient().theWorld;
				x = dat.readInt();
				y = dat.readInt();
				z = dat.readInt();
				ent = world.getTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityEnergyCounter))
				{
					return;
				}
				TileEntityEnergyCounter counter = (TileEntityEnergyCounter)ent;
				counter.counter = dat.readLong();
				break;
			case PacketHandler.PACKET_ACOUNTER:
				world = FMLClientHandler.instance().getClient().theWorld;
				x = dat.readInt();
				y = dat.readInt();
				z = dat.readInt();
				ent = world.getTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityAverageCounter))
				{
					return;
				}
				TileEntityAverageCounter avgCounter = (TileEntityAverageCounter)ent;
				avgCounter.setClientAverage(dat.readInt());
				break;
			case PacketHandler.PACKET_DISP_SETTINGS_ALL:
				world = FMLClientHandler.instance().getClient().theWorld;
				x = dat.readInt();
				y = dat.readInt();
				z = dat.readInt();
				ent = world.getTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityInfoPanel))
				{
					return;
				}
				panel = (TileEntityInfoPanel)ent;
				byte count = dat.readByte();
				for (int i = 0; i < count; i++)
				{
					slot = dat.readByte();
					short dCount = dat.readShort();
					Map<UUID, Integer> settings = panel.getDisplaySettingsForSlot(slot);
					for (int j = 0; j < dCount; j++)
					{
						long most = dat.readLong();
						long least = dat.readLong();
						settings.put(new UUID(most, least), dat.readInt()); 
					}
				}
				panel.resetCardData();
				break;
			case PacketHandler.PACKET_DISP_SETTINGS_UPDATE:
				world = FMLClientHandler.instance().getClient().theWorld;
				x = dat.readInt();
				y = dat.readInt();
				z = dat.readInt();
				slot = dat.readByte();
				ent = world.getTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityInfoPanel))
				{
					return;
				}
				long most = dat.readLong();
				long least = dat.readLong();
				panel = (TileEntityInfoPanel)ent;
				int settings = dat.readInt();
				panel.getDisplaySettingsForSlot(slot).put(new UUID(most, least), settings);
				panel.resetCardData();
				break;
			default:
				FMLLog.warning("%sUnknown packet type: %d", IC2NuclearControl.LOG_PREFIX, packetType);
				break;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
