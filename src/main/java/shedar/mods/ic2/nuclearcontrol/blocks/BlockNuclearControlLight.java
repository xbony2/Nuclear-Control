package shedar.mods.ic2.nuclearcontrol.blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.api.BonyDebugger;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.utils.LightDamages;

public class BlockNuclearControlLight extends Block{
	public static Map<Integer, Boolean> subblocks;
	private IIcon[] icon;

	public BlockNuclearControlLight() {
		super(Material.redstoneLight);
		subblocks = new HashMap<Integer, Boolean>();
		setHardness(0.3F);
		setStepSound(soundTypeGlass);
		setCreativeTab(IC2NuclearControl.tabIC2NC);
		register(LightDamages.DAMAGE_WHITE_OFF, false);
		register(LightDamages.DAMAGE_WHITE_ON, true);
		icon = new IIcon[subblocks.size() + 1];
	}
	
	public void register(int damage, boolean isOn) {
		subblocks.put(damage, isOn);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 1 || meta % 2 == 1) return 15;
		return 0;
	}
	
	@Override
	public int damageDropped(int i) {
		if(i == 0 || i % 2 == 0) return i;
		return i - 1;
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return icon[metadata];
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register){
		for(int i = 0; i <= subblocks.size(); i++){
			icon[i] = register.registerIcon("nuclearcontrol:light/lamp" + i);
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 1 || meta % 2 == 1){
			if(!world.isRemote){
				if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
					world.setBlock(x, y, z, this, meta - 1, 2);
				}
			}
		}else{
			if (!world.isRemote) {
				if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
					world.setBlock(x, y, z, this, meta + 1, 2);
				}
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 1 || meta % 2 == 1){
			if(!world.isRemote) {
				if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
					world.setBlock(x, y, z, this, meta - 1, 2);
				}
			}
		}else{
			if(!world.isRemote){
				if(world.isBlockIndirectlyGettingPowered(x, y, z)){
					world.setBlock(x, y, z, this, meta + 1, 2);
				}
			}
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void getSubBlocks(Item id, CreativeTabs tab, List itemList) {
		for (int i = 0; i <= LightDamages.DAMAGE_MAX; i++) {
			itemList.add(new ItemStack(this, 1, i));
			/*if(i == 0 || i % 2 == 0){
				
			}*/
		}
	}
	
}
