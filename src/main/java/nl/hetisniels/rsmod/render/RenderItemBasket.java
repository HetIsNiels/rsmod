package nl.hetisniels.rsmod.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nl.hetisniels.rsmod.entity.EntityItemBasket;

public class RenderItemBasket extends Render<EntityItemBasket> {
	public RenderItemBasket() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(EntityItemBasket entity, double x, double y, double z, float entityYaw, float partialTicks) {
		ItemStack[] items = entity.getItems();

		if (items.length == 0)
			return;

		double xOffset = 0;
		double zOffset = 0;
		for (ItemStack itemStack : items) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.translate(x + xOffset, y, z + zOffset);
			GlStateManager.scale(0.4d, 0.4d, 0.4d);
			GlStateManager.pushAttrib();

			RenderHelper.enableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();


			GlStateManager.popAttrib();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();

			xOffset += 0.2d;
			if (xOffset >= 1d) {
				xOffset = 0d;
				zOffset += 0.5d;

				if (zOffset >= 1d)
					break;
			}
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityItemBasket entityItemBasket) {
		return null;
	}
}
