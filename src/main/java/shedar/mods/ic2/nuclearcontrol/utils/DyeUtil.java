package shedar.mods.ic2.nuclearcontrol.utils;

import net.minecraft.item.Item;
import scala.actors.threadpool.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class DyeUtil {
	public static final Dye WHITE_DYE = new Dye("dyeWhite", ColorUtil.COLOR_WHITE);
	public static final Dye ORANGE_DYE = new Dye("dyeOrange", ColorUtil.COLOR_ORANGE);
	public static final Dye MAGENTA_DYE = new Dye("dyeMagenta", ColorUtil.COLOR_MAGENTA);
	public static final Dye LIGHT_BLUE_DYE = new Dye("dyeLightBlue", ColorUtil.COLOR_LIGHT_BLUE);
	public static final Dye YELLOW_DYE = new Dye("dyeYellow", ColorUtil.COLOR_YELLOW);
	public static final Dye LIME_DYE = new Dye("dyeLime", ColorUtil.COLOR_LIME);
	public static final Dye PINK_DYE = new Dye("dyePink", ColorUtil.COLOR_PINK);
	public static final Dye LIGHT_GRAY_DYE = new Dye("dyeLightGray", ColorUtil.COLOR_GRAY);
	public static final Dye CYAN_DYE = new Dye("dyeCyan", ColorUtil.COLOR_CYAN);
	public static final Dye PURPLE_DYE = new Dye("dyePurple", ColorUtil.COLOR_PURPLE);
	public static final Dye BLUE_DYE = new Dye("dyeBlue", ColorUtil.COLOR_BLUE);
	public static final Dye BROWN_DYE = new Dye("dyeBrown", ColorUtil.COLOR_BROWN);
	public static final Dye GREEN_DYE = new Dye("dyeGreen", ColorUtil.COLOR_GREEN);
	public static final Dye RED_DYE = new Dye("dyeRed", ColorUtil.COLOR_RED);
	public static final Dye BLACK_DYE = new Dye("dyeBlack", ColorUtil.COLOR_BLACK);
	
	public static final Dye[] ALL_DYES_DYE = {WHITE_DYE, ORANGE_DYE, MAGENTA_DYE, LIGHT_BLUE_DYE, YELLOW_DYE, LIME_DYE, PINK_DYE, LIGHT_GRAY_DYE, CYAN_DYE, PURPLE_DYE, BLUE_DYE, BROWN_DYE, 
		GREEN_DYE, RED_DYE, BLACK_DYE};
	
	//public static final ItemStack[] ALL_DYES =
			//(ItemStack[]) ArrayUtils.addAll(WHITE_DYE.DYES, ORANGE_DYE.DYES, MAGENTA_DYE.DYES, LIGHT_BLUE_DYE.DYES, YELLOW_DYE.DYES, LIME_DYE.DYES,
			//PINK_DYE.DYES, LIGHT_GRAY_DYE.DYES, CYAN_DYE.DYES, PURPLE_DYE.DYES, BLUE_DYE.DYES, BROWN_DYE.DYES, GREEN_DYE.DYES, RED_DYE.DYES, BLACK_DYE.DYES);
	
	public static boolean isADye(ItemStack itemstack){
		Item toFind = itemstack.getItem(); //There's no setStackSize() apperently
		//NCLog.error(BLACK_DYE.setDyes().length);
		for(int s=0; s < ALL_DYES_DYE.length; s++){
			for(int z=0; z< ALL_DYES_DYE[s].setDyes().length; z++){
				if(ALL_DYES_DYE[s].setDyes()[z].getItem().equals(toFind)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getDyeId(ItemStack itemstack){
		ItemStack i2 = new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage());
		for(Dye dye: ALL_DYES_DYE){
			for(ItemStack dyestack : dye.DYES){
				if(itemstack.getItem().equals(dyestack.getItem()) && itemstack.getItemDamage() == dyestack.getItemDamage())
					return dye.getDyeId();
			}
		}
		return -1; //This will probably cause an error.
	}
	
	private static class Dye {
		private String name;
		private int id;
		public final ItemStack[] DYES;
		
		private Dye(String name, int id){
			this.name = name;
			this.id = id;
			DYES = setDyes();
		}

		private ItemStack[] setDyes(){
			ItemStack[] carl = new ItemStack[OreDictionary.getOres(name).size()];
			for(int i=0; i < OreDictionary.getOres(name).size(); i++) {
				carl[i] = OreDictionary.getOres(name).get(i);
			}
			return carl;
		}
		
		public int getDyeId(){
			return id;
		}
	}
}
