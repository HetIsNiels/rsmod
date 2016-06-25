package nl.hetisniels.rsmod.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.entity.EntityItemBasket;
import org.lwjgl.opengl.GL11;

public class RenderItemBasket extends Render<EntityItemBasket> {
	public RenderItemBasket() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(EntityItemBasket entity, double x, double y, double z, float entityYaw, float partialTicks) {
		ItemStack[] items = entity.getItems();
		double[] offset = entity.getOffset();

		if (items.length == 0)
			return;

		for (ItemStack itemStack : items) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.translate(x + offset[0], y + 0.5d + offset[1], z + offset[2]);
			GlStateManager.scale(0.4d, 0.4d, 0.4d);
			GlStateManager.pushAttrib();

			RenderHelper.enableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();

			GlStateManager.popAttrib();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
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
