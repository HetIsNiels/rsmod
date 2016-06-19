package nl.hetisniels.rsmod;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = RSMod.MODID, version = RSMod.VERSION)
public class RSMod {
	public static final String MODID = "rsmod";
	public static final String VERSION = "1.0";
	public static final CreativeTab CREATIVE_TAB = new CreativeTab(null, MODID + " " + VERSION);

	@EventHandler
	public void init(FMLInitializationEvent event) {
		BlockPipe block = new BlockPipe(Material.GLASS, MapColor.CLOTH);
		Item blockItem = block.createItemForBlock();
		CREATIVE_TAB.setTabIconItem(blockItem);
		GameRegistry.<Block>register(block);
		GameRegistry.register(blockItem);
		MinecraftForge.EVENT_BUS.register(this);

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(blockItem, 0, new ModelResourceLocation(MODID + ":pipe", "inventory"));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
		if (e.getPlayer().worldObj.getBlockState(e.getTarget().getBlockPos()).getBlock() instanceof BlockPipe)
			((BlockPipe) e.getPlayer().worldObj.getBlockState(e.getTarget().getBlockPos()).getBlock()).drawBlockHighlight(e);
	}
}
