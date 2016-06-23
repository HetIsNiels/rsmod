package nl.hetisniels.rsmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import nl.hetisniels.rsmod.block.BlockBase;

public class CreativeTab extends CreativeTabs {
	private Item tabIconItem;

	public CreativeTab(String label, Item tabIconItem) {
		super(label);

		this.tabIconItem = tabIconItem;
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

	@Override
	public String getBackgroundImageName() {
		return CreativeTabs.SEARCH.getBackgroundImageName();
	}

	@Override
	public Item getTabIconItem() {
		return this.tabIconItem;
	}
}
