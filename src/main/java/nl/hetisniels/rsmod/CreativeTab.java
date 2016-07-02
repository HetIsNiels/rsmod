package nl.hetisniels.rsmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs {
	private Item tabIconItem;

	public CreativeTab(String label) {
		super(label);
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

	public void setTabIconItem(Item tabIconItem) {
		this.tabIconItem = tabIconItem;
	}
}
