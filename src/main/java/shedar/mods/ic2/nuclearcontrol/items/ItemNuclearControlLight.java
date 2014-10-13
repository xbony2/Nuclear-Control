package shedar.mods.ic2.nuclearcontrol.items;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemNuclearControlLight extends ItemBlock{

	public ItemNuclearControlLight(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(IC2NuclearControl.tabIC2NC);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack item) {
		return "tile.nclight" + item.getItemDamage();
	}
}
