package nl.hetisniels.rsmod.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import nl.hetisniels.rsmod.tile.TilePipe;

public class RendererPipe extends TileEntitySpecialRenderer<TilePipe> {
	private RenderItem itemRenderer;

	public RendererPipe() {
		this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void renderTileEntityAt(TilePipe pipe, double x, double y, double z, float partialTicks, int destroyStage) {
		ItemStack[] itemStacks = pipe.getCurrentItemStacks();

		for (ItemStack itemStack : itemStacks) {
			if (itemStack == null)
				continue;

			EntityItem entityitem = new EntityItem(pipe.getWorld(), 0, 0, 0, itemStack);
			entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;

			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5d, y + 0.5d, z + 0.5d);
			GlStateManager.disableLighting();
			GlStateManager.scale(0.6d, 0.6d, 0.6d);
			GlStateManager.pushAttrib();

			RenderHelper.enableStandardItemLighting();
			this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();

			GlStateManager.popAttrib();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}
