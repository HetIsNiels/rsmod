package nl.hetisniels.rsmod.item;

import net.minecraft.item.ItemBlock;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.block.BlockBase;

public class ItemBlockBase extends ItemBlock {
	public final BlockBase block;

	public ItemBlockBase(BlockBase block) {
		super(block);

		this.block = block;

		setRegistryName(block.getRegistryName());
		setCreativeTab(RSMod.CREATIVE_TAB);
	}
}