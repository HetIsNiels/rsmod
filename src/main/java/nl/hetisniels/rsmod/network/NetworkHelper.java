package nl.hetisniels.rsmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class NetworkHelper<self extends NetworkHelper> implements IMessage, IMessageHandler<self, IMessage> {
	@Override
	public abstract void fromBytes(ByteBuf buf);

	@Override
	public abstract void toBytes(ByteBuf buf);

	@SuppressWarnings("unchecked")
	@Override
	public self onMessage(self message, MessageContext context) {
		if (message != this)
				return (self) message.onMessage(message, context);

		return this.handleMessage(context);
	}

	public abstract self handleMessage(MessageContext context);
}
