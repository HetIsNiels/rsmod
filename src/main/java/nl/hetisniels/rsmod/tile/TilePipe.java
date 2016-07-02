package nl.hetisniels.rsmod.tile;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nl.hetisniels.rsmod.entity.EntityItemBasket;

public class TilePipe extends TileBase {
	private final IItemHandler itemHandler;

	public TilePipe() {
		this.itemHandler = new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				if (getStackInSlot(slot) != null) {
					worldObj.spawnEntityInWorld(new EntityItemBasket(worldObj, getPos(), getStackInSlot(slot)));
					setStackInSlot(slot, null);
				}
			}
		};
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
}
