package shedar.mods.ic2.nuclearcontrol.crossmod;

public class EnergyStorageData {
	public static final int TARGET_TYPE_UNKNOWN = -1;
	public static final int TARGET_TYPE_IC2 = 0;
	public static final int TARGET_TYPE_RF = 1;
	public static final int TARGET_TYPE_GT = 2; //reserved for future use
	
	public static final String UNITS_EU = "EU";
	public static final String UNITS_RF = "RF";
	public static final String UNITS_GT = "GT";

	public double stored;
	public double capacity;
	public String units;
	public int type;

}
