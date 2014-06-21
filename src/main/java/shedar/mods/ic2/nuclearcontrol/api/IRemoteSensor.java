package shedar.mods.ic2.nuclearcontrol.api;

/**
 * Interface defines, that card has only one target object. if interface is implemented by
 * card, then Information Panel checks if target in range. Methods {@link ICardWrapper#getTarget()}
 * and {@link ICardWrapper#setTarget(int, int, int)} can be used only by cards, which implements IRemoteSensor
 * interface.  
 * @author Shedar
 */
public interface IRemoteSensor
{
}
