package nl.hetisniels.rsmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.hetisniels.rsmod.block.BlockPipe;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

public class EntityItemBasket extends Entity {
	private static final ItemBasketItemDataSerializer ITEM_BASKET_ITEM_SERIALIZER = new ItemBasketItemDataSerializer();
	private static final ItemBasketOffsetDataSerializer ITEM_BASKET_OFFSET_SERIALIZER = new ItemBasketOffsetDataSerializer();

	static {
		DataSerializers.registerSerializer(ITEM_BASKET_ITEM_SERIALIZER);
		DataSerializers.registerSerializer(ITEM_BASKET_OFFSET_SERIALIZER);
	}

	private static final DataParameter<List<ItemStack>> ITEMS = EntityDataManager.createKey(EntityItemBasket.class, ITEM_BASKET_ITEM_SERIALIZER);
	private static final DataParameter<double[]> OFFSET = EntityDataManager.createKey(EntityItemBasket.class, ITEM_BASKET_OFFSET_SERIALIZER);
	private static final DataParameter<double[]> OFFSET_GOAL = EntityDataManager.createKey(EntityItemBasket.class, ITEM_BASKET_OFFSET_SERIALIZER);

	public EntityItemBasket(World world) {
		super(world);

		this.setSize(1, 1);
		this.preventEntitySpawning = true;
	}

