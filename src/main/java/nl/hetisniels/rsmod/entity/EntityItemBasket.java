package nl.hetisniels.rsmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

public class EntityItemBasket extends Entity {
	private static final ItemBasketDataSerializer ITEM_BASKET_SERIALIZER = new ItemBasketDataSerializer();

	static {
		DataSerializers.registerSerializer(ITEM_BASKET_SERIALIZER);
	}

	public static final DataParameter<List<ItemStack>> ITEMS = EntityDataManager.createKey(EntityItemBasket.class, ITEM_BASKET_SERIALIZER);

	public EntityItemBasket(World world) {
		super(world);

		this.preventEntitySpawning = true;

		this.setSize(0.25F, 0.25F);
	}

	@Override
	public void onUpdate() {
		if(this.getDataManager().get(ITEMS).size() < 100 && Math.random() < 0.3d)
			this.getDataManager().get(ITEMS).add(new ItemStack(Items.DIAMOND));
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(ITEMS, new ArrayList<ItemStack>());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		NBTTagList items = compound.getTagList("items", TAG_COMPOUND);

		this.getDataManager().get(ITEMS).clear();
		for (int i = 0; i < items.tagCount(); i++)
			this.getDataManager().get(ITEMS).add(ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i)));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		NBTTagList items = new NBTTagList();

		for (ItemStack itemStack : this.getDataManager().get(ITEMS))
			items.appendTag(itemStack.serializeNBT());

		compound.setTag("items", items);
	}

	public ItemStack[] getItems() {
		return this.getDataManager().get(ITEMS).toArray(new ItemStack[this.getDataManager().get(ITEMS).size()]);
	}

	private static class ItemBasketDataSerializer implements DataSerializer<List<ItemStack>> {
		@Override
		public void write(PacketBuffer packetBuffer, List<ItemStack> itemStacks) {
			packetBuffer.writeInt(itemStacks.size());

			for (ItemStack itemStack : itemStacks) {
				packetBuffer.writeItemStackToBuffer(itemStack);
			}
		}

		@Override
		public List<ItemStack> read(PacketBuffer packetBuffer) {
			List<ItemStack> itemStacks = new ArrayList<ItemStack>();

			try {
				int size = packetBuffer.readInt();

				for (int i = 0; i < size; i++)
					itemStacks.add(packetBuffer.readItemStackFromBuffer());
			} catch (Exception ignore) {

			}

			return itemStacks;
		}

		@Override
		public DataParameter<List<ItemStack>> createKey(int id) {
			return new DataParameter<List<ItemStack>>(id, this);
		}
	}
}
