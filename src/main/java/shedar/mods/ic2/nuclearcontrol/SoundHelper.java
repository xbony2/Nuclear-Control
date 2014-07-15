/**
 * 
 * @author Zuxelus, and he got it from the SoundsCool mod
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import paulscode.sound.SoundSystem;
import cpw.mods.fml.client.FMLClientHandler;

public class SoundHelper
{
	private static final String SOUND_ID_PREFIX = "NuclearControl_";
	public static final String SOUND_FOLDER = "nuclearControl";
	private static final float DEFAULT_RANGE = 16F; 

	private static int internalId = 0;

	private static String playSound(String name)
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation(name)));
		return name;
	}

	public static String playAlarm(double x, double y, double z, String name, float volume)
	{
		float range = DEFAULT_RANGE;

		if (volume > 1.0F)
		{
			range *= volume;
		}

		Entity person = FMLClientHandler.instance().getClient().renderViewEntity;

		if (person!= null && person.getDistanceSq(x, y, z) < (double)(range * range))
		{
			return playSound(name);
		}
		return null;
	}

	public static boolean isPlaying(String name)
	{
		return Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(PositionedSoundRecord.func_147673_a(new ResourceLocation(name)));
	}

	public static void stopAlarm(String name)
	{
		Minecraft.getMinecraft().getSoundHandler().stopSound(PositionedSoundRecord.func_147673_a(new ResourceLocation(name)));
	}
}