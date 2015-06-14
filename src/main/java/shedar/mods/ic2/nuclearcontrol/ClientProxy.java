/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import com.jadarstudios.developercapes.DevCapes;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.gui.GuiRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.renderers.MainBlockRenderer;
import shedar.mods.ic2.nuclearcontrol.renderers.TileEntityIC2ThermoRenderer;
import shedar.mods.ic2.nuclearcontrol.renderers.TileEntityInfoPanelRenderer;
import shedar.mods.ic2.nuclearcontrol.renderers.TileEntityRemoteThermoRenderer;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerTileEntities() {
		TileEntityIC2ThermoRenderer renderThermalMonitor = new TileEntityIC2ThermoRenderer();
		TileEntityRemoteThermoRenderer renderRemoteThermo = new TileEntityRemoteThermoRenderer();
		TileEntityInfoPanelRenderer renderInfoPanel = new TileEntityInfoPanelRenderer();

		ClientRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo.class, "IC2Thermo", renderThermalMonitor);
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
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiRemoteMonitor.REMOTEMONITOR_GUI){
            return new GuiRemoteMonitor(player.inventory, player.getCurrentEquippedItem(),  new InventoryItem(player.getHeldItem()), player, new TileEntityInfoPanel(), world);
        }
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
	public void cape(){
		DevCapes.getInstance().registerConfig("http://xbony2.github.io/cape.json");
	}
}