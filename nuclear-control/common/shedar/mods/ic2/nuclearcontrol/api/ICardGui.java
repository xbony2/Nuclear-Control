package shedar.mods.ic2.nuclearcontrol.api;

/**
 * Interface for card's gui. Used to set wrapper object.
 * @author Shedar
 *
 */
public interface ICardGui
{
    /**
     * Method sets wrapper object, which should be used to store new settings and return to the
     * Information Panel gui.
     * @param wrapper
     * @see ICardSettingsWrapper
     */
    void setCardSettingsHelper(ICardSettingsWrapper wrapper);
}
