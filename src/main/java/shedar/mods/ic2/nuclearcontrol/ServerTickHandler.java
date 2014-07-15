/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundListSerializer;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.world.WorldEvent;

public class ServerTickHandler {

	public final static ServerTickHandler instance = new ServerTickHandler();
	private static final Gson gson = (new GsonBuilder()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
	private static final ParameterizedType type = new ParameterizedType()
	{
		private static final String __OBFID = "CL_00001148";
		public Type[] getActualTypeArguments()
		{
			return new Type[] {String.class, SoundList.class};
		}
		public Type getRawType()
		{
			return Map.class;
		}
		public Type getOwnerType()
		{
			return null;
		}
	};

	// Function onWorldUnload from CommonProxy.java	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		IC2NuclearControl.instance.screenManager.clearWorld(event.world);
	}

	// Function playerLoggedIn from ConnectionHandler.java
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try
		{		
			out.writeShort(PacketHandler.PACKET_ALARM);
			out.writeInt(IC2NuclearControl.instance.maxAlarmRange);
			out.writeUTF(IC2NuclearControl.instance.allowedAlarms);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToPlayer(new Packet(stream.toByteArray()),event.player);
	}

	@SubscribeEvent
	public void importSound(SoundLoadEvent event)
	{
		IC2NuclearControl ncInstance = IC2NuclearControl.instance; 
		ncInstance.availableAlarms = new ArrayList<String>();

		try
		{
			List list = Minecraft.getMinecraft().getResourceManager().getAllResources(new ResourceLocation("nuclearcontrol", "sounds.json")); 

			for (int i = list.size() - 1; i >= 0; --i)
			{
				IResource iresource = (IResource)list.get(i);

				try
				{
					Map map = (Map) gson.fromJson(new InputStreamReader(iresource.getInputStream()), type);
					Iterator iterator1 = map.entrySet().iterator();

					while (iterator1.hasNext())
					{
						Entry entry = (Entry)iterator1.next();
						ncInstance.availableAlarms.add(((String)entry.getKey()).replace("alarm-",""));
					}
				}
				catch (RuntimeException runtimeexception)
				{
					;
				}
			}
		}
		catch (IOException ioexception)
		{
			;
		}

		ncInstance.serverAllowedAlarms = new ArrayList<String>();
	}
}
