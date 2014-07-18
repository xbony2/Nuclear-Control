package shedar.mods.ic2.nuclearcontrol.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.StorageArrayRecipe;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitMultipleSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class RecipesOld {
	@Deprecated
	public static void addOldRecipes(){
    	ItemStack thermalMonitor = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_THERMAL_MONITOR);
        Recipes.advRecipes.addRecipe(thermalMonitor, new Object[]{
                    "GGG", "GCG", "GRG", 
                        'G', IC2Items.getItem("reinforcedGlass"), 
                        'R', "dustRedstone", 
                        'C', "circuitAdvanced"});
        
        ItemStack howler = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_HOWLER_ALARM);
        Recipes.advRecipes.addRecipe(howler, new Object[]{
                    "NNN", "ICI", "IRI", 
                        'I', "ingotIron", 
                        'R', "dustRedstone", 
                        'N', Blocks.noteblock, 
                        'C', "circuitBasic"});
        
        ItemStack industrialAlarm = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_INDUSTRIAL_ALARM);
        Recipes.advRecipes.addRecipe(industrialAlarm, new Object[]{
                    "GOG", "GHG", "GRG", 
                        'G', IC2Items.getItem("reinforcedGlass"), 
                        'O', "dyeOrange", 
                        'R', "dustRedstone", 
                        'H', howler});

        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_REMOTE_THERMO), new Object[]{
                    "F", "M", "T", 
                        'T', thermalMonitor, 
                        'M', IC2Items.getItem("machine"), 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_INFO_PANEL), new Object[]{
                    "PPP", "LCL", "IRI", 
                        'P', "paneGlass", 
                        'L', "dyeLime", 
                        'I', "dyeBlack", 
                        'R', "dustRedstone", 
                        'C', "circuitBasic"});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_INFO_PANEL_EXTENDER), new Object[] {
                    "PPP", "WLW", "WWW", 
                        'P', "paneGlass", 
                        'L', "dyeLime", 
                        'W', "plankWood"});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_ADVANCED_PANEL), new Object[] {
                    "PPP", "GLG", "CAC", 
                        'P', "paneGlass", 
                        'L', "dyeLime", 
                        'G', IC2Items.getItem("goldCableItem"),
                        'A', "circuitAdvanced", 
                        'C', IC2Items.getItem("carbonPlate")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_ADVANCED_EXTENDER), new Object[] {
                    "PPP", "GLG", "GCG", 
                        'P', "paneGlass", 
                        'L', "dyeLime", 
                        'G', IC2Items.getItem("goldCableItem"),
                        'C', IC2Items.getItem("carbonPlate")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemToolThermometer, 1), new Object[] {
                    "IG ", "GWG", " GG", 
                        'G', "blockGlass", 
                        'I', "ingotIron", 
                        'W', IC2Items.getItem("waterCell")});
        
        ItemStack digitalThermometer = new ItemStack(IC2NuclearControl.itemToolDigitalThermometer, 1);
        Recipes.advRecipes.addRecipe(digitalThermometer, new Object[] {
                    "I  ", "IC ", " GI", 
                        'G', "dustGlowstone", 
                        'I', "ingotIron", 
                        'C', "circuitBasic"}); 
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemRemoteSensorKit, 1), new Object[]{
                    "  F", " D ", "P  ", 
                        'P', Items.paper, 
                        'D', digitalThermometer, 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemEnergySensorKit, 1), new Object[]{
                    "  F", " R ", "P  ", 
                        'P', Items.paper, 
                        'R', "redstone", 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), new Object[]{
                    "CFC", 
                        'C', IC2Items.getItem("insulatedCopperCableItem"), 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_COLOR), new Object[] {
                    "RYG","WCM","IAB", 
                        'R', "dyeRed",  
                        'Y', "dyeYellow",  
                        'G', "dyeGreen",  
                        'W', "dyeWhite",  
                        'C', IC2Items.getItem("insulatedCopperCableItem"), 
                        'M', "dyeMagenta",  
                        'I', "dyeBlack",  
                        'A', "dyeCyan",  
                        'B', "dyeBlue"});
        
        /*if(isHttpSensorAvailable){
            Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_WEB), new Object[]{
                        "CFC","CAC","CFC", 
                            'C', new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), 
                            'A', IC2Items.getItem("advancedCircuit"),
                            'F', IC2Items.getItem("glassFiberCableItem")});
        }*/
        
        ItemStack energyCounter = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_ENERGY_COUNTER);
        Recipes.advRecipes.addRecipe(energyCounter, new Object[]{
                    " A ", "FTF", 
                        'A', "circuitAdvanced", 
                        'F', IC2Items.getItem("glassFiberCableItem"), 
                        'T', IC2Items.getItem("mvTransformer")});
        
        ItemStack averageCounter = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_AVERAGE_COUNTER);
        Recipes.advRecipes.addRecipe(averageCounter, new Object[]{
                "FTF", " A ",  
                        'A', "circuitAdvanced", 
                        'F', IC2Items.getItem("glassFiberCableItem"), 
                        'T', IC2Items.getItem("mvTransformer")});
        
        ItemStack rangeTrigger = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_RANGE_TRIGGER);
        Recipes.advRecipes.addRecipe(rangeTrigger, new Object[]{
                "EFE", "AMA",  " R ",
                        'E', IC2Items.getItem("detectorCableItem"), 
                        'F', IC2Items.getItem("frequencyTransmitter"),
                        'A', "circuitAdvanced", 
                        'M', IC2Items.getItem("machine"), 
                        'R', "dustRedstone"});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_COUNTER), new Object[] {
                    "  F", " C ", "P  ", 
                        'P', Items.paper, 
                        'C', "circuitBasic", 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_LIQUID), new Object[] {
                    "  F", " C ", "P  ", 
                        'P', Items.paper, 
                        'C', Items.bucket, 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemTextCard, 1), new Object[] {
                    " C ", "PFP", " C ", 
                        'P', Items.paper, 
                        'C', "circuitBasic", 
                        'F', IC2Items.getItem("insulatedCopperCableItem")});
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2NuclearControl.itemTimeCard, 1),  
        		"circuitBasic", Items.clock);
        
        /*Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2),  
        		IC2NuclearControl.itemSensorLocationCard );
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2),  
        		IC2NuclearControl.itemEnergySensorLocationCard );
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2),  
                new  ItemStack(IC2NuclearControl.itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_COUNTER));
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 1),  
                new  ItemStack(IC2NuclearControl.itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_LIQUID));
        */
        CraftingManager.getInstance().getRecipeList().add(new StorageArrayRecipe());
    }
}
