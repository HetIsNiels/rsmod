package nl.hetisniels.rsmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import nl.hetisniels.rsmod.RSMod;

public abstract class BlockBase extends Block {
	private final String name;

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
}
