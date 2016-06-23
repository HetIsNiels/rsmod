package nl.hetisniels.rsmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.item.ItemBlockBase;

public abstract class BlockBase extends Block {
	private final String name;
	private ItemBlock itemBlock;

	public BlockBase(String name) {
		super(Material.ROCK);

		this.name = name;
		setRegistryName(RSMod.ID, name);
	}

	public String getName() {
		return name;
	}

	@Override
	public String getUnlocalizedName() {
		return "block." + RSMod.ID + ":" + name;
	}

	public Item getAsItem() {
		if(this.itemBlock == null)
			this.itemBlock = new ItemBlockBase(this);

		return this.itemBlock;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
		return EnumBlockRenderType.MODEL;
	}
}
