package nl.hetisniels.rsmod;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.hetisniels.rsmod.block.BlockPipe;
import nl.hetisniels.rsmod.block.BlockPipeWood;
import nl.hetisniels.rsmod.block.IBlockHighlight;
import nl.hetisniels.rsmod.render.RendererPipe;
import nl.hetisniels.rsmod.tile.TilePipe;

@Mod(modid = RSMod.MODID, version = RSMod.VERSION)
public class RSMod {
	public static final String MODID = "rsmod";
	public static final String VERSION = "1.0";
	public static final CreativeTab CREATIVE_TAB = new CreativeTab(MODID);
	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	public static int NETWORK_ID = 0;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		BlockPipe block = new BlockPipe(Material.ROCK, MapColor.STONE);
		CREATIVE_TAB.setTabIconItem(block.registerItemForBlock());
		GameRegistry.<Block>register(block);

		block = new BlockPipeWood(Material.WOOD, MapColor.WOOD);
		block.registerItemForBlock();
		GameRegistry.register(block);

		MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.registerTileEntity(TilePipe.class, MODID);

		ClientRegistry.bindTileEntitySpecialRenderer(TilePipe.class, new RendererPipe());
		NETWORK_WRAPPER.registerMessage(PipeDataMessage.class, PipeDataMessage.class, NETWORK_ID++, Side.CLIENT);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
		try {
			EntityPlayer player = event.getPlayer();

			World world = player.worldObj;
			BlockPos blockPos = event.getTarget().getBlockPos();
			IBlockState state = world.getBlockState(blockPos);
			Block block = state.getBlock();

			if (block instanceof IBlockHighlight)
				event.setCanceled(((IBlockHighlight) block).drawBlockHighlight(world, player, blockPos, block, event.getPartialTicks()));
		} catch (NullPointerException e) {
			// do nothing, thrown because the player looked at an entity
		}
	}
}
