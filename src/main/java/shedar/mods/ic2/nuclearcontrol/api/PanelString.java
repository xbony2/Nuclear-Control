package shedar.mods.ic2.nuclearcontrol.api;

/**
 * Object defines one line of the Information Panel. Each line has contain 3 sections: left, center, right.
 * At least one of them should be not null. Each section can have its own color.   
 * @see IPanelDataSource#getStringData(int, ICardWrapper, boolean)
 * @author Shedar
 */
public class PanelString{
	/**
	 * Text of the left aligned part of the line.
	 */
	public String textLeft;

	/**
	 * Text of the centered part of the line.
	 */
	public String textCenter;

	/**
	 * text of the right aligned part of the line.
	 */
	public String textRight;

	/**
	 * Color of the left aligned part of the line.
	 */
	public int colorLeft = 0;

	/**
	 * Color of the centered part of the line.
	 */
	public int colorCenter = 0;

	/**
	 * Color of the right aligned part of the line.
	 */
	public int colorRight = 0;
}