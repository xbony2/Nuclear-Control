package shedar.mods.ic2.nuclearcontrol.crossmod.nei;

import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.guihook.GuiContainerManager;

public class NEINuclearControlConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		GuiContainerManager.addTooltipHandler(new TooltipHandler());
	}

	@Override
	public String getName() {
		return "Nuclear Control";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

}
