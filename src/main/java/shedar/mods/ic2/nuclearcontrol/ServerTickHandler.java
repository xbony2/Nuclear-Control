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
import net.minecraft.entity.player.EntityPlayerMP;

import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.network.message.PacketAlarm;

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
	public void onPlayerLogin(PlayerLoggedInEvent event){
		ChannelHandler.network.sendTo(new PacketAlarm(IC2NuclearControl.instance.maxAlarmRange, IC2NuclearControl.instance.allowedAlarms), (EntityPlayerMP)event.player);
	}

}
