package shedar.mods.ic2.nuclearcontrol;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.UUID;

public class ConfigurationHandler {
    public Configuration configuration;

    public void init(File configFile) {
        if (configuration == null){
            configuration = new Configuration(configFile);
        }
        loadConfiguration();
    }

    private void loadConfiguration(){
        try{
        	IC2NuclearControl.instance.alarmRange = configuration.get(Configuration.CATEGORY_GENERAL, "alarmRange", 64).getInt();
        	IC2NuclearControl.instance.maxAlarmRange = configuration.get(Configuration.CATEGORY_GENERAL, "maxAlarmRange", 128).getInt();
        	IC2NuclearControl.instance.allowedAlarms = configuration.get(Configuration.CATEGORY_GENERAL, "allowedAlarms", "default,sci-fi").getString().replaceAll(" ", "");
        	IC2NuclearControl.instance.remoteThermalMonitorEnergyConsumption = configuration.get(Configuration.CATEGORY_GENERAL, "remoteThermalMonitorEnergyConsumption", 1).getInt();
        	IC2NuclearControl.instance.screenRefreshPeriod = configuration.get(Configuration.CATEGORY_GENERAL, "infoPanelRefreshPeriod", 20).getInt();
        	IC2NuclearControl.instance.rangeTriggerRefreshPeriod = configuration.get(Configuration.CATEGORY_GENERAL, "rangeTriggerRefreshPeriod", 20).getInt();
        	IC2NuclearControl.instance.SMPMaxAlarmRange = configuration.get(Configuration.CATEGORY_GENERAL, "SMPMaxAlarmRange", 256).getInt();
        	IC2NuclearControl.instance.isHttpSensorAvailable = configuration.get(Configuration.CATEGORY_GENERAL, "isHttpSensorAvailable", true).getBoolean(true);
        	IC2NuclearControl.instance.httpSensorKey = configuration.get(Configuration.CATEGORY_GENERAL, "httpSensorKey", UUID.randomUUID().toString().replace("-", "")).getString();
        	IC2NuclearControl.instance.recipes = configuration.get(Configuration.CATEGORY_GENERAL, "recipes", "normal").getString();
        }
        catch (Exception e){
        	IC2NuclearControl.logger.error("Mod has a problem loading it's configuration", e);
        }finally{
            if (configuration.hasChanged()){
                configuration.save();
            }
        }
    }

    public void save(){
        if (configuration.hasChanged()){
            configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event){
        if (event.modID.equalsIgnoreCase("IC2NuclearControl2")){
            loadConfiguration();
        }
    }
}