package shedar.mods.ic2.nuclearcontrol.utils;

import net.minecraft.util.StatCollector;

public class LangHelper {
	public static String translate(String key) {
		return StatCollector.translateToLocal(key);
	}

}
