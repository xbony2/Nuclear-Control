package shedar.mods.ic2.nuclearcontrol.api;

/**
 * Enum of card states. Used as return value of
 * {@link IPanelDataSource#update(net.minecraft.tileentity.TileEntity, ICardWrapper, int)}
 * . If card state is OK or CUSTOM_ERROR, then
 * {@link IPanelDataSource#getStringData(int, ICardWrapper, boolean)} will be
 * called. Otherwise standard message will be displayed.
 * 
 * @author Shedar
 */
public enum CardState {
	/**
	 * All required data found, ready to display it
	 */
	OK(1),

	/**
	 * Target block doesn't exist or has invalid type
	 */
	NO_TARGET(2),

	/**
	 * Target is out of range
	 */
	OUT_OF_RANGE(3),

	/**
	 * Card doesn't have required fields. Maybe was spawned via NEI
	 */
	INVALID_CARD(4),

	/**
	 * Reserved for future use
	 */
	CUSTOM_ERROR(5);

	private final int index;

	CardState(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static CardState fromInteger(int value) {
		switch (value) {
		case 1:
			return OK;
		case 2:
			return NO_TARGET;
		case 3:
			return OUT_OF_RANGE;
		case 4:
			return INVALID_CARD;
		case 5:
			return CUSTOM_ERROR;
		}
		return CUSTOM_ERROR;
	}
}