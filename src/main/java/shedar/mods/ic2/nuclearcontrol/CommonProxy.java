/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.gui.GuiRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.tileentities.*;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityThermo.class, "IC2Thermo");
		GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, "IC2HowlerAlarm");
		GameRegistry.registerTileEntity(TileEntityIndustrialAlarm.class, "IC2IndustrialAlarm");
		GameRegistry.registerTileEntity(TileEntityRemoteThermo.class, "IC2RemoteThermo");
		GameRegistry.registerTileEntity(TileEntityInfoPanel.class, "IC2NCInfoPanel");
		GameRegistry.registerTileEntity(TileEntityInfoPanelExtender.class, "IC2NCInfoPanelExtender");
		GameRegistry.registerTileEntity(TileEntityAdvancedInfoPanel.class, "IC2NCAdvancedInfoPanel");
		GameRegistry.registerTileEntity(TileEntityAdvancedInfoPanelExtender.class, "IC2NCAdvancedInfoPanelExtender");
		GameRegistry.registerTileEntity(TileEntityEnergyCounter.class, "IC2NCEnergyCounter");
		GameRegistry.registerTileEntity(TileEntityAverageCounter.class, "IC2NCAverageCounter");
		GameRegistry.registerTileEntity(TileEntityRangeTrigger.class, "IC2NCRangeTrigger");
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiRemoteMonitor.REMOTEMONITOR_GUI){
            return new ContainerRemoteMonitor(player.inventory, player.getHeldItem(),  new InventoryItem(player.getHeldItem()), new TileEntityInfoPanel());
        }
		Subblock block = IC2NuclearControl.blockNuclearControlMain.getSubblock(ID);
		if (block == null)
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		return block.getServerGuiElement(tileEntity, player);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// should be null on server :p
		return null;
	}

	public void cape(){}

}