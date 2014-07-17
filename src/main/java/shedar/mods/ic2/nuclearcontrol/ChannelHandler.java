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
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, Packet msg){
		switch (FMLCommonHandler.instance().getEffectiveSide()){
		case CLIENT:
			IC2NuclearControl.instance.proxy.onPacketDataClient(source, ClientProxy.getPlayer());
			break;
		case SERVER:
			NetHandlerPlayServer netHandler = (NetHandlerPlayServer)(ctx.channel().attr(NetworkRegistry.NET_HANDLER).get());
			IC2NuclearControl.instance.proxy.onPacketData(source, netHandler.playerEntity);
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
	public static void sendToAllPlayers(Packet packet){
		IC2NuclearControl.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		IC2NuclearControl.channels.get(Side.SERVER).writeOutbound(packet);
	}*/

}
