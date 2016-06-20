package nl.hetisniels.rsmod.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nl.hetisniels.rsmod.RSMod;

public class BlockPipeWood extends BlockPipe {
	public BlockPipeWood(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	@Override
	protected void setup() {
		setHardness(0.8F);
		setUnlocalizedName(RSMod.MODID + ":pipe_wood");
		setRegistryName(RSMod.MODID, "pipe_wood");
		setCreativeTab(RSMod.CREATIVE_TAB);
	}
}
