/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.WorldEvent;
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
		try{		
			out.writeShort(PacketHandler.PACKET_ALARM);
			out.writeInt(IC2NuclearControl.instance.maxAlarmRange);
			out.writeUTF(IC2NuclearControl.instance.allowedAlarms);
		}catch (IOException e){
			e.printStackTrace();
		}
		ChannelHandler.instance.sendToPlayer(new Packet(stream.toByteArray()),event.player);
	}

}
