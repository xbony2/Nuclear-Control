/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.renderers.MainBlockRenderer;
import shedar.mods.ic2.nuclearcontrol.renderers.TileEntityIC2ThermoRenderer;
import shedar.mods.ic2.nuclearcontrol.renderers.TileEntityInfoPanelRenderer;
import shedar.mods.ic2.nuclearcontrol.renderers.TileEntityRemoteThermoRenderer;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.LanguageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public String playAlarm(double x, double y, double z, String name, float volume)
	{
		return SoundHelper.playAlarm(x, y, z, name, volume);
	}

	@Override
	public void stopAlarm(String soundId)
	{
		SoundHelper.stopAlarm(soundId);
	}

	@Override
	public boolean isPlaying(String soundId)
	{
		return SoundHelper.isPlaying(soundId);
	}

	@Override
	public void registerTileEntities()
	{
		TileEntityIC2ThermoRenderer renderThermalMonitor = new TileEntityIC2ThermoRenderer();
		TileEntityRemoteThermoRenderer renderRemoteThermo = new TileEntityRemoteThermoRenderer();
		TileEntityInfoPanelRenderer renderInfoPanel = new TileEntityInfoPanelRenderer(); 

		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIC2Thermo.class, "IC2Thermo", renderThermalMonitor);
		GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm.class, "IC2HowlerAlarm");
		GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIndustrialAlarm.class, "IC2IndustrialAlarm");
		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo.class, "IC2RemoteThermo", renderRemoteThermo);
		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel.class, "IC2NCInfoPanel", renderInfoPanel);
		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanelExtender.class, "IC2NCInfoPanelExtender", renderInfoPanel);
		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel.class, "IC2NCAdvancedInfoPanel", renderInfoPanel);
		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanelExtender.class, "IC2NCAdvancedInfoPanelExtender", renderInfoPanel);
		GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter.class, "IC2NCEnergyCounter");
		GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter.class, "IC2NCAverageCounter");
		GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger.class, "IC2NCRangeTrigger");
		int modelId = RenderingRegistry.getNextAvailableRenderId();
		IC2NuclearControl.instance.modelId = modelId;
		RenderingRegistry.registerBlockHandler(new MainBlockRenderer(modelId));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		Subblock block = IC2NuclearControl.blockNuclearControlMain.getSubblock(ID);
		if (block == null)
			return null;
		return block.getClientGuiElement(tileEntity, player);
	}

	public static EntityPlayer getPlayer() {	
		return Minecraft.getMinecraft().thePlayer;
	}
	
		@Override
		public void onPacketDataClient(ByteBuf source, EntityPlayer entityPlayer){		
			World world;
			int x,y,z;
			TileEntity ent;
			TileEntityInfoPanel panel;
			try{
				DataInputStream dat = new DataInputStream(new ByteArrayInputStream(Arrays.copyOfRange(source.array(), 1, source.array().length)));        
				short packetType = dat.readShort();
				switch (packetType){
				case PacketHandler.PACKET_CHAT:
					String message = dat.readUTF();
					String[] chunks = message.split(":");
					message = LanguageHelper.translate("msg.nc." + chunks[0]);
					if (chunks.length > 1){
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
							try{
								tag = CompressedStreamTools.readCompressed(dat);
								helper.setTag(name, tag);
							}catch (IOException e){
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
					if (ent == null || !(ent instanceof TileEntityInfoPanel)){
						return;
					}
					panel = (TileEntityInfoPanel)ent;
					slot = dat.readByte();
					ItemStack itemStack = panel.getStackInSlot(slot);
					if(itemStack == null || !(itemStack.getItem() instanceof IPanelDataSource)){
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
					if (ent == null || !(ent instanceof TileEntityEnergyCounter)){
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
					if (ent == null || !(ent instanceof TileEntityAverageCounter)){
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