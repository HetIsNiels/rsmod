package nl.hetisniels.rsmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nl.hetisniels.rsmod.tile.TilePipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PipeDataMessage extends NetworkHelper<PipeDataMessage> {
    private double x;
    private double y;
    private double z;

    private List<ItemStack> items = new ArrayList<ItemStack>();

    public PipeDataMessage() {

    }

    public PipeDataMessage(double x, double y, double z, ItemStack[] itemStacks) {
        this.x = x;
        this.y = y;
        this.z = z;

        Collections.addAll(items, itemStacks);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();

        int items = buf.readInt();

        this.items.clear();
        for (int i = 0; i < items; ++i) {
            boolean skip = buf.readBoolean();

            if (skip)
                continue;

            int size = buf.readInt();

            ItemStack stack = ByteBufUtils.readItemStack(buf);
            if (stack != null) {
                stack.stackSize = size;

                this.items.add(stack);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);

        buf.writeInt(items.size());

        for (ItemStack item : items) {
            if (item == null) {
                buf.writeBoolean(true);
                continue;
            }

            buf.writeBoolean(false);
            buf.writeInt(item.stackSize);
            ByteBufUtils.writeItemStack(buf, item);
        }
    }

    @Override
    public PipeDataMessage handleMessage(MessageContext context) {
        TilePipe pipe = (TilePipe) Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(this.x, this.y, this.z));

        if (pipe == null) {
            System.out.println("pipe is null!");
            return null;
        }

        //pipe.setItems(this.items.toArray(new ItemStack[this.items.size()]));

        return null;
    }
}
