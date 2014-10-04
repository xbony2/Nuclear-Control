package shedar.mods.ic2.nuclearcontrol.api;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import net.minecraft.item.ItemStack;

/**
 * Helper class, if you need {@link ICardWrapper} object somewhere.
 * 
 * @author Shedar
 */
public final class CardHelper {
	private static final String className = "shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ICardWrapper getWrapper(ItemStack card) {
		try {
			Class c = Class.forName(className);
			return (ICardWrapper) c.getConstructor(ItemStack.class, int.class).newInstance(card, -1);
		}catch (Exception e){
			IC2NuclearControl.logger.error("Can't create Nuclear Control Card Wrapper: %s", e.toString());
		}
		return null;
	}
}
