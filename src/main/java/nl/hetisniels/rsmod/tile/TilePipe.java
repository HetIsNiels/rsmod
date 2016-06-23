package nl.hetisniels.rsmod.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nl.hetisniels.rsmod.network.PipeDataMessage;
import nl.hetisniels.rsmod.RSMod;

import javax.annotation.Nullable;

public class TilePipe extends TileBase {
	private ItemStackHandler buffer;

	public TilePipe() {
		this.buffer = new ItemStackHandler(9) {
			private TilePipe pipe;

			@Override
			protected void onContentsChanged(int slot) {
				if (pipe != null) {
					pipe.sync();
				}
			}

			ItemStackHandler bind(TilePipe pipe) {
				this.pipe = pipe;
				return this;
			}
		}.bind(this);
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			RSMod.NETWORK.sendToAll(new PipeDataMessage(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getCurrentItemStacks()));
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		this.sync();

		return super.getUpdatePacket();
	}

	public ItemStack[] getCurrentItemStacks() {
		ItemStack[] itemStacks = new ItemStack[this.buffer.getSlots()];

		for (int i = 0; i < this.buffer.getSlots(); i++)
			if (this.buffer.getStackInSlot(i) != null)
				itemStacks[i] = this.buffer.getStackInSlot(i);

		return itemStacks;
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
