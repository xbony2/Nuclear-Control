package shedar.mods.ic2.nuclearcontrol.api;

/**
 * Interface is used to define, that card has own advanced settings (custom
 * GUI), like Text Card. If card implements this interface, Information Panel
 * displays "..." button in its GUI.
 * 
 * @author Shedar
 */
public interface IAdvancedCardSettings {
	/**
	 * Method called when player click "..." button in the Information Panel
	 * GUI. It should return instance of GuiScreen to display. Gui should also
	 * implement {@link ICardGui} interface. Otherwise it wouldn't be able to
	 * save new settings. GuiContainers are not supported.
	 * 
	 * @param wrapper
	 * @return instance of card's Gui.
	 */
	ICardGui getSettingsScreen(ICardWrapper wrapper);
}
