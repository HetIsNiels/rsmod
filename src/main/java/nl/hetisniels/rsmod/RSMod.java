package nl.hetisniels.rsmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import nl.hetisniels.rsmod.definition.RSModBlocks;
import nl.hetisniels.rsmod.proxy.CommonProxy;

@Mod(modid = RSMod.ID, version = RSMod.VERSION)
public class RSMod {
	public static final String ID = "rsmod";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide = "nl.hetisniels.rsmod.proxy.ClientProxy", serverSide = "nl.hetisniels.rsmod.proxy.ServerProxy")
	public static CommonProxy PROXY;
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(ID);
	public static final CreativeTab CREATIVE_TAB = new CreativeTab(ID);

	@Instance
	public static RSMod INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		PROXY.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		PROXY.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		PROXY.postInit(e);
	}
}
