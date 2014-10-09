package shedar.mods.ic2.nuclearcontrol.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.LightDamages;

public class BlockNuclearControlLight extends Block{
	public boolean isOn;
	public int damageOff;
	public int damageOn = damageOff + 1;

	public BlockNuclearControlLight() {
		super(Material.redstoneLight);
		setHardness(0.3F);
		setStepSound(soundTypeGlass);
		if(!isOn) setCreativeTab(IC2NuclearControl.tabIC2NC);
		register(LightDamages.DAMAGE_WHITE_OFF, false);
		register(LightDamages.DAMAGE_WHITE_ON, true);
	}
	
	public void register(int damage, boolean on){
		isOn = on;
		if(isOn) damageOff = damage - 1;
		else damageOff = damage;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		if(isOn) return 15;
		return 0;
	}
	
	@Override
	public int damageDropped(int i) {
		return damageOff;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register){
		if(isOn) blockIcon = register.registerIcon("nuclearcontrol:light/on/lamp" + damageOn);
		else blockIcon = register.registerIcon("nuclearcontrol:light/off/lamp" + damageOff);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if(isOn){
			if(!world.isRemote){
				if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
					world.setBlock(x, y, z, this, damageOff, 2);
				}
			}
		}
		if(!isOn){
			if (!world.isRemote) {
				if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
					world.setBlock(x, y, z, this, damageOn, 2);
				}
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
		if(isOn){
			if(!world.isRemote) {
				if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
					world.setBlock(x, y, z, this, damageOff, 2);
				}
			}
		}
		if(!isOn){
			if(!world.isRemote){
				if(world.isBlockIndirectlyGettingPowered(x, y, z)){
					world.setBlock(x, y, z, this, damageOn, 2);
				}
			}
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void getSubBlocks(Item id, CreativeTabs tab, List itemList) {
		for (int i = 0; i <= LightDamages.DAMAGE_MAX; i++) {
			itemList.add(new ItemStack(this, 1, i));
		}
	}
}