	public EntityItemBasket(World world, BlockPos pos, ItemStack stackInSlot) {
		this(world);

		this.setPosition(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
		this.getDataManager().get(ITEMS).add(stackInSlot);

		System.out.println("creato");
		System.out.println(worldObj.isRemote ? "remote" : "no remote");
	}

	@Override
	public void onUpdate() {
		this.moveOffset();
		this.checkOffset();
	}

	public double[] calculateGoal(double[] current) {
		double x = 0;
		double y = 0;
		double z = 0;

		if (current[0] == 0 && current[1] == 0 && current[2] == 0) {
			if (worldObj.getBlockState(new BlockPos(this.posX + 1, this.posY, this.posZ)).getBlock() instanceof BlockPipe)
				x += 0.5d;
			else if (worldObj.getBlockState(new BlockPos(this.posX - 1, this.posY, this.posZ)).getBlock() instanceof BlockPipe)
				x -= 0.5d;
			else if (worldObj.getBlockState(new BlockPos(this.posX, this.posY + 1, this.posZ)).getBlock() instanceof BlockPipe)
				y += 0.5d;
			else if (worldObj.getBlockState(new BlockPos(this.posX, this.posY - 1, this.posZ)).getBlock() instanceof BlockPipe)
				y -= 0.5d;
			else if (worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ + 1)).getBlock() instanceof BlockPipe)
				z += 0.5d;
			else if (worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ - 1)).getBlock() instanceof BlockPipe)
				z -= 0.5d;
			else
				this.dropAndDie();
		}

		return new double[]{
				x, y, z
		};
	}

	public void moveOffset() {
		double[] offset = this.getDataManager().get(OFFSET);
		double[] goal = this.getDataManager().get(OFFSET_GOAL);

		if (offset[0] == goal[0] && offset[1] == goal[1] && offset[2] == goal[2]) {
			goal = this.calculateGoal(goal);
			this.getDataManager().set(OFFSET_GOAL, goal);
		}

		if (goal[0] > offset[0]) {
			offset[0] += .02d;
			offset[0] = Math.min(goal[0], offset[0]);
		} else if (goal[0] < offset[0]) {
			offset[0] -= .02d;
			offset[0] = Math.max(goal[0], offset[0]);
		} else if (goal[1] > offset[1]) {
			offset[1] += .02d;
			offset[1] = Math.min(goal[1], offset[1]);
		} else if (goal[1] < offset[1]) {
			offset[1] -= .02d;
			offset[1] = Math.max(goal[1], offset[1]);
		} else if (goal[2] > offset[2]) {
			offset[2] += .02d;
			offset[2] = Math.min(goal[2], offset[2]);
		} else if (goal[2] < offset[2]) {
			offset[2] -= .02d;
			offset[2] = Math.max(goal[2], offset[2]);
		}
	}

	private void checkOffset() {
		if (worldObj.isRemote)
			return;

		double[] offset = this.getDataManager().get(OFFSET);

		if (offset[0] > .5d) {
			this.moveOrDrop(this.posX + 1, this.posY, this.posZ);
			offset[0] = -.5d;
		} else if (offset[0] < -.5d) {
			this.moveOrDrop(this.posX - 1, this.posY, this.posZ);
			offset[0] = .5d;
		} else if (offset[1] > .5d) {
			this.moveOrDrop(this.posX, this.posY + 1, this.posZ);
			offset[1] = -.5d;
		} else if (offset[1] < -.5d) {
			this.moveOrDrop(this.posX, this.posY - 1, this.posZ);
			offset[1] = .5d;
		} else if (offset[2] > .5d) {
			this.moveOrDrop(this.posX, this.posY, this.posZ + 1);
			offset[2] = -.5d;
		} else if (offset[2] < -.5d) {
			this.moveOrDrop(this.posX, this.posY, this.posZ - 1);
			offset[2] = .5d;
		}
	}

	private void moveOrDrop(double x, double y, double z) {
		if (worldObj.isRemote)
			return;

		BlockPos pos = new BlockPos(x, y, z);

		//System.out.println(worldObj.getBlockState(pos).getBlock().getUnlocalizedName());

		if (worldObj.getBlockState(pos).getBlock() instanceof BlockPipe) {
			this.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		} else {
			this.dropAndDie();
		}
	}

	private void dropAndDie() {
		if (worldObj.isRemote)
			return;

		double[] offset = getOffset();
		for (ItemStack itemStack : getItems())
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX + offset[0], posY + offset[1], posZ + offset[2], itemStack));

		this.getDataManager().get(ITEMS).clear();
		this.setDead();
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(ITEMS, new ArrayList<ItemStack>());
		this.getDataManager().register(OFFSET, new double[]{0, 0, 0});
		this.getDataManager().register(OFFSET_GOAL, new double[]{0, 0, 0});
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		NBTTagList items = compound.getTagList("items", TAG_COMPOUND);

		this.getDataManager().get(ITEMS).clear();
		for (int i = 0; i < items.tagCount(); i++)
			this.getDataManager().get(ITEMS).add(ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i)));

		this.getDataManager().get(OFFSET)[0] = compound.getDouble("offsetX");
		this.getDataManager().get(OFFSET)[1] = compound.getDouble("offsetY");
		this.getDataManager().get(OFFSET)[2] = compound.getDouble("offsetZ");

		this.getDataManager().get(OFFSET_GOAL)[0] = compound.getDouble("offsetGoalX");
		this.getDataManager().get(OFFSET_GOAL)[1] = compound.getDouble("offsetGoalY");
		this.getDataManager().get(OFFSET_GOAL)[2] = compound.getDouble("offsetGoalZ");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		NBTTagList items = new NBTTagList();

		for (ItemStack itemStack : this.getDataManager().get(ITEMS))
			items.appendTag(itemStack.serializeNBT());

		compound.setTag("items", items);
		compound.setDouble("offsetX", this.getDataManager().get(OFFSET)[0]);
		compound.setDouble("offsetY", this.getDataManager().get(OFFSET)[1]);
		compound.setDouble("offsetZ", this.getDataManager().get(OFFSET)[2]);
		compound.setDouble("offsetGoalX", this.getDataManager().get(OFFSET_GOAL)[0]);
		compound.setDouble("offsetGoalY", this.getDataManager().get(OFFSET_GOAL)[1]);
		compound.setDouble("offsetGoalZ", this.getDataManager().get(OFFSET_GOAL)[2]);
	}

	public ItemStack[] getItems() {
		return this.getDataManager().get(ITEMS).toArray(new ItemStack[this.getDataManager().get(ITEMS).size()]);
	}

	public double[] getOffset() {
		return this.getDataManager().get(OFFSET);
	}

	private static class ItemBasketItemDataSerializer implements DataSerializer<List<ItemStack>> {
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

	private static class ItemBasketOffsetDataSerializer implements DataSerializer<double[]> {
		@Override
		public void write(PacketBuffer packetBuffer, double[] offset) {
			packetBuffer.writeDouble(offset[0]);
			packetBuffer.writeDouble(offset[1]);
			packetBuffer.writeDouble(offset[2]);
		}

		@Override
		public double[] read(PacketBuffer packetBuffer) {
			return new double[]{
					packetBuffer.readDouble(),
					packetBuffer.readDouble(),
					packetBuffer.readDouble(),
			};
		}

		@Override
		public DataParameter<double[]> createKey(int id) {
			return new DataParameter<double[]>(id, this);
		}
	}
}
