package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;

public class SoundHelper {
	private static final float DEFAULT_RANGE = 16F;

	private static void playSound(PositionedSoundRecord sound) {
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
	}

	public static PositionedSoundRecord playAlarm(double x, double y, double z,
			String name, float volume) {
		float range = DEFAULT_RANGE;

		if (volume > 1.0F) {
			range *= volume;
		}

		Entity person = FMLClientHandler.instance().getClient().renderViewEntity;

		if (person != null && volume > 0
				&& person.getDistanceSq(x, y, z) < range * range) {
			PositionedSoundRecord sound = new PositionedSoundRecord(
					new ResourceLocation(name), volume, 1.0F, (float) x,
					(float) y, (float) z);
			playSound(sound);
			return sound;
		}
		return null;
	}

	public static boolean isPlaying(PositionedSoundRecord sound) {
		return Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(sound);
	}

	public static void stopAlarm(PositionedSoundRecord sound) {
		Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
	}
}