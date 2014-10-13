package shedar.mods.ic2.nuclearcontrol.gui;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {
	public ConfigGui(GuiScreen parent) {
		super(parent, new ConfigElement(IC2NuclearControl.config.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), 
				"IC2NuclearControl", false, false, GuiConfig .getAbridgedConfigPath(IC2NuclearControl.config.configuration.toString()));
	}
}