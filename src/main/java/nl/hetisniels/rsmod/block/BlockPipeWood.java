package nl.hetisniels.rsmod.block;

import nl.hetisniels.rsmod.RSMod;

public class BlockPipeWood extends BlockPipe {
	public BlockPipeWood(String name) {
		super(name);

		setHardness(0.8F);
		setCreativeTab(RSMod.CREATIVE_TAB);
	}
}
