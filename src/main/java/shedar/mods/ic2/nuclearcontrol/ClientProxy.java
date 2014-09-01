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
import net.minecraft.client.audio.PositionedSoundRecord;
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

}