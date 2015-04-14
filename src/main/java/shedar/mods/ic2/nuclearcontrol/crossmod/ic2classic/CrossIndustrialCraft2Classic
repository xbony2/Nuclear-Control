package shedar.mods.ic2.nuclearcontrol.crossmod.ic2classic;

import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

public class CrossIndustrialCraft2Classic{
	private boolean isIC2ClassicOn;
	
	public CrossIndustrialCraft2Classic(){
		try{
			Class classicClass = Class.forName("ic2.api.info.IC2Classic");
			isIC2ClassicOn = true;
			NCLog.warn("IC2 Classic detected: issues may occur. Especially with Speiger's terrible indent style :P");
		}catch(ClassNotFoundException e){
			isIC2ClassicOn = false;
		}
	}
	
	public boolean isIc2ClassicOn(){
		return isIC2ClassicOn;
	}
}
