package shedar.mods.ic2.nuclearcontrol.utils;

public class TextureResolver {
	private static final String TEXTURE_FOLDER = "nuclearControl";

	public static String getItemTexture(String name) {
		return TEXTURE_FOLDER + ":" + name;
	}
}
