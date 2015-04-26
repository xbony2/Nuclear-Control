package shedar.mods.ic2.nuclearcontrol.crossmod.ic2classic;

import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

public class CrossIndustrialCraft2Classic{
	private IC2ClassicType type;
	
	public CrossIndustrialCraft2Classic(){
		try{
			Class classicClass = Class.forName("ic2.api.info.IC2Classic");
			type = IC2ClassicType.SPEIGER;
			NCLog.warn("IC2 Classic detected: issues may occur. Especially with Speiger's terrible indent style :P");
		}catch(ClassNotFoundException e){
			try{
				Class classicClass = Class.forName("ic2classic.core.IC2");
				type = IC2ClassicType.IMMIBIS;
				NCLog.warn("IC2 Classic detected: issues may occur. Especially since Immibis "
						+ "doesn't know how to created new packages :P");
			}catch(ClassNotFoundException e1){
				type = IC2ClassicType.NONE;
			}
		}
	}
	
	public IC2ClassicType getClassicType(){
		return type;
	}
}
