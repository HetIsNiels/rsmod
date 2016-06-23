package nl.hetisniels.rsmod.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.block.BlockBase;
import nl.hetisniels.rsmod.definition.RSModBlocks;
import nl.hetisniels.rsmod.network.PipeDataMessage;
import nl.hetisniels.rsmod.tile.TilePipe;

public class CommonProxy {
	private static int NETWORK_ID = 0;

	public void preInit(FMLPreInitializationEvent e) {
		this.registerNetworkHelper(PipeDataMessage.class, Side.CLIENT);

		GameRegistry.registerTileEntity(TilePipe.class, RSMod.ID + ":pipe");

		this.registerBlockAndItem(RSModBlocks.PIPE_WOOD);
		this.registerBlockAndItem(RSModBlocks.PIPE);
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	private void registerNetworkHelper(Class<PipeDataMessage> helper, Side side) {
		RSMod.NETWORK.registerMessage(helper, helper, CommonProxy.NETWORK_ID++, side);
	}

	private void registerBlockAndItem(BlockBase block) {
		GameRegistry.<Block>register(block);
		GameRegistry.<Item>register(block.getAsItem());
	}
}
