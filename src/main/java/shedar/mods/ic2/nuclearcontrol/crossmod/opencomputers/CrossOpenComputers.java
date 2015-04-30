package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import cpw.mods.fml.common.Loader;
import li.cil.oc.api.Driver;

public class CrossOpenComputers {
	private boolean _isApiAvailable = false;
	private Class _driverTile;
	
	public CrossOpenComputers(){
		if(!Loader.isModLoaded("OpenComputers")) return;
		try{
			_driverTile = Class.forName("li.cil.oc.api.Driver", false, this.getClass().getClassLoader());
			_isApiAvailable = true;
		}catch(ClassNotFoundException e){
			_isApiAvailable = false;
		}
		
		if(_isApiAvailable) addDrivers();
	}
	
	public static void addDrivers(){
		Driver.add(new DriverAdvancedInfoPanel());
		Driver.add(new DriverInfoPanel());
		Driver.add(new DriverThermalMonitor());
		Driver.add(new DriverHowlerAlarm());
		Driver.add(new DriverAverageCounter());
	}
}
