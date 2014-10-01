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
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

	public void registerTileEntities() {
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIC2Thermo.class,
						"IC2Thermo");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm.class,
						"IC2HowlerAlarm");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIndustrialAlarm.class,
						"IC2IndustrialAlarm");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo.class,
						"IC2RemoteThermo");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel.class,
						"IC2NCInfoPanel");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanelExtender.class,
						"IC2NCInfoPanelExtender");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel.class,
						"IC2NCAdvancedInfoPanel");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanelExtender.class,
						"IC2NCAdvancedInfoPanelExtender");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter.class,
						"IC2NCEnergyCounter");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter.class,
						"IC2NCAverageCounter");
		GameRegistry
				.registerTileEntity(
						shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger.class,
						"IC2NCRangeTrigger");
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		Subblock block = IC2NuclearControl.blockNuclearControlMain
				.getSubblock(ID);
		if (block == null)
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		return block.getServerGuiElement(tileEntity, player);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// null on server
		return null;
	}

}