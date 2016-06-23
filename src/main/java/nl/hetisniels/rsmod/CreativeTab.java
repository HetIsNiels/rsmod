package nl.hetisniels.rsmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs {
	private Item tabIconItem;

	public CreativeTab(String label) {
		super(label);

		this.tabIconItem = Items.EGG;
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

	@Override
	public String getBackgroundImageName() {
		return CreativeTabs.SEARCH.getBackgroundImageName();
	}

	public void setTabIconItem(Item tabIconItem) {
		this.tabIconItem = tabIconItem;
	}

	@Override
	public Item getTabIconItem() {
		return this.tabIconItem;
	}
}
