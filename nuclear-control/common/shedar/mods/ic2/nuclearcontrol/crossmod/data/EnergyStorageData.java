package shedar.mods.ic2.nuclearcontrol.crossmod.data;

public class EnergyStorageData
{
    public static final int TARGET_TYPE_UNKNOWN = -1;
    public static final int TARGET_TYPE_IC2 = 0;
    public static final int TARGET_TYPE_BC = 1;
    
    public long stored;
    public long capacity;
    public String units;
    public int type;

}
