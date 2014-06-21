package shedar.mods.ic2.nuclearcontrol.api;

/**
 * Interface defines methods to save new card's settings from card's gui.
 * @see #commit()
 * @see #closeGui()
 * @author Shedar
 *
 */
public interface ICardSettingsWrapper
{
    void setInt(String name, Integer value);
    void setLong(String name, Long value);
    void setString(String name, String value);
    void setBoolean(String name, Boolean value);
    
    /**
     * Method saves unsaved field changes.
     */
    void commit();
    
    /**
     * Method closes card's gui and returns control to the gui of Information panel.
     */
    void closeGui();
}
