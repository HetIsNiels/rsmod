package nl.hetisniels.rsmod.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nl.hetisniels.rsmod.PipeDataMessage;
import nl.hetisniels.rsmod.RSMod;

public class TilePipe extends TileEntity {
	private ItemStackHandler buffer;

	public TilePipe() {
		this.buffer = new ItemStackHandler(9) {
			private TilePipe pipe;

			@Override
			protected void onContentsChanged(int slot) {
				if (pipe != null){
					RSMod.NETWORK_WRAPPER.sendToAll(new PipeDataMessage(this.pipe.getPos().getX(), this.pipe.getPos().getY(), this.pipe.getPos().getZ(), this.stacks));
				}
			}

			ItemStackHandler bind(TilePipe pipe) {
				this.pipe = pipe;
				return this;
			}
		}.bind(this);
	}

	public ItemStack[] getCurrentItemStacks() {
		ItemStack[] itemStacks = new ItemStack[this.buffer.getSlots()];

		for (int i = 0; i < this.buffer.getSlots(); i++)
			if (this.buffer.getStackInSlot(i) != null)
				itemStacks[i] = this.buffer.getStackInSlot(i);

		return itemStacks;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return 32 * 32;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) buffer;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setTag("buffer", this.buffer.serializeNBT());

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.buffer.deserializeNBT(compound.getCompoundTag("buffer"));
	}

	public void setItems(ItemStack[] itemStacks) {
		this.buffer = new ItemStackHandler(itemStacks);
	}
}
