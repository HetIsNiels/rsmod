package nl.hetisniels.rsmod.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.definition.RSModBlocks;
import nl.hetisniels.rsmod.event.drawBlockHighlightEventHandler;
import nl.hetisniels.rsmod.render.RendererPipe;
import nl.hetisniels.rsmod.tile.TilePipe;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);

		ClientRegistry.bindTileEntitySpecialRenderer(TilePipe.class, new RendererPipe());
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);

		MinecraftForge.EVENT_BUS.register(new drawBlockHighlightEventHandler());
		RSMod.CREATIVE_TAB.setTabIconItem(RSModBlocks.PIPE.getAsItem());
	}
}
