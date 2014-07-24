package shedar.mods.ic2.nuclearcontrol.api;

import java.util.UUID;

/**
 * Object of PanelSetting class defines one checkbox in the card's settings. 
 * @author Shedar
 * @see IPanelDataSource#getSettingsList()
 */
public class PanelSetting{
	/**
	 * Name of the option
	 */
	public String title;

	/**
	 * Bit number in display settings. Should be in the range 0-31. 
	 */
	public int displayBit;

	/**
	 * Identifier of the card. Should be same as {@link IPanelDataSource#getCardType()}.
	 */
	public UUID cardType;

	/**
	 * @param title Name of the option
	 * @param displayBit Bit number in display settings. Should be in the range 0-31.
	 * @param cardType Identifier of the card. Should be same as {@link IPanelDataSource#getCardType()}.
	 */
	public PanelSetting(String title, int displayBit, UUID cardType){
		this.title = title;
		this.displayBit = displayBit;
		this.cardType = cardType;
	}
}